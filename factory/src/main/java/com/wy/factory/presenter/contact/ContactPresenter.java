package com.wy.factory.presenter.contact;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.wy.common.factory.presenter.BasePresenter;
import com.wy.factory.model.db.User;
import com.wy.factory.model.db.User_Table;
import com.wy.factory.persistence.Account;

import java.util.List;

/* 名称: ITalker.com.wy.factory.presenter.contact.ContactPresenter
 * 用户: _VIEW
 * 时间: 2019/9/15,14:49
 * 描述: ContactPresenter
 */
public class ContactPresenter extends BasePresenter<ContactContract.View>
        implements ContactContract.Presenter {
    public ContactPresenter(ContactContract.View mView) {
        super(mView);
    }

    @Override
    public void start() {
        //TODO 加载数据
        SQLite.select().from(User.class)
                .where(User_Table.isFollow.eq(true))
                .and(User_Table.id.notEq(Account.getUserId()))
                .orderBy(User_Table.name, true)
                .limit(100)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<User>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @NonNull List<User> tResult) {
                        getView().getRecyclerAdapter().replace(tResult);
                        getView().onAdapterDataChanged();
                    }
                }).execute();

    }

    @Override
    public void destroy() {

    }
}
