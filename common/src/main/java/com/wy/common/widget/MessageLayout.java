package com.wy.common.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

/* 名称: ITalker.com.wy.common.widget.MessageLayout
 * 用户: _VIEW
 * 时间: 2019/9/22,14:58
 * 描述: 修改LinearLayout的fitsSystemWindows属性
 *      使其能够保持沉浸式状态栏和弹出输入框
 */
public class MessageLayout extends LinearLayout {
    public MessageLayout(Context context) {
        super(context);
    }

    public MessageLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MessageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected boolean fitSystemWindows(Rect insets) {
        insets.left = 0;
        insets.right = 0;
        insets.top = 0;
        return super.fitSystemWindows(insets);
    }
}
