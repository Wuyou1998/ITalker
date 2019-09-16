package com.wy.factory.utils;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

/* 名称: ITalker.com.wy.factory.utils.DiffUiDataCallback
 * 用户: _VIEW
 * 时间: 2019/9/16,14:23
 * 描述: DiffUtil的callback 用于比较两个集合不同的回调
 */
public class DiffUiDataCallback<T extends DiffUiDataCallback.UiDataDiffer<T>> extends DiffUtil.Callback {
    private List<T> oldList, newList;

    public DiffUiDataCallback(List<T> oldList, List<T> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    //旧的数据大小
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    //新的数据大小
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    //两个item是否hi相同的，比如id相等的user
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        T oldItem = oldList.get(oldItemPosition);
        T newItem = newList.get(newItemPosition);
        return newItem.isSame(oldItem);
    }

    @Override
    //在经过相等判断后，进一步判断是否有数据更改
    //比如一个用户有两个实例，其中的name字段不同
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        T oldItem = oldList.get(oldItemPosition);
        T newItem = newList.get(newItemPosition);
        return newItem.isUiContentSame(oldItem);
    }

    //进行比较的数据类型
    public interface UiDataDiffer<T> {
        //传递一个旧数据，返回是否和这个旧数据相等
        boolean isSame(T old);

        //和旧数据相比，内容是否相同
        boolean isUiContentSame(T old);
    }
}
