package com.wy.common.factory.presenter;

import androidx.annotation.StringRes;

/* 名称: ITalker.com.wy.common.factory.presenter.BaseContract
 * 用户: _VIEW
 * 时间: 2019/9/9,15:29
 * 描述: MVP公共基本契约
 */
public interface BaseContract {
    interface View<T extends Presenter > {
        //显示字符串错误
        void showError(@StringRes int str);

        //显示进度条
        void showLoading();

        //支持设置一个presenter
        void setPresenter(T presenter);
    }

    interface Presenter {

        //公用的开始方法
        void start();

        //公用的销毁方法
        void destroy();
    }
}
