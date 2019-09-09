package com.wy.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.wy.common.app.BaseActivity;
import com.wy.italker.R;
import com.wy.italker.fragments.account.AccountTrigger;
import com.wy.italker.fragments.account.LoginFragment;
import com.wy.italker.fragments.account.RegisterFragment;

import net.qiujuer.genius.ui.compat.UiCompat;

import butterknife.BindView;

public class AccountActivity extends BaseActivity implements AccountTrigger {
    private Fragment mCurFragment;
    private Fragment loginFragment;
    private Fragment registerFragment;

    @BindView(R.id.iv_bg)
    ImageView iv_bg;

    /**
     * 账户Activity的入口
     *
     * @param context 上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_account;
    }

    @Override
    protected void initView() {
        super.initView();
        //初始化Fragment
        mCurFragment = loginFragment = new LoginFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, mCurFragment)
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
    public void triggerView() {
        Fragment fragment;
        if (mCurFragment == loginFragment) {
            if (registerFragment == null) {
                //第一次为null，新建
                registerFragment = new RegisterFragment();
            }
            fragment = registerFragment;
        } else {
            //默认情况下已经赋值，无需判空
            fragment = loginFragment;
        }

        mCurFragment = fragment;
        //切换显示
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, mCurFragment).commit();
    }

}
