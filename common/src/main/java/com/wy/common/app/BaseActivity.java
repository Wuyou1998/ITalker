package com.wy.common.app;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.wy.common.widget.convention.PlaceHolderView;

import java.util.List;

import butterknife.ButterKnife;

/* 名称: ITalker.com.wy.common.app.BaseActivity
 * 用户: _VIEW
 * 时间: 2019/9/2,10:48
 * 描述: activity 基类
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected PlaceHolderView placeHolderView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在界面未初始化之前初始化窗口
        initWindow();
        if (initArgs(getIntent().getExtras())) {
            setContentView(getContentLayoutId());
            initBefore();
            initView();
            initData();
        } else {
            finish();
        }
    }

    /**
     * 初始化控件调用之前
     */
    protected void initBefore() {

    }

    /**
     * 初始化窗口
     */
    protected void initWindow() {
    }

    /**
     * 初始化界面相关参数
     *
     * @param bundle 参数bundle
     * @return 参数正确返回true 错误 false
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 得到当前资源文件id
     *
     * @return 资源文件id
     */
    protected abstract int getContentLayoutId();

    //初始化UI
    protected void initView() {
        ButterKnife.bind(this);
    }

    //初始化数据
    protected void initData() {
    }

    @Override
    public boolean onSupportNavigateUp() {
        //当导航栏back被点击后 finish当前页面
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        //得到当前activity下的所有fragment
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        //判断是否为空
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                //判断是否为BaseFragment
                if (fragment instanceof BaseFragment) {
                    //判断BaseFragment是否自行处理了事件
                    if (((BaseFragment) fragment).onBackPressed())
                        return;
                }
            }
        }
        super.onBackPressed();
        finish();
    }

    /**
     * 设置占位布局
     *
     * @param placeHolderView 继承了占位布局规范的View
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.placeHolderView = placeHolderView;
    }
}
