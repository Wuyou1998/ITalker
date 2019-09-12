package com.wy.factory.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wy.common.app.Application;
import com.wy.factory.model.api.account.AccountRspModel;
import com.wy.factory.model.db.User;
import com.wy.factory.model.db.User_Table;

/* 名称: ITalker.com.wy.factory.persistence.Account
 * 用户: _VIEW
 * 时间: 2019/9/11,11:12
 * 描述: 推送SDK的相关配置
 */
public class Account {

    private static final String KEY_PUSH_ID = "KEY_PUSH_ID";
    private static final String KEY_PUSH_BIND = "KEY_PUSH_BIND";
    private static final String KEY_TOKEN = "KEY_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_ACCOUNT = "KEY_ACCOUNT";
    //设备id
    private static String pushId;

    //绑定状态,设备id是否绑定到了服务器
    private static boolean isBind;
    //登录状态Token，用来获接口请求
    private static String token;
    //登录的用户ID
    private static String userId;
    //登录的账户
    private static String account;

    /**
     * 获取设备id
     *
     * @return 设备id
     */
    public static String getPushId() {
        return pushId;
    }

    /**
     * 设置并存储设备id
     *
     * @param pushId 设备id
     */
    public static void setPushId(String pushId) {
        Account.pushId = pushId;
        save(Application.getInstance());
    }

    /**
     * 是否已经绑定到了服务器
     *
     * @return true是
     */

    public static boolean isBind() {
        return isBind;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Account.token = token;
        save(Application.getInstance());
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(String userId) {
        Account.userId = userId;
        save(Application.getInstance());
    }

    public static String getAccount() {
        return account;
    }

    public static void setAccount(String account) {
        Account.account = account;
        save(Application.getInstance());
    }

    public static void setIsBind(boolean isBind) {
        Account.isBind = isBind;
        save(Application.getInstance());
    }

    /**
     * 返回当前账户是否登录
     *
     * @return true已登录
     */
    public static boolean isLogin() {
        return !TextUtils.isEmpty(userId) && !TextUtils.isEmpty(token);
    }

    /**
     * 是否已经完善了用户信息
     *
     * @return true 完善了
     */
    public static boolean isComplete() {
        //首先保证登录成功
        if (isLogin()) {
            User self = getUser();
            return !TextUtils.isEmpty(self.getDescription()) && !TextUtils.isEmpty(self.getAvatar()) && self.getSex() != 0;
        }
        return false;
    }

    /**
     * SharedPreferences 做数据持久化
     *
     * @param context Context
     */

    private static void save(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        preferences.edit()
                .putString(KEY_PUSH_ID, pushId)
                .putBoolean(KEY_PUSH_BIND, isBind)
                .putString(KEY_TOKEN, token)
                .putString(KEY_USER_ID, userId)
                .putString(KEY_ACCOUNT, account)
                .apply();
    }

    /**
     * 进行数据加载
     */
    public static void load(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Account.class.getName(), Context.MODE_PRIVATE);
        pushId = preferences.getString(KEY_PUSH_ID, "");
        isBind = preferences.getBoolean(KEY_PUSH_BIND, false);
        token = preferences.getString(KEY_TOKEN, "");
        userId = preferences.getString(KEY_USER_ID, "");
        account = preferences.getString(KEY_ACCOUNT, "");
    }

    /**
     * 保存自己的数据到xml中
     *
     * @param model model
     */
    public static void login(AccountRspModel model) {
        //存储当前登录的token 用户id，方便从数据库中查询我的信息
        Account.token = model.getToken();
        Account.account = model.getAccount();
        Account.userId = model.getUser().getId();
        save(Application.getInstance());
    }

    /**
     * 获取当前登录的用户信息，如果为null 就new一个user 其次从数据库查询
     *
     * @return user
     */
    public static User getUser() {
        return TextUtils.isEmpty(userId) ? new User() :
                SQLite.select().from(User.class).where(User_Table.id.eq(userId)).querySingle();
    }

}
