package com.wy.factory.data.helper;

import com.wy.common.factory.data.DataSource;
import com.wy.factory.Factory;
import com.wy.factory.R;
import com.wy.factory.model.api.RspModel;
import com.wy.factory.model.api.user.UserUpdateModel;
import com.wy.factory.model.card.UserCard;
import com.wy.factory.model.db.User;
import com.wy.factory.net.Network;
import com.wy.factory.net.RemoteService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* 名称: ITalker.com.wy.factory.data.helper.UserHelper
 * 用户: _VIEW
 * 时间: 2019/9/12,18:32
 * 描述: 用户信息更新
 */
public class UserHelper {
    //更新用户信息，异步
    public static void update(UserUpdateModel model, final DataSource.Callback<UserCard> callback) {
        //调用Retrofit对网络请求接口做代理
        RemoteService service = Network.remote();
        //得到一个Call
        Call<RspModel<UserCard>> call = service.userUpdate(model);
        call.enqueue(new Callback<RspModel<UserCard>>() {
            @Override
            public void onResponse(Call<RspModel<UserCard>> call, Response<RspModel<UserCard>> response) {
                RspModel<UserCard> rspModel = response.body();
                if (rspModel.success()) {
                    UserCard userCard = rspModel.getResult();
                    //数据库的存储操作，需要把UserCard转换为User
                    User user = userCard.build();
                    user.save();
                    //返回成功
                    callback.onDataLoad(userCard);
                } else {
                    //出错后进行错误分配
                    Factory.decodeRspCode(rspModel, callback);
                }
            }

            @Override
            public void onFailure(Call<RspModel<UserCard>> call, Throwable t) {
                //网络请求失败
                callback.onDataNotAvailable(R.string.data_network_error);
            }
        });
    }
}
