package com.wy.factory.presenter.contact;

import androidx.recyclerview.widget.DiffUtil;

import com.wy.common.factory.data.DataSource;
import com.wy.common.factory.presenter.BaseRecyclerPresenter;
import com.wy.common.widget.recycler.RecyclerAdapter;
import com.wy.factory.data.helper.UserHelper;
import com.wy.factory.data.user.ContactDataSource;
import com.wy.factory.data.user.ContactRepository;
import com.wy.factory.model.db.User;
import com.wy.factory.utils.DiffUiDataCallback;

import java.util.List;

/* 名称: ITalker.com.wy.factory.presenter.contact.ContactPresenter
 * 用户: _VIEW
 * 时间: 2019/9/15,14:49
 * 描述: ContactPresenter
 */
public class ContactPresenter extends BaseRecyclerPresenter<ContactContract.View, User>
        implements ContactContract.Presenter, DataSource.SuccessCallback<List<User>> {
    private ContactDataSource source;

    public ContactPresenter(ContactContract.View mView) {
        super(mView);
        source = new ContactRepository();
    }

    @Override
    public void start() {
        super.start();
        //进行本地的数据加载，并添加监听
        source.load(this);
        //加载网络数据
        UserHelper.refreshContacts();
    }


    @Override
    public void destroy() {
        //当界面销毁时，应当把数据监听进行销毁
        source.dispose();
    }

    @Override
    //无论如何操作，数据发生变更后，都会回调到这里来
    //运行到这里是应当是子线程
    public void onDataLoad(List<User> users) {
        final ContactContract.View view = getView();
        if (view == null) {
            return;
        }
        RecyclerAdapter<User> adapter = view.getRecyclerAdapter();
        List<User> old = adapter.getItems();

        //进行数据对比
        DiffUtil.Callback callback = new DiffUiDataCallback<User>(old, users);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);

        //调用基类方法进行界面刷新
        refreshData(result, users);
    }

}
