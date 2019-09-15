package com.wy.common.app;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wy.common.widget.convention.PlaceHolderView;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/* 名称: ITalker.com.wy.common.app.BaseFragment
 * 用户: _VIEW
 * 时间: 2019/9/2,10:48
 * 描述: Fragment基类
 */
public abstract class BaseFragment extends Fragment {
    protected View mRoot;
    protected Unbinder unbinder;
    protected PlaceHolderView placeHolderView;
    protected boolean isFirstInitData = true;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoot == null) {
            View root = inflater.inflate(getContentLayoutId(), container, false);
            initView(root);
            mRoot = root;
        } else {
            if (mRoot.getParent() != null) {
                //当fragment被回收，mRoot还没被回收，将mRoot从其父布局移除
                ((ViewGroup) mRoot.getParent()).removeView(mRoot);
            }
        }
        return mRoot;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isFirstInitData) {
            //触发一次后就不会再触发
            isFirstInitData = false;
            onFirstInit();
        }
        //View初始化完成后初始化数据
        initData();
    }

    /**
     * 初始化界面相关参数
     */
    protected void initArgs(Bundle bundle) {
    }

    /**
     * 首次初始化数据的时候会调用
     */
    protected void onFirstInit() {

    }

    /**
     * 得到当前资源文件id
     *
     * @return 资源文件id
     */
    protected abstract int getContentLayoutId();

    //初始化UI
    protected void initView(View view) {
        unbinder = ButterKnife.bind(this, view);
    }

    //初始化数据
    protected void initData() {
    }

    /**
     * 点击返回键后触发
     *
     * @return true表示fragment自身已经处理，activity无须关心，false表示返回事件交由activity处理
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * 设置占位符View
     *
     * @param placeHolderView 实现了PlaceHolderView接口的view
     */
    public void setPlaceHolderView(PlaceHolderView placeHolderView) {
        this.placeHolderView = placeHolderView;
    }
}
