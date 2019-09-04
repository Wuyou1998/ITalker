package com.wy.common.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/* 名称: ITalker.com.wy.common.widget.SquareLayout
 * 用户: _VIEW
 * 时间: 2019/9/4,2:55
 * 描述: 正方形控件
 */
public class SquareLayout extends FrameLayout {
    public SquareLayout(@NonNull Context context) {
        super(context);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //将高度换成宽度 即为基于宽度的正方形
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
