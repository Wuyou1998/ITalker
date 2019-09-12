package com.wy.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.wy.common.app.BaseActivity;
import com.wy.italker.R;
import com.wy.italker.fragments.user.UpdateInfoFragment;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;

/**
 * 用户信息界面 可以提供用户信息修改
 */
public class UserActivity extends BaseActivity {
    private UpdateInfoFragment updateInfoFragment;
    @BindView(R.id.iv_bg)
    ImageView iv_bg;

    public static void show(Context context) {
        context.startActivity(new Intent(context, UserActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_user;
    }

    @Override
    protected void initView() {
        super.initView();
        updateInfoFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, updateInfoFragment)
                .commit();

        //初始化背景
        Glide.with(this).load(R.mipmap.bg_src_tianjin).centerCrop().into(
                new ViewTarget<ImageView, GlideDrawable>(iv_bg) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        Drawable drawable = resource.getCurrent();
                        //适配包包装一层
                        drawable = DrawableCompat.wrap(drawable);
                        drawable.setColorFilter(UiCompat.getColor(getResources(), R.color.colorAccent)
                                , PorterDuff.Mode.SCREEN);//设置着色效果和颜色，蒙版模式
                        this.view.setImageDrawable(drawable);
                    }
                }
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (updateInfoFragment != null)
            updateInfoFragment.onActivityResult(requestCode, resultCode, data);
    }
}
