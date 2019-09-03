package com.wy.italker;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wy.common.app.BaseActivity;
import com.wy.common.widget.PortraitView;

import net.qiujuer.genius.ui.widget.FloatActionButton;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity{

    @BindView(R.id.abl_app_bar)
    View abl_app_bar;

    @BindView(R.id.iv_avatar)
    PortraitView iv_avatar;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.iv_search)
    ImageView iv_search;

    @BindView(R.id.fl_container)
    FrameLayout fl_container;

    @BindView(R.id.f_btn_action)
    FloatActionButton f_btn_action;

    @BindView(R.id.nbv_navigation)
    BottomNavigationView nbv_navigation;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        //给AppBar设置背景 在xml中设置会有拉伸
        Glide.with(this).load(R.mipmap.bg_src_morning).centerCrop().into(new ViewTarget<View, GlideDrawable>(abl_app_bar) {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                this.view.setBackground(resource.getCurrent());
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
    }

    @OnClick(R.id.iv_avatar)
    void onAvatarClick(){

    }

    @OnClick(R.id.f_btn_action)
    void onFBtnClick(){

    }
    @OnClick(R.id.iv_search)
    void onSearchClick(){
        
    }
}
