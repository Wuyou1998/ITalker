package com.wy.factory.presenter.message;

import com.wy.factory.data.helper.UserHelper;
import com.wy.factory.data.message.MessageRepository;
import com.wy.factory.model.db.Message;
import com.wy.factory.model.db.User;

/* 名称: ITalker.com.wy.factory.presenter.message.ChatUserPresenter
 * 用户: _VIEW
 * 时间: 2019/9/22,23:27
 * 描述: 人聊天Presenter
 */
public class ChatUserPresenter extends ChatPresenter<ChatContact.UserView> implements ChatContact.Presenter {

    public ChatUserPresenter(ChatContact.UserView mView, String receiverId) {
        //数据源，View，接收者，接收者类型
        super(new MessageRepository(receiverId), mView, receiverId, Message.RECEIVER_TYPE_NONE);
    }

    @Override
    public void start() {
        super.start();
        //从本地拿这个人的信息
        User receiver = UserHelper.findFromLocal(receiverId);
        getView().onInit(receiver);
    }
}
