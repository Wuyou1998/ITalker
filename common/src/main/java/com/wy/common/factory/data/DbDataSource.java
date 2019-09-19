package com.wy.common.factory.data;

import java.util.List;

/* 名称: ITalker.com.wy.common.factory.data.DbDataSource
 * 用户: _VIEW
 * 时间: 2019/9/19,14:16
 * 描述: 基础的数据库源接口定义
 */
public interface DbDataSource<Data> extends DataSource {
    /**
     *有一个基本的数据源加载方法
     * @param callback 传递一个callback回调，一般回调到Presenter
     */
    void load(SuccessCallback<List<Data>> callback);
}
