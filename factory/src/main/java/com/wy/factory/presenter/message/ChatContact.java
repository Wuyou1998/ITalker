package com.wy.factory.presenter.message;

import com.wy.common.factory.presenter.BaseContract;
import com.wy.factory.model.db.Group;
import com.wy.factory.model.db.Message;
import com.wy.factory.model.db.User;
import com.wy.factory.model.db.view.MemberUserModel;

import java.util.List;

/* 名称: ITalker.com.wy.factory.presenter.message.ChatContact
 * 用户: _VIEW
 * 时间: 2019/9/22,23:04
 * 描述: 聊天接口
 */
public interface ChatContact {
    interface Presenter extends BaseContract.Presenter {
        //发送文字
        void pushText(String content);

        //发送语音
        void pushAudio(String path);

        //发送图片
        void pushImages(String[] paths);

        //重新发送一个消息，返回是否调度成功
        boolean rePush(Message message);
    }

    //聊天基类
    interface View<InitModel> extends BaseContract.RecyclerView<Message, Presenter> {
        //初始化Model
        void onInit(InitModel model);
    }

    //人聊天的界面
    interface UserView extends View<User> {

    }

    //群聊天的界面
    interface GroupView extends View<Group> {
        //显示管理员菜单
        void showAdminOption(boolean isAdmin);

        //初始化成员信息
        void onInitGroupMembers(List<MemberUserModel> members, long moreCount);
    }
}
