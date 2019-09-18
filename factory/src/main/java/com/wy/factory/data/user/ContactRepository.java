package com.wy.factory.data.user;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.wy.common.factory.data.DataSource;
import com.wy.factory.data.helper.DbHelper;
import com.wy.factory.model.db.User;
import com.wy.factory.model.db.User_Table;
import com.wy.factory.persistence.Account;

import java.util.LinkedList;
import java.util.List;

/* 名称: ITalker.com.wy.factory.data.user.ContactRepository
 * 用户: _VIEW
 * 时间: 2019/9/18,17:06
 * 描述: 联系人仓库
 */
public class ContactRepository implements ContactDataSource,
        QueryTransaction.QueryResultListCallback<User>, DbHelper.ChangedListener<User> {
    private DataSource.SuccessCallback<List<User>> callback;
    private final List<User> users = new LinkedList<>();

    @Override
    public void load(DataSource.SuccessCallback<List<User>> callback) {
        this.callback = callback;
        //对数据辅助工具类添加一个数据更新的监听
        DbHelper.addChangedListener(User.class, this);
        //加载本地数据库数据
        SQLite.select().from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(this).execute();
    }

    @Override
    public void dispose() {
        this.callback = null;
        //取消对数据集合的监听
        DbHelper.removeChangedListener(User.class, this);
    }

    @Override
    //数据库加载数据成功
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
        //添加到自己当前的缓冲区
        if (tResult.size() == 0) {
            users.clear();
            notifyDataChange();
            return;
        }
        //转变为数组
        User[] users = tResult.toArray(new User[0]);
        //回到数据集更新的操作中
        onDataSave(users);
    }


    @Override
    //当数据库数据变更
    public void onDataSave(User... list) {
        boolean isChanged = false;
        for (User user : list) {
            //是关注的人，同时这个人不是我自己
            if (isRequired(user)) {
                insertOrUpdate(user);
                isChanged = true;
            }
        }
        //有数据变更则进行界面刷新
        if (isChanged) {
            notifyDataChange();
        }
    }

    @Override
    //当数据库数据删除
    public void onDataDelete(User... list) {
        boolean isChanged = false;
        for (User user : list) {
            if (users.remove(user)) {
                isChanged = true;
            }
        }
        //有数据变更则进行界面刷新
        if (isChanged)
            notifyDataChange();
    }

    private void insertOrUpdate(User user) {
        int index = indexOf(user);
        if (index >= 0) {
            replace(index, user);
        } else {
            insert(user);
        }
    }

    //添加
    private void insert(User user) {
        users.add(user);
    }

    private void replace(int index, User user) {
        users.remove(index);
        users.add(index, user);
    }

    private int indexOf(User user) {
        int index = -1;
        for (User user1 : users) {
            index++;
            if (user1.isSame(user)) {
                return index;
            }
        }
        return -1;
    }

    private void notifyDataChange() {
        if (callback != null) {
            callback.onDataLoad(users);
        }
    }

    /**
     * 检查一个user是否是我需要关注的数据
     *
     * @param user User
     * @return true 是我关注的数据
     */
    private boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equalsIgnoreCase(Account.getUserId());
    }
}
