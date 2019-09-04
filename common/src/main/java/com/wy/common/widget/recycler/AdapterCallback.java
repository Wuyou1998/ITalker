package com.wy.common.widget.recycler;

/* 名称: ITalker.com.wy.common.widget.recycler.AdapterCallback
 * 用户: _VIEW
 * 时间: 2019/9/2,14:05
 * 描述: adapter 回调
 */
public interface AdapterCallback<Data> {
    void update(Data data, RecyclerAdapter.ViewHolder<Data> viewHolder);
}
