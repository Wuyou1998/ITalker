package com.wy.factory.presenter.group;

import com.wy.common.factory.presenter.BaseRecyclerPresenter;
import com.wy.factory.Factory;
import com.wy.factory.data.helper.GroupHelper;
import com.wy.factory.model.db.view.MemberUserModel;

import java.util.List;

/* 名称: ITalker.com.wy.factory.presenter.group.GroupMembersPresenter
 * 用户: _VIEW
 * 时间: 2019/9/27,17:29
 * 描述: 群成员管理 Presenter
 */
public class GroupMembersPresenter extends BaseRecyclerPresenter<GroupMembersContract.View, MemberUserModel>
        implements GroupMembersContract.Presenter {
    public GroupMembersPresenter(GroupMembersContract.View mView) {
        super(mView);
    }

    @Override
    public void refresh() {
        //显示loading
        start();
        //异步加载
        Factory.runOnAsync(loader);
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            GroupMembersContract.View view = getView();
            if (view == null)
                return;
            String groupId = view.getGroupId();
            //limit为-1表示查询所有
            List<MemberUserModel> models = GroupHelper.getMemberUsers(groupId, -1);
            refreshData(models);
        }
    };
}
