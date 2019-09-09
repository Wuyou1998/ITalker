package com.wy.italker.activities;

import androidx.annotation.Nullable;

import android.content.Intent;

import com.wy.common.app.BaseActivity;
import com.wy.italker.R;
import com.wy.italker.fragments.user.UpdateInfoFragment;

public class UserActivity extends BaseActivity {
    private UpdateInfoFragment updateInfoFragment;

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
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (updateInfoFragment != null)
            updateInfoFragment.onActivityResult(requestCode, resultCode, data);
    }
}
