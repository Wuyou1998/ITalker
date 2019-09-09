package com.wy.factory.model.api;

import androidx.annotation.NonNull;

/* 名称: ITalker.com.wy.factory.model.api.RegisterModel
 * 用户: _VIEW
 * 时间: 2019/9/9,16:54
 * 描述: 注册使用的请求model
 */
public class RegisterModel {
    private String account;
    private String password;
    private String name;
    private String pushId;

    public RegisterModel(String account, String password, String name) {
        this(account, password, name, null);
    }

    public RegisterModel(String account, String password, String name, String pushId) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.pushId = pushId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    @NonNull
    @Override
    public String toString() {
        return "RegisterModel{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", pushId='" + pushId + '\'' +
                '}';
    }
}
