package com.wy.factory.data.helper;

import android.text.TextUtils;

import com.wy.common.factory.data.DataSource;
import com.wy.factory.Factory;
import com.wy.factory.R;
import com.wy.factory.model.api.RspModel;
import com.wy.factory.model.api.account.AccountRspModel;
import com.wy.factory.model.api.account.LoginModel;
import com.wy.factory.model.api.account.RegisterModel;
import com.wy.factory.model.db.User;
import com.wy.factory.net.Network;
import com.wy.factory.net.RemoteService;
import com.wy.factory.persistence.Account;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* 名称: ITalker.com.wy.factory.data.helper.AccountHelper
 * 用户: _VIEW
 * 时间: 2019/9/9,16:51
 * 描述: 账号部分的操作
 */
public class AccountHelper {

    /**
     * 注册的接口，异步调用
     *
     * @param model        传递一个model进来
     * @param userCallback 成功与失败的接口回送
     */
    public static void register(RegisterModel model, final DataSource.Callback<User> userCallback) {
        //调用Retrofit对网络请求接口做代理
        RemoteService service = Network.remote();
        //得到一个Call
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        call.enqueue(new AccountRspCallback(userCallback));
    }

    /**
     * 对设备Id进行绑定的操作
     *
     * @param callback DataSource.Callback
     */
    public static void bindPush(DataSource.Callback<User> callback) {
        //检查是否有pushId
        String pushId = Account.getPushId();
        if (TextUtils.isEmpty(pushId)) {
            return;
        }
        //调用Retrofit对网络接口做请求代理
        RemoteService service = Network.remote();
        Call<RspModel<AccountRspModel>> call = service.accountBind(pushId);
        call.enqueue(new AccountRspCallback(callback));

    }

    /**
     * 登录的调用
     *
     * @param model        登录的Model
     * @param userCallback 成功与失败的接口回调
     */
    public static void login(LoginModel model, final DataSource.Callback<User> userCallback) {
        //调用Retrofit对网络请求接口做代理
        RemoteService service = Network.remote();
        //得到一个Call
        Call<RspModel<AccountRspModel>> call = service.accountLogin(model);
        call.enqueue(new AccountRspCallback(userCallback));
    }

    /**
     * 请求的回调部分封装
     */
    private static class AccountRspCallback implements Callback<RspModel<AccountRspModel>> {
        final DataSource.Callback<User> userCallback;

        AccountRspCallback(DataSource.Callback<User> callback) {
            this.userCallback = callback;
        }

        @Override
        public void onResponse(Call<RspModel<AccountRspModel>> call,
                               Response<RspModel<AccountRspModel>> response) {
            //请求成功，从返回中获取全局model，内部是使用的Gson解析
            RspModel<AccountRspModel> rspModel = response.body();
            //判断绑定状态，是否绑定设备
            if (rspModel.success()) {
                //拿到实体
                AccountRspModel accountRspModel = rspModel.getResult();
                //获取到我的信息
                User user = accountRspModel.getUser();
                //数据库写入和缓存绑定，然后返回
                //第一种保存方式
                user.save();
                //第二种通过 ModelAdapter
                    /*
                        FlowManager.getModelAdapter(User.class).save(user);
                        第三种 通过事务
                        DatabaseDefinition databaseDefinition = FlowManager.getDatabase(AppDatabase.class);
                        databaseDefinition.beginTransactionAsync(new ITransaction() {
                            @Override
                            public void execute(DatabaseWrapper databaseWrapper) {
                                FlowManager.getModelAdapter(User.class).save(user);
                            }
                        }).execute();*/
                //持久化保存到XML中
                Account.login(accountRspModel);
                if (accountRspModel.isBind()) {
                    if (userCallback != null)
                        userCallback.onDataLoad(user);
                } else {
                    //进行绑定操作
                    bindPush(userCallback);
                }

            } else {
                //TODO对返回的RspModel中失败的Code解析，解析到对应的String资源上面
                Factory.decodeRspCode(rspModel, userCallback);
            }

        }

        @Override
        public void onFailure(Call<RspModel<AccountRspModel>> call, Throwable t) {
            //网络请求失败
            if (userCallback != null)
                userCallback.onDataNotAvailable(R.string.data_network_error);
        }
    }
}
