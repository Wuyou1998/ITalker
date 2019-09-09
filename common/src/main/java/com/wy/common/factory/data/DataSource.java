package com.wy.common.factory.data;

import androidx.annotation.StringRes;

/* 名称: ITalker.com.wy.common.factory.data.DataSource
 * 用户: _VIEW
 * 时间: 2019/9/9,17:14
 * 描述: 数据源接口定义
 */
public interface DataSource {
    /**
     * 同时包括了失败成功两种回调接口
     *
     * @param <T> 泛型
     */
    interface Callback<T> extends SuccessCallback<T>, FailedCallback {

    }

    /**
     * 只关注成功的接口
     *
     * @param <T> 泛型
     */
    interface SuccessCallback<T> {
        //数据加载成功，网络请求成功
        void onDataLoad(T t);
    }

    /**
     * 只关注成功的接口
     */
    interface FailedCallback {
        //数据加载失败，网络请求失败
        void onDataNotAvailable(@StringRes int strRes);
    }
}
