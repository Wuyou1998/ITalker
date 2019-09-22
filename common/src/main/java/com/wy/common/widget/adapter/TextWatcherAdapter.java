package com.wy.common.widget.adapter;

import android.text.Editable;
import android.text.TextWatcher;

/* 名称: ITalker.com.wy.common.widget.adapter.TextWatcherAdapter
 * 用户: _VIEW
 * 时间: 2019/9/22,15:33
 * 描述: TextWatcher 实现类
 */
public abstract class TextWatcherAdapter implements TextWatcher {
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
