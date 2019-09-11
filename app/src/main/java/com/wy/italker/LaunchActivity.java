package com.wy.italker;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Property;
import android.view.View;

import com.wy.common.app.BaseActivity;
import com.wy.factory.persistence.Account;
import com.wy.italker.activities.AccountActivity;
import com.wy.italker.activities.MainActivity;
import com.wy.italker.fragments.assist.PermissionsFragment;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

;

public class LaunchActivity extends BaseActivity {
    //Drawable
    private ColorDrawable mBgDrawable;
    // 是否已经得到PushId
    private boolean mAlreadyGotPushReceiverId = false;


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void initView() {
        super.initView();

        //拿到根布局
        View root = findViewById(R.id.activity_launch);
        //获取颜色
        int color = UiCompat.getColor(getResources(), R.color.colorPrimary);
        //创建一个Drawable
        ColorDrawable colorDrawable = new ColorDrawable(color);
        //设置背景
        root.setBackground(colorDrawable);
        mBgDrawable = colorDrawable;
    }

    @Override
    protected void initData() {
        super.initData();
        //动画进行到50%，等待pushId获取到
        startAnim(0.5f, this::waitPushReceiveId);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // 判断是否已经得到推送Id，如果已经得到则进行跳转操作，
        // 在操作中检测权限状态
        if (mAlreadyGotPushReceiverId) {
            realSkip();
        }
    }

    /**
     * 等待个推框架对id设置好值
     */
    private void waitPushReceiveId() {
        //判断是否登录
        if (Account.isLogin()) {
            //已经登录判断是否绑定，如果没有绑定则等待广播接收器进行绑定
            if (Account.isBind()) {
                skip();
                return;
            }
        } else {
            //没有登录，如果拿到了PushId，没有登录是不能绑定PushId的
            if (!TextUtils.isEmpty(Account.getPushId())) {
                waitPushReceiverIdDone();
                return;
            }
        }

        //间隔500毫秒的循环等待
        getWindow().getDecorView().postDelayed(this::waitPushReceiveId, 500);
    }

    /**
     * 先把动画剩下的50%完成
     * 如果都有权限就跳转MainActivity，自己finish
     */
    private void skip() {
        startAnim(1f, this::realSkip);
    }

    /**
     * 在跳转之前需要把剩下的50%进行完成
     */
    private void waitPushReceiverIdDone() {
        // 标志已经得到PushId
        mAlreadyGotPushReceiverId = true;
        startAnim(1f, this::realSkip);
    }

    /**
     * 如果都有权限就跳转MainActivity，自己finish
     */
    private void realSkip() {
        if (PermissionsFragment.haveAll(this, getSupportFragmentManager())) {
            //检查跳转到主页还是登录
            if (Account.isLogin()) {
                MainActivity.show(this);
            } else {
                AccountActivity.show(this);
            }
            finish();
        }
    }

    /**
     * 给背景设置一个动画
     *
     * @param endProgress 动画的结束进度
     * @param endCallback 动画结束时触发
     */
    private void startAnim(float endProgress, Runnable endCallback) {
        //获取一个最终的颜色
        int finalColor = Resource.Color.WHITE;
        //运算当前进度的颜色
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        int endColor = (int) argbEvaluator.evaluate(endProgress, mBgDrawable.getColor(), finalColor);
        //构建一个属性动画
        ValueAnimator valueAnimator = ObjectAnimator.ofObject(this, property, argbEvaluator, endColor);
        valueAnimator.setDuration(1000);
        valueAnimator.setIntValues(mBgDrawable.getColor(), endColor);//开始结束设置
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                endCallback.run();
            }
        });
        valueAnimator.start();
    }

    private final Property<LaunchActivity, Object> property = new Property<LaunchActivity, Object>(Object.class, "color") {
        @Override
        public Object get(LaunchActivity launchActivity) {
            return launchActivity.mBgDrawable.getColor();
        }

        @Override
        public void set(LaunchActivity object, Object value) {
            object.mBgDrawable.setColor((Integer) value);
        }
    };

}
