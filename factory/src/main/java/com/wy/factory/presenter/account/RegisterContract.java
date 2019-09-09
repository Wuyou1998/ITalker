package com.wy.factory.presenter.account;

import com.wy.common.factory.presenter.BaseContract;

/* 名称: ITalker.com.wy.factory.presenter.account.RegisterContract
 * 用户: _VIEW
 * 时间: 2019/9/9,14:58
 * 描述: 注册部分逻辑
 */
public interface RegisterContract {
    interface View extends BaseContract.View<Presenter> {
        //注册成功
        void registerSuccess();

    }

    interface Presenter extends BaseContract.Presenter {
        //发起一个注册
        void register(String phone, String name, String password);

        //检查手机号是否正确
        boolean checkMobile(String phone);

    }
}
