package com.wy.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.bumptech.glide.RequestManager;
import com.wy.common.R;
import com.wy.common.factory.model.Author;

import de.hdodenhof.circleimageview.CircleImageView;

/* 名称: ITalker.com.wy.common.widget.PortraitView
 * 用户: _VIEW
 * 时间: 2019/9/3,13:58
 * 描述: 自定义头像控件
 */
public class PortraitView extends CircleImageView {
    public PortraitView(Context context) {
        super(context);
    }

    public PortraitView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PortraitView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setup(RequestManager manager, Author author) {
        if (author == null)
            return;
        setup(manager, author.getAvatar());
    }

    public void setup(RequestManager manager, String url) {
        setup(manager, R.drawable.default_portrait, url);
    }

    public void setup(RequestManager manager, int resourceId, String url) {
        if (url == null)
            url = "";
        manager.load(url).placeholder(resourceId).centerCrop().dontAnimate().into(this);
    }
}
