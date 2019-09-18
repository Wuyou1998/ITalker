package com.wy.common.factory.presenter;

import androidx.recyclerview.widget.DiffUtil;

import com.wy.common.widget.recycler.RecyclerAdapter;

import net.qiujuer.genius.kit.handler.Run;

import java.util.List;

/* 名称: ITalker.com.wy.common.factory.presenter.BaseRecyclerPresenter
 * 用户: _VIEW
 * 时间: 2019/9/18,16:50
 * 描述: 对RecyclerView进行简单的Presenter封装
 */
public class BaseRecyclerPresenter<View extends BaseContract.RecyclerView, ViewModel> extends BasePresenter<View> {
    public BaseRecyclerPresenter(View mView) {
        super(mView);
    }

    /**
     * 刷新一堆新数据到界面中
     *
     * @param dataList 新数据
     */
    protected void refreshData(final List<ViewModel> dataList) {
        Run.onUiAsync(() -> {
            View view = getView();
            if (view == null)
                return;
            //更新数据并刷新界面
            RecyclerAdapter<ViewModel> adapter = view.getRecyclerAdapter();
            adapter.replace(dataList);
            view.onAdapterDataChanged();
        });
    }

    /**
     * 刷新界面操作，该操作可以保证执行方法在主线程进行
     *
     * @param result   差异结果集
     * @param dataList 具体的新数据
     */
    protected void refreshData(final DiffUtil.DiffResult result, final List<ViewModel> dataList) {
        Run.onUiAsync(() -> {
            //主线程运行时
            refreshDataOnUiThread(result, dataList);
        });

    }

    private void refreshDataOnUiThread(final DiffUtil.DiffResult result, final List<ViewModel> dataList) {
        View view = getView();
        if (view == null)
            return;

        //改变数据集合并不通知刷新
        RecyclerAdapter<ViewModel> adapter = view.getRecyclerAdapter();
        adapter.getItems().clear();
        adapter.getItems().addAll(dataList);
        //通知界面刷新占位布局
        view.onAdapterDataChanged();
        //进行增量更新
        result.dispatchUpdatesTo(adapter);
    }
}
