package com.wy.factory.presenter.contact;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.wy.common.factory.data.DataSource;
import com.wy.common.factory.presenter.BasePresenter;
import com.wy.factory.data.helper.UserHelper;
import com.wy.factory.model.card.UserCard;
import com.wy.factory.model.db.AppDatabase;
import com.wy.factory.model.db.User;
import com.wy.factory.model.db.User_Table;
import com.wy.factory.persistence.Account;
import com.wy.factory.utils.DiffUiDataCallback;

import java.util.ArrayList;
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
        //加载本地数据库数据
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
        //加载网络数据
        UserHelper.refreshContacts(new DataSource.Callback<List<UserCard>>() {
            @Override
            public void onDataNotAvailable(int strRes) {
                //网络刷新失败，但是本地有数据，忽略错误
            }

            @Override
            public void onDataLoad(final List<UserCard> userCards) {
                final List<User> users = new ArrayList<>();
                for (UserCard userCard : userCards) {
                    users.add(userCard.build());
                }
                //数据放到事务中，保存数据库
                DatabaseDefinition databaseDefinition = FlowManager.getDatabase(AppDatabase.class);
                databaseDefinition.beginTransactionAsync(new ITransaction() {
                    @Override
                    public void execute(DatabaseWrapper databaseWrapper) {
                        FlowManager.getModelAdapter(User.class).saveAll(users);
                    }
                }).execute();
                List<User> old = getView().getRecyclerAdapter().getItems();
                diff(users, old);
            }
        });

        //TODO 关注后没有刷新联系人，从网络刷新或者从数据库刷新刷新的是全局，本地和网络在显示时有可能冲突


    }

    private void diff(List<User> newList, List<User> oldList) {
        //进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<User>(oldList, newList);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        //在对比完成后进行数据的赋值
        getView().getRecyclerAdapter().replace(newList);
        //尝试刷新界面
        result.dispatchUpdatesTo(getView().getRecyclerAdapter());
        getView().onAdapterDataChanged();
    }

    @Override
    public void destroy() {

    }
}
