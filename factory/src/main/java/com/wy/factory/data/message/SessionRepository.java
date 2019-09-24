package com.wy.factory.data.message;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.wy.factory.data.BaseDbRepository;
import com.wy.factory.model.db.Session;
import com.wy.factory.model.db.Session_Table;

import java.util.Collections;
import java.util.List;

/* 名称: ITalker.com.wy.factory.data.message.SessionRepository
 * 用户: _VIEW
 * 时间: 2019/9/24,20:25
 * 描述: 最近聊天列表仓库，对SessionDataSource的实现
 */
public class SessionRepository extends BaseDbRepository<Session> implements SessionDataSource {
    @Override
    public void load(SuccessCallback<List<Session>> callback) {
        super.load(callback);
        //数据库查询
        SQLite.select().from(Session.class)
                .orderBy(Session_Table.modifyAt, false)//倒序
                .limit(100)
                .async()
                .queryListResultCallback(this)
                .execute();
    }

    @Override
    public void dispose() {

    }

    @Override
    protected boolean isRequired(Session session) {
        //所有的会话都是需要的，所以不需要过滤
        return true;
    }

    @Override
    protected void insert(Session session) {
        //复写方法，让新的数据加到头部
        datas.addFirst(session);
    }

    @Override
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Session> tResult) {
        //复写数据库回来的方法，进行一次反转
        Collections.reverse(tResult);
        super.onListQueryResult(transaction, tResult);
    }
}
