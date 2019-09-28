package com.wy.factory.presenter.message;

import android.text.TextUtils;

import androidx.recyclerview.widget.DiffUtil;

import com.wy.factory.data.message.MessageDataSource;
import com.wy.factory.data.helper.MessageHelper;
import com.wy.factory.model.api.message.MsgCreateModel;
import com.wy.factory.model.db.Message;
import com.wy.factory.persistence.Account;
import com.wy.factory.presenter.BaseSourcePresenter;
import com.wy.factory.utils.DiffUiDataCallback;

import java.util.List;

/* 名称: ITalker.com.wy.factory.presenter.message.ChatPresenter
 * 用户: _VIEW
 * 时间: 2019/9/22,23:30
 * 描述: ChatPresenter 聊天基类
 */
public class ChatPresenter<View extends ChatContact.View>
        extends BaseSourcePresenter<Message, Message, View, MessageDataSource>
        implements ChatContact.Presenter {
    //接收者Id，可能是群，或者人ID
    protected String receiverId;
    //区分是人还是群的Id
    protected int receiverType;

    public ChatPresenter(MessageDataSource source, View mView, String receiverId, int receiverType) {
        super(source, mView);
        this.receiverId = receiverId;
        this.receiverType = receiverType;
    }

    @Override
    public void pushText(String content) {
        //构建一个新消息
        MsgCreateModel msgCreateModel = new MsgCreateModel.Builder()
                .receiver(receiverId, receiverType)
                .content(content, Message.TYPE_STR)
                .build();
        //进行网络发送
        MessageHelper.push(msgCreateModel);
    }

    @Override
    public void pushAudio(String path, long time) {
        //发送语音
        if (TextUtils.isEmpty(path)) {
            return;
        }

        // 构建一个新的消息
        MsgCreateModel model = new MsgCreateModel.Builder()
                .receiver(receiverId, receiverType)
                .content(path, Message.TYPE_AUDIO)
                .attach(String.valueOf(time))
                .build();

        // 进行网络发送
        MessageHelper.push(model);
    }

    @Override
    public void pushImages(String[] paths) {
        //发送图片
        if (paths == null || paths.length == 0)
            return;
        // 此时路径是本地的手机上的路径
        for (String path : paths) {
            // 构建一个新的消息
            MsgCreateModel model = new MsgCreateModel.Builder()
                    .receiver(receiverId, receiverType)
                    .content(path, Message.TYPE_PIC)
                    .build();

            // 进行网络发送
            MessageHelper.push(model);
        }
    }

    @Override
    public boolean rePush(Message message) {
        // 确定消息是可重复发送的
        if (Account.getUserId().equalsIgnoreCase(message.getSender().getId())
                && message.getStatus() == Message.STATUS_FAILED) {

            // 更改状态
            message.setStatus(Message.STATUS_CREATED);
            // 构建发送Model
            MsgCreateModel model = MsgCreateModel.buildWithMessage(message);
            MessageHelper.push(model);
            return true;
        }

        return false;
    }

    @Override
    public void onDataLoad(List<Message> messages) {
        ChatContact.View view = getView();
        if (view == null)
            return;
        //拿到老数据
        @SuppressWarnings("unchecked")
        List<Message> old = view.getRecyclerAdapter().getItems();
        //差异计算
        DiffUiDataCallback<Message> callback = new DiffUiDataCallback<>(old, messages);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        //进行界面刷新
        refreshData(result, messages);

    }
}
