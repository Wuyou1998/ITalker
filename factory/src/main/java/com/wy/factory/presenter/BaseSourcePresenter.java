package com.wy.factory.presenter;

import com.wy.common.factory.data.DataSource;
import com.wy.common.factory.data.DbDataSource;
import com.wy.common.factory.presenter.BaseContract;
import com.wy.common.factory.presenter.BaseRecyclerPresenter;

import java.util.List;

/* 名称: ITalker.com.wy.factory.presenter.BaseSourcePresenter
 * 用户: _VIEW
 * 时间: 2019/9/19,14:54
 * 描述: 基础的仓库源的Presenter
 */
public abstract class BaseSourcePresenter<Data, ViewModel, View extends BaseContract.RecyclerView,
        Source extends DbDataSource<Data>>
        extends BaseRecyclerPresenter<View, ViewModel> implements DataSource.SuccessCallback<List<Data>> {
    protected Source source;

    public BaseSourcePresenter(Source source, View mView) {
        super(mView);
        this.source = source;
    }

    @Override
    public void start() {
        super.start();
        if (source != null)
            source.load(this);
    }

    @Override
    public void destroy() {
        super.destroy();
        source.dispose();
        source = null;
    }
}
