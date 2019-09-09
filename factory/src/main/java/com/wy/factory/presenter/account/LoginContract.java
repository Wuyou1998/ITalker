package com.wy.factory.presenter.account;

import androidx.annotation.StringRes;

import com.wy.common.factory.presenter.BaseContract;

/* 名称: ITalker.com.wy.factory.presenter.account.LoginContract
 * 用户: _VIEW
 * 时间: 2019/9/9,15:24
 * 描述: 注册部分逻辑
 */
public interface LoginContract {
    interface View extends BaseContract.View<Presenter>{
        //登录成功
        void loginSuccess();
    }

    interface Presenter extends BaseContract.Presenter {
        //发起一个登录
        void login(String phone, String name, String password);

        //检查手机号是否正确
        boolean checkMobile(String phone);
    }
}
