package com.wy.italker.fragments.message;


import com.wy.factory.model.db.Group;
import com.wy.factory.presenter.message.ChatContact;
import com.wy.italker.R;

/**
 * 群聊天界面
 */

public class ChatGroupFragment extends ChatFragment<Group> implements ChatContact.GroupView {

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_chat_group;
    }

    @Override
    protected ChatContact.Presenter initPresenter() {
        return null;
    }

    @Override
    public void onInit(Group group) {

    }
}
