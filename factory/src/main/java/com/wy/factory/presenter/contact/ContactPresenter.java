package com.wy.factory.presenter.contact;

import androidx.recyclerview.widget.DiffUtil;

import com.wy.common.factory.data.DataSource;
import com.wy.common.widget.recycler.RecyclerAdapter;
import com.wy.factory.data.helper.UserHelper;
import com.wy.factory.data.user.ContactDataSource;
import com.wy.factory.data.user.ContactRepository;
import com.wy.factory.model.db.User;
import com.wy.factory.presenter.BaseSourcePresenter;
import com.wy.factory.utils.DiffUiDataCallback;

import java.util.List;

/* 名称: ITalker.com.wy.factory.presenter.contact.ContactPresenter
 * 用户: _VIEW
 * 时间: 2019/9/15,14:49
 * 描述: ContactPresenter
 */
public class ContactPresenter extends BaseSourcePresenter<User, User, ContactContract.View, ContactDataSource>
        implements ContactContract.Presenter, DataSource.SuccessCallback<List<User>> {

    public ContactPresenter(ContactContract.View mView) {
        //初始化数据仓库
        super(new ContactRepository(), mView);
    }

    @Override
    public void start() {
        super.start();
        //加载网络数据
        UserHelper.refreshContacts();
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
