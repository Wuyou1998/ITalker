package com.wy.factory.data.helper;

import com.wy.common.factory.data.DataSource;
import com.wy.factory.Factory;
import com.wy.factory.R;
import com.wy.factory.model.api.RspModel;
import com.wy.factory.model.api.account.AccountRspModel;
import com.wy.factory.model.api.account.RegisterModel;
import com.wy.factory.model.db.User;
import com.wy.factory.net.Network;
import com.wy.factory.net.RemoteService;

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
        RemoteService service = Network.getRetrofit().create(RemoteService.class);
        //得到一个Call
        Call<RspModel<AccountRspModel>> call = service.accountRegister(model);
        call.enqueue(new Callback<RspModel<AccountRspModel>>() {
            @Override
            public void onResponse(Call<RspModel<AccountRspModel>> call,
                                   Response<RspModel<AccountRspModel>> response) {
                //请求成功，从返回中获取全局model，内部是使用的Gson解析
                RspModel<AccountRspModel> rspModel = response.body();
                //判断绑定状态，是否绑定设备
                if (rspModel.success()) {
                    //拿到实体
                    AccountRspModel accountRspModel = rspModel.getResult();
                    if (accountRspModel.isBind()) {
                        User user = accountRspModel.getUser();
                        //TODO数据库写入和缓存绑定，然后返回
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
                userCallback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }

    /**
     * 对设备Id进行绑定的操作
     *
     * @param callback DataSource.Callback
     */
    public static void bindPush(DataSource.Callback<User> callback) {
        callback.onDataNotAvailable(R.string.app_name);
    }
}
