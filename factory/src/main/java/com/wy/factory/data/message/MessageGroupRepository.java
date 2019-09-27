package com.wy.factory.data.message;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.wy.factory.data.BaseDbRepository;
import com.wy.factory.model.db.Message;
import com.wy.factory.model.db.Message_Table;

import java.util.Collections;
import java.util.List;

/* 名称: ITalker.com.wy.factory.data.message.MessageGroupRepository
 * 用户: _VIEW
 * 时间: 2019/9/27,12:56
 * 描述: 跟群聊天的时候发送的聊天列表
 *      关注的内容是我发送到群或者是别人发送到群
 */
public class MessageGroupRepository extends BaseDbRepository<Message> implements MessageDataSource {
    //聊天的群id
    private String receiverId;

    public MessageGroupRepository(String receiverId) {
        super();
        this.receiverId = receiverId;
    }

    @Override
    public void load(SuccessCallback<List<Message>> callback) {
        super.load(callback);
        //无论是我直接发还是别人发的，只要发到这个群，这个group_id就是receiverId
        SQLite.select()
                .from(Message.class)
                .where(Message_Table.group_id.eq(receiverId))
                .orderBy(Message_Table.createAt, false)
                .limit(30)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    protected boolean isRequired(Message message) {
        //如果消息的Group不为空，则一定是发送到一个群的
        //如果群id等于我们需要的id，那么就通过
        return message.getGroup() != null && receiverId.equalsIgnoreCase(message.getGroup().getId());
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Message> tResult) {
        //将数据倒序
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
