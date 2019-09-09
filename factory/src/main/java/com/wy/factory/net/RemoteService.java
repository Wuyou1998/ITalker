package com.wy.factory.net;

import com.wy.factory.model.api.RspModel;
import com.wy.factory.model.api.account.AccountRspModel;
import com.wy.factory.model.api.account.RegisterModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/* 名称: ITalker.com.wy.factory.net.RemoteService
 * 用户: _VIEW
 * 时间: 2019/9/9,22:24
 * 描述: 网络请求的所有接口
 */
public interface RemoteService {
    /**
     * 网络请求一个注册接口
     *
     * @param model RegisterModel
     * @return spModel<AccountRspModel>
     */
    @POST("account/register")
    Call<RspModel<AccountRspModel>> accountRegister(@Body RegisterModel model);

}
