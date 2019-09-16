package com.wy.factory;

import androidx.annotation.StringRes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.wy.common.app.Application;
import com.wy.common.factory.data.DataSource;
import com.wy.factory.data.group.GroupCenter;
import com.wy.factory.data.group.GroupDispatcher;
import com.wy.factory.data.message.MessageCenter;
import com.wy.factory.data.message.MessageDispatcher;
import com.wy.factory.data.user.UserCenter;
import com.wy.factory.data.user.UserDispatcher;
import com.wy.factory.model.api.RspModel;
import com.wy.factory.persistence.Account;
import com.wy.factory.utils.DBFlowExclusionStrategy;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* 名称: ITalker.com.wy.factory.Factory
 * 用户: _VIEW
 * 时间: 2019/9/5,22:13
 * 描述: 一些工厂方法
 */
public class Factory {
    //单例模式
    private static volatile Factory instance;
    //全局的线程池
    private final Executor executor;
    //全局的Gson
    private final Gson gson;

    private Factory() {
        //新建一个4线程线程池
        executor = Executors.newFixedThreadPool(4);
        //Gson初始化,设置时间格式，设置过滤器
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                //设置一个过滤器，数据库级别的Model不进行Json转换
                .setExclusionStrategies(new DBFlowExclusionStrategy())
                .create();
    }

    public static Factory getInstance() {
        if (instance == null) {
            synchronized (Factory.class) {
                if (instance == null) {
                    instance = new Factory();
                }
            }
        }
        return instance;
    }

    /**
     * 异步运行的方法
     *
     * @param runnable Runnable
     */
    public static void runOnAsync(Runnable runnable) {
        //拿到单例，拿到线程池，之后异步执行
        getInstance().executor.execute(runnable);
    }

    public static Gson getGson() {
        return getInstance().gson;
    }

    /**
     * 进行错误Code的解析，
     * 把网络返回的Code值进行统一的规划并返回为一个String资源
     *
     * @param model    RspModel
     * @param callback DataSource.FailedCallback 用于返回一个错误的资源Id
     */
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback) {
        if (model == null)
            return;

        // 进行Code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Application.showToast(R.string.data_rsp_error_account_token);
                //instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    private static void decodeRspCode(@StringRes final int resId,
                                      final DataSource.FailedCallback callback) {
        if (callback != null)
            callback.onDataNotAvailable(resId);
    }

    /**
     * 处理推送过来的消息
     *
     * @param message 信息
     */
    public static void dispatchPush(String message) {

    }

    /**
     * Factory的初始化
     */
    public static void setUp() {
        //初始化数据
        FlowManager.init(new FlowConfig.Builder(Application.getInstance())
                .openDatabasesOnInit(true)//数据库在初始化的时候就打开
                .build());
        //持久化的数据的初始化
        Account.load(Application.getInstance());
    }

    /**
     * 获取一个用户中心的实现类
     *
     * @return 用户中心的规范接口
     */
    public static UserCenter getUserCenter() {
        return UserDispatcher.instance();
    }

    /**
     * 获取一个消息中心的实现类
     *
     * @return 消息中心的规范接口
     */
    public static MessageCenter getMessageCenter() {
        return MessageDispatcher.instance();
    }


    /**
     * 获取一个群处理中心的实现类
     *
     * @return 群中心的规范接口
     */
    public static GroupCenter getGroupCenter() {
        return GroupDispatcher.instance();
    }
}
