package com.wy.common.factory.presenter;

import androidx.annotation.StringRes;

import com.wy.common.widget.recycler.RecyclerAdapter;

/* 名称: ITalker.com.wy.common.factory.presenter.BaseContract
 * 用户: _VIEW
 * 时间: 2019/9/9,15:29
 * 描述: MVP公共基本契约
 */
public interface BaseContract {
    //基本界面职责
    interface View<T extends Presenter> {
        //显示字符串错误
        void showError(@StringRes int str);

        //显示进度条
        void showLoading();

        //支持设置一个presenter
        void setPresenter(T presenter);
    }

    //基本Presenter职责
    interface Presenter {

        //公用的开始方法
        void start();

        //公用的销毁方法
        void destroy();
    }

    //基本的列表职责
    interface RecyclerView<ViewModel, T extends Presenter> extends View<T> {
        //界面端只能刷新整个数据集合，不能精确到每一条数据刷新
        //void onDone(List<User> users);
        //拿到一个适配器，然后自己进行自主刷新
        RecyclerAdapter<ViewModel> getRecyclerAdapter();

        //当适配器数据更改了的时候触发
        void onAdapterDataChanged();
    }
}
