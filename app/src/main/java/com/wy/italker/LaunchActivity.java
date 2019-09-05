package com.wy.italker;


import com.wy.common.app.BaseActivity;
;
import com.wy.italker.activities.MainActivity;
import com.wy.italker.fragments.assist.PermissionsFragment;

public class LaunchActivity extends BaseActivity {


    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_launch;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionsFragment.haveAll(this, getSupportFragmentManager())) {
            MainActivity.show(this);
            finish();
        }
    }
}
