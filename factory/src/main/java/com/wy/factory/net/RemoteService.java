package com.wy.factory.net;

import com.wy.factory.model.api.RspModel;
import com.wy.factory.model.api.account.AccountRspModel;
import com.wy.factory.model.api.account.LoginModel;
import com.wy.factory.model.api.account.RegisterModel;
import com.wy.factory.model.api.group.GroupCreateModel;
import com.wy.factory.model.api.group.GroupMemberAddModel;
import com.wy.factory.model.api.message.MsgCreateModel;
import com.wy.factory.model.api.user.UserUpdateModel;
import com.wy.factory.model.card.GroupCard;
import com.wy.factory.model.card.GroupMemberCard;
import com.wy.factory.model.card.MessageCard;
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

    //用户关注
    @PUT("user/follow/{userId}")
    Call<RspModel<UserCard>> userFollow(@Path("userId") String name);

    //用户搜索
    @GET("user/contact")
    Call<RspModel<List<UserCard>>> userContacts();

    // 查询某人的信息
    @GET("user/{userId}")
    Call<RspModel<UserCard>> userFind(@Path("userId") String userId);

    // 创建群
    @POST("group")
    Call<RspModel<GroupCard>> groupCreate(@Body GroupCreateModel model);

    // 拉取群信息
    @GET("group/{groupId}")
    Call<RspModel<GroupCard>> groupFind(@Path("groupId") String groupId);

    // 我的群列表
    @GET("group/list/{date}")
    Call<RspModel<List<GroupCard>>> groups(@Path(value = "date", encoded = true) String date);

    // 群搜索的接口
    @GET("group/search/{name}")
    Call<RspModel<List<GroupCard>>> groupSearch(@Path(value = "name", encoded = true) String name);

    // 我的群的成员列表
    @GET("group/{groupId}/members")
    Call<RspModel<List<GroupMemberCard>>> groupMembers(@Path("groupId") String groupId);

    // 发送消息的接口
    @POST("msg")
    Call<RspModel<MessageCard>> msgPush(@Body MsgCreateModel model);

    // 给群添加成员
    @POST("group/{groupId}/members")
    Call<RspModel<List<GroupMemberCard>>> groupMemberAdd(@Path("groupId") String groupId,
                                                         @Body GroupMemberAddModel model);
}
