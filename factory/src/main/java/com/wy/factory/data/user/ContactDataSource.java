package com.wy.factory.data.user;

import com.wy.common.factory.data.DataSource;
import com.wy.factory.model.db.User;

import java.util.List;

/* 名称: ITalker.com.wy.factory.data.user.ContactDataSource
 * 用户: _VIEW
 * 时间: 2019/9/18,17:07
 * 描述: 联系人数据集合
 */
public interface ContactDataSource {
    /**
     * 对数据进行加载的一个职责
     *
     * @param callback 加载成功后返回的callback
     */
    void load(DataSource.SuccessCallback<List<User>> callback);

    /**
     * 销毁操作
     */
    void dispose();
}
