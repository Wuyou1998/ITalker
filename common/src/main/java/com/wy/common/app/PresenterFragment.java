package com.wy.common.app;

import android.content.Context;

import com.wy.common.factory.presenter.BaseContract;

/* 名称: ITalker.com.wy.common.app.PresenterFragment
 * 用户: _VIEW
 * 时间: 2019/9/9,15:40
 * 描述: 基础的 presenterFragment
 */
public abstract class PresenterFragment<Presenter extends BaseContract.Presenter> extends BaseFragment
        implements BaseContract.View<Presenter> {
    protected Presenter presenter;

    //onAttach之后开始触发,初始化Presenter
    protected abstract Presenter initPresenter();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initPresenter();
    }

    @Override
    public void showError(int str) {
        //发生错误时优先使用占位布局
        if (placeHolderView != null)
            placeHolderView.triggerError(str);
        else
            Application.showToast(str);
    }

    @Override
    public void showLoading() {
        if (placeHolderView != null)
            placeHolderView.triggerLoading();
    }

    @Override
    public void setPresenter(Presenter presenter) {
        this.presenter = presenter;
    }
}
