package com.wy.italker.fragments.account;

import android.content.Context;

import com.wy.common.app.BaseFragment;
import com.wy.italker.R;

/**
 * 登录
 */
public class LoginFragment extends BaseFragment {

    private AccountTrigger accountTrigger;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到对Activity的引用
        accountTrigger = (AccountTrigger) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        //切换到注册页面
        accountTrigger.triggerView();
    }
}
