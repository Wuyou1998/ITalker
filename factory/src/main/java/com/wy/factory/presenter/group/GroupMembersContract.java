package com.wy.factory.presenter.group;

import com.wy.common.factory.presenter.BaseContract;
import com.wy.factory.model.db.view.MemberUserModel;

/* 名称: ITalker.com.wy.factory.presenter.group.GroupMembersContract
 * 用户: _VIEW
 * 时间: 2019/9/27,15:52
 * 描述: 群成员契约
 */
public interface GroupMembersContract {
    interface Presenter extends BaseContract.Presenter {
        //刷新
        void refresh();
    }

    interface View extends BaseContract.RecyclerView<MemberUserModel, Presenter> {
        //获取群的id
        String getGroupId();
    }
}
