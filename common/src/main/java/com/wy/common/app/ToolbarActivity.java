package com.wy.common.app;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.wy.common.R;

/* 名称: ITalker.com.wy.common.app.ToolbarActivity
 * 用户: _VIEW
 * 时间: 2019/9/13,22:08
 * 描述: ToolbarActivity
 */
public abstract class ToolbarActivity extends BaseActivity {
    protected Toolbar toolbar;

    @Override
    protected void initView() {
        super.initView();
        initToolbar(findViewById(R.id.toolbar));
    }

    /**
     * 初始化toolbar
     *
     * @param toolbar Toolbar
     */
    public void initToolbar(Toolbar toolbar) {
        this.toolbar = toolbar;
        if (this.toolbar != null) {
            setSupportActionBar(this.toolbar);
        }
        initTitleNeedBack();
    }

    protected void initTitleNeedBack() {
        //设置左上角的返回按钮为实际的返回效果
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
    }
}
