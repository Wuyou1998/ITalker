package com.wy.factory.presenter.message;

import com.wy.factory.data.helper.GroupHelper;
import com.wy.factory.data.message.MessageGroupRepository;
import com.wy.factory.model.db.Group;
import com.wy.factory.model.db.Message;
import com.wy.factory.model.db.view.MemberUserModel;
import com.wy.factory.persistence.Account;

import java.util.List;

/* 名称: ITalker.com.wy.factory.presenter.message.ChatGroupPresenter
 * 用户: _VIEW
 * 时间: 2019/9/22,23:28
 * 描述: 群聊天ChatGroupPresenter
 */
public class ChatGroupPresenter extends ChatPresenter<ChatContact.GroupView> implements ChatContact.Presenter {

    public ChatGroupPresenter(ChatContact.GroupView mView, String receiverId) {
        super(new MessageGroupRepository(receiverId), mView, receiverId, Message.RECEIVER_TYPE_GROUP);
    }

    @Override
    public void start() {
        super.start();
        //从本地拿这个群的信息
        Group group = GroupHelper.findFromLocal(receiverId);
        if (group != null) {
            //初始化操作
            ChatContact.GroupView view = getView();
            boolean isAdmin = Account.getUserId().equalsIgnoreCase(group.getOwner().getId());
            view.showAdminOption(isAdmin);
            //基础信息初始化
            view.onInit(group);
            //成员初始化
            List<MemberUserModel> models = group.getLatelyGroupMembers();
            final long memberCount = group.getGroupMemberCount();
            //没有显示的成员数量
            long moreCount = memberCount - models.size();

            view.onInitGroupMembers(models, moreCount);
        }
    }

}
