package com.wy.factory.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.raizlabs.android.dbflow.structure.ModelAdapter;

/* 名称: ITalker.com.wy.factory.utils.DBFlowExclusionStrategy
 * 用户: _VIEW
 * 时间: 2019/9/11,17:33
 * 描述: DBFlow 的数据库过滤字段 Gson
 */
public class DBFlowExclusionStrategy implements ExclusionStrategy {
    @Override
    //被跳过的的字段
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getDeclaringClass().equals(ModelAdapter.class);
    }

    @Override
    //被跳过的class
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
