package com.wy.factory.data.message;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.OperatorGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.wy.factory.data.BaseDbRepository;
import com.wy.factory.model.db.Message;
import com.wy.factory.model.db.Message_Table;

import java.util.Collections;
import java.util.List;

/* 名称: ITalker.com.wy.factory.data.message.MessageRepository
 * 用户: _VIEW
 * 时间: 2019/9/22,23:36
 * 描述: 跟某人聊天时的聊天记录列表
 *      关注的内容是我发送给这个人的，或者是这个人发送给我的
 */
public class MessageRepository extends BaseDbRepository<Message> implements MessageDataSource {
    //聊天的对象id
    private String receiverId;

    public MessageRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SuccessCallback<List<Message>> callback) {
        super.load(callback);
        SQLite.select()
                .from(Message.class)
                .where(OperatorGroup.clause()
                        .and(Message_Table.sender_id.eq(receiverId))
                        .and(Message_Table.group_id.isNull()))
                .or(Message_Table.receiver_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Message message) {
        // receiverId 如果是发送者，那么Group==null情况下一定是发送给我的消息
        // 如果消息的接收者不为空，那么一定是发送给某个人的，这个人只能是我或者是某个人
        // 如果这个"某个人"就是receiverId，那么就是我需要关注的信息
        return (receiverId.equalsIgnoreCase(message.getSender().getId()) && message.getGroup() == null)
                || (message.getReceiver() != null && receiverId.equalsIgnoreCase(message.getReceiver().getId()));
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        //将数据倒序
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
