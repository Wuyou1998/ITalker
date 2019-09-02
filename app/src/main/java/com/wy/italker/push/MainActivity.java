package com.wy.italker.push;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wy.common.app.BaseActivity;
import com.wy.italker.R;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.tv_text)
    TextView tv_text;
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
       tv_text.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               tv_text.setText("123456");
           }
       });
    }
}
