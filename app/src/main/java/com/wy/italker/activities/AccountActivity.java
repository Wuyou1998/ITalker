package com.wy.italker.activities;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.wy.common.app.BaseActivity;
import com.wy.italker.R;
import com.wy.italker.fragments.account.UpdateInfoFragment;

public class AccountActivity extends BaseActivity {

    private UpdateInfoFragment updateInfoFragment;

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
        updateInfoFragment = new UpdateInfoFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, updateInfoFragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (updateInfoFragment != null)
            updateInfoFragment.onActivityResult(requestCode, resultCode, data);
    }
}
