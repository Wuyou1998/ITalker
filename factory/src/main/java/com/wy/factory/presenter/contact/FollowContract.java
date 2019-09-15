package com.wy.factory.presenter.contact;

import com.wy.common.factory.presenter.BaseContract;
import com.wy.factory.model.card.UserCard;

/* 名称: ITalker.com.wy.factory.presenter.contact.FollowContract
 * 用户: _VIEW
 * 时间: 2019/9/14,23:54
 * 描述: 关注
 */
public interface FollowContract {
    // 任务调度者
    interface Presenter extends BaseContract.Presenter {
        // 关注一个人
        void follow(String id);
    }

    interface View extends BaseContract.View<Presenter> {
        // 成功的情况下返回一个用户的信息
        void onFollowSucceed(UserCard userCard);
    }
}
