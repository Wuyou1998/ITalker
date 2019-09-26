package com.wy.factory.presenter.group;

import androidx.recyclerview.widget.DiffUtil;

import com.wy.factory.data.group.GroupDataSource;
import com.wy.factory.data.group.GroupRepository;
import com.wy.factory.data.helper.GroupHelper;
import com.wy.factory.model.db.Group;
import com.wy.factory.presenter.BaseSourcePresenter;
import com.wy.factory.utils.DiffUiDataCallback;

import java.util.List;

/* 名称: ITalker.com.wy.factory.presenter.group.GroupPresenter
 * 用户: _VIEW
 * 时间: 2019/9/26,20:08
 * 描述: 我加入的群聊 Presenter
 */
public class GroupPresenter extends BaseSourcePresenter<Group, Group, GroupContract.View, GroupDataSource>
implements GroupContract.Presenter{


    public GroupPresenter( GroupContract.View mView) {
        super(new GroupRepository(), mView);
    }

    @Override
    public void start() {
        super.start();
        //加载网络数据,可以优化到下拉刷新中，只有用户下拉才进行网络请求
        GroupHelper.refreshGroups();
    }

    @Override
    public void onDataLoad(List<Group> groups) {
        GroupContract.View view = getView();
        if (view == null)
            return;
        //对比差异
        List<Group> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Group> callback = new DiffUiDataCallback<>(old, groups);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        //界面刷新
        refreshData(result, groups);
    }
}
