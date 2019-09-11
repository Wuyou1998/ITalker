package com.wy.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/* 名称: ITalker.com.wy.factory.model.db.AppDatabase
 * 用户: _VIEW
 * 时间: 2019/9/11,17:45
 * 描述: 数据库基本信息
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";
    public static final int VERSION = 1;
}
