package com.wy.factory.net;

import com.wy.factory.model.api.RspModel;
import com.wy.factory.model.api.account.AccountRspModel;
import com.wy.factory.model.api.account.LoginModel;
import com.wy.factory.model.api.account.RegisterModel;
import com.wy.factory.model.api.user.UserUpdateModel;
import com.wy.factory.model.card.UserCard;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/* 名称: ITalker.com.wy.factory.net.RemoteService
 * 用户: _VIEW
 * 时间: 2019/9/9,22:24
 * 描述: 网络请求的所有接口
 */
public interface RemoteService {
    /**
     * 注册接口
     *
     * @param model RegisterModel
     * @return spModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

    /**
     * 登录接口
     *
     * @param model LoginModel
     * @return RspModel<AccountRspModel>
     */
    @POST("account/login")
    Call<RspModel<AccountRspModel>> accountLogin(@Body LoginModel model);

    /**
     * 绑定设备id
     *
     * @param pushId 设备id
     * @return RspModel<AccountRspModel>
     */
    @POST("account/bind/{pushId}")
    Call<RspModel<AccountRspModel>> accountBind(@Path(encoded = true, value = "pushId") String pushId);

    //用户信息更新的接口
    @PUT("user")
    Call<RspModel<UserCard>> userUpdate(@Body UserUpdateModel model);

    //用户搜索
    @GET("user/search/{name}")
    Call<RspModel<List<UserCard>>> userSearch(@Path("name") String name);
}
