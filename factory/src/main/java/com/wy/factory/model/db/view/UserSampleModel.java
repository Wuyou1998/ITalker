package com.wy.factory.model.db.view;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.QueryModel;
import com.wy.common.factory.model.Author;
import com.wy.factory.model.db.AppDatabase;


/**
 * 用户基础信息的Model，可以和数据库进行查询
 */
@QueryModel(database = AppDatabase.class)
public class UserSampleModel implements Author {
    @Column
    public String id;
    @Column
    public String name;
    @Column
    public String avatar;

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    @Override
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

}
