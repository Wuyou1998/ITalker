package com.wy.italker.activities;

import android.content.Context;
import android.content.Intent;

import com.wy.common.app.BaseActivity;
import com.wy.italker.R;

public class MessageActivity extends BaseActivity {
    /**
     * 显示聊天界面
     *
     * @param context context
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, MessageActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_message;
    }
}
