package com.wy.factory.data.group;

import android.text.TextUtils;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wy.factory.data.BaseDbRepository;
import com.wy.factory.data.helper.GroupHelper;
import com.wy.factory.model.db.Group;
import com.wy.factory.model.db.Group_Table;
import com.wy.factory.model.db.view.MemberUserModel;

import java.util.List;

/* 名称: ITalker.com.wy.factory.data.group.GroupRepository
 * 用户: _VIEW
 * 时间: 2019/9/26,20:10
 * 描述: group数据仓库
 */
public class GroupRepository extends BaseDbRepository<Group> implements GroupDataSource {

    @Override
    protected boolean isRequired(Group group) {
        /**
         * 一个群的信息在数据库中只有两种情况：你加入了群  或者  你创建了群
         * 无论那种情况，拿到的都是群的信息，没有成员信息
         * 需要进行成员信息初始化
         */
        if (group.getGroupMemberCount() > 0) {
            //已经有了车成员信息
            group.holder = buildGroupHolder(group);
        } else {
            //待初始化
            group.holder = null;
            GroupHelper.refreshGroupMember(group);
        }
        //所有加入的群都是要显示的
        return true;
    }

    //初始化界面显示的成员信息
    private String buildGroupHolder(Group group) {
        List<MemberUserModel> userModels = group.getLatelyGroupMembers();
        if (userModels == null || userModels.size() == 0) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        for (MemberUserModel userModel : userModels) {
            builder.append(TextUtils.isEmpty(userModel.alias) ? userModel.name : userModel.alias).append(",");
        }
        builder.delete(builder.lastIndexOf(","), builder.length());
        return builder.toString();
    }

    @Override
    public void load(SuccessCallback<List<Group>> callback) {
        super.load(callback);
        SQLite.select()
                .from(Group.class)
                .orderBy(Group_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }
}
