package com.wy.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wy.common.app.BaseActivity;
import com.wy.common.widget.PortraitView;
import com.wy.factory.persistence.Account;
import com.wy.italker.R;
import com.wy.italker.fragments.helper.NavHelper;
import com.wy.italker.fragments.mian.ActiveFragment;
import com.wy.italker.fragments.mian.ContactFragment;
import com.wy.italker.fragments.mian.GroupFragment;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        NavHelper.OnTabChangeListener<Integer> {

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
    private ActiveFragment activeFragment;
    private GroupFragment groupFragment;
    private ContactFragment contactFragment;
    private NavHelper<Integer> navHelper;
    private static final String TAG = "MainActivity";

    /**
     * MainActivity 显示的入口
     *
     * @param context 上下文
     */
    public static void show(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        if (Account.isComplete())
            //判断用户信息是否完全，完全则走正常流程
            return super.initArgs(bundle);
        else {
            UserActivity.show(this);
            return false;
        }

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
        //初始化底部辅助工具类
        navHelper = new NavHelper<>(this, getSupportFragmentManager(), R.id.fl_container, this);
        navHelper.add(R.id.action_home, new NavHelper.Tab<>(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab<>(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab<>(ContactFragment.class, R.string.title_contact));
        nbv_navigation.setOnNavigationItemSelectedListener(this);
        activeFragment = new ActiveFragment();
        groupFragment = new GroupFragment();
        contactFragment = new ContactFragment();
    }

    @Override
    protected void initData() {
        super.initData();
        //从底部导航中接管menu，手动触发第一次点击事件
        Menu menu = nbv_navigation.getMenu();
        //触发点击 home
        menu.performIdentifierAction(R.id.action_home, 0);
        //初始化头像
        iv_avatar.setup(Glide.with(this),Account.getUser());

    }

    @OnClick(R.id.iv_avatar)
    void onAvatarClick() {
//        AccountActivity.show(this);
        PersonalActivity.show(this, Account.getUserId());
    }

    @OnClick(R.id.f_btn_action)
    void onFBtnClick() {
        //浮动按钮点击时，当前页面如果是群，则打开创建群页面，否则打开添加联系人页面
        if (Objects.equals(navHelper.getCurrentTab().extra, R.string.title_group)) {
            //TODO 群创建
            SearchActivity.show(this, SearchActivity.TYPE_GROUP);
        } else if (Objects.equals(navHelper.getCurrentTab().extra, R.string.title_contact)) {
            SearchActivity.show(this, SearchActivity.TYPE_USER);
        }

    }

    @OnClick(R.id.iv_search)
    void onSearchClick() {
        //在群的界面时，点击顶部的搜索就打开去搜索页面，其他均为人搜索页面
        int type = Objects.equals(navHelper.getCurrentTab().extra, R.string.title_group) ?
                SearchActivity.TYPE_GROUP : SearchActivity.TYPE_USER;
        SearchActivity.show(this, type);
    }

    //底部导航栏被选中时调用
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return navHelper.performClickMenu(item.getItemId());
    }

    /**
     * NavHelper处理点击后回调的helper
     *
     * @param newTab 新的tab
     * @param oldTab 上一个tab
     */
    @Override
    public void onTabChange(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        //从额外字段中取出title资源id
        tv_title.setText(newTab.extra);
        //floatingActionButton的动画处理
        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            transY = Ui.dipToPx(getResources(), 78);
        } else {
            //transY 为 0 默认显示
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                //群组
                f_btn_action.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else {
                //联系人
                f_btn_action.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }
        //开始动画，旋转，Y轴位移，弹性插值器
        f_btn_action.animate()
                .rotation(rotation)
                .translationY(transY)
                .setDuration(480)
                .setInterpolator(new AnticipateOvershootInterpolator(1))
                .start();
    }
}
