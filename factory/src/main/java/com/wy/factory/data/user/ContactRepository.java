package com.wy.factory.data.user;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.wy.common.factory.data.DataSource;
import com.wy.factory.data.BaseDbRepository;
import com.wy.factory.data.helper.DbHelper;
import com.wy.factory.model.db.User;
import com.wy.factory.model.db.User_Table;
import com.wy.factory.persistence.Account;

import java.util.List;

/* 名称: ITalker.com.wy.factory.data.user.ContactRepository
 * 用户: _VIEW
 * 时间: 2019/9/18,17:06
 * 描述: 联系人仓库
 */
public class ContactRepository extends BaseDbRepository<User> implements ContactDataSource {

    @Override
    public void load(DataSource.SuccessCallback<List<User>> callback) {
        super.load(callback);
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

    /**
     * 检查一个user是否是我需要关注的数据
     *
     * @param user User
     * @return true 是我关注的数据
     */
    protected boolean isRequired(User user) {
        return user.isFollow() && !user.getId().equalsIgnoreCase(Account.getUserId());
    }
}
