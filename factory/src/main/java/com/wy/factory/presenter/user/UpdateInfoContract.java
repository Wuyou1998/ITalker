package com.wy.factory.presenter.user;

import com.wy.common.factory.presenter.BaseContract;

/* 名称: ITalker.com.wy.factory.presenter.user.UpdateInfoContract
 * 用户: _VIEW
 * 时间: 2019/9/12,14:29
 * 描述:更新用户信息的基本契约
 */
public interface UpdateInfoContract {
     interface Presenter extends BaseContract.Presenter {
        //更新
        void update(String photoFilePath, String desc, boolean isMan);
    }

    interface View extends BaseContract.View<Presenter> {
        //回调成功
        void updateSucceed();
    }
}
