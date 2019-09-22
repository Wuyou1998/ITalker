package com.wy.italker.fragments.message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.wy.common.app.BaseFragment;
import com.wy.common.widget.adapter.TextWatcherAdapter;
import com.wy.italker.R;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wy.italker.activities.MessageActivity.KEY_RECEIVER_ID;

/* 名称: ITalker.com.wy.italker.fragments.message.ChatFragment
 * 用户: _VIEW
 * 时间: 2019/9/20,20:33
 * 描述: 聊天界面基类
 */
public abstract class ChatFragment extends BaseFragment implements AppBarLayout.OnOffsetChangedListener {
    protected String mReceiverId;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_recycler)
    RecyclerView rv_recycler;
    @BindView(R.id.abl_app_bar)
    AppBarLayout abl_app_bar;
    @BindView(R.id.edt_content)
    EditText edt_content;
    @BindView(R.id.iv_submit)
    ImageView iv_submit;

    @Override
    protected void initArgs(Bundle bundle) {
        super.initArgs(bundle);
        mReceiverId = bundle.getString(KEY_RECEIVER_ID);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        initToolbar();
        initAppBar();
        //RecyclerView基本设置
        rv_recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        initEditContent();
    }

    //初始化Toolbar
    protected void initToolbar() {
        Toolbar toolbar = this.toolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> {
            Objects.requireNonNull(getActivity()).finish();
        });
    }

    //给AppBar设置一个监听，得到关闭与打开时的进度
    private void initAppBar() {
        abl_app_bar.addOnOffsetChangedListener(this);
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

    }

    private void initEditContent() {
        edt_content.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMessage = !TextUtils.isEmpty(content);
                //设置状态，改变对应的Icon
                iv_submit.setActivated(needSendMessage);
            }
        });
    }

    @OnClick(R.id.iv_face)
    void onFaceClick() {
        //TODO
    }

    @OnClick(R.id.iv_record)
    void onRecordClick() {
        //TODO
    }

    @OnClick(R.id.iv_submit)
    void onSubmitClick() {
        if (iv_submit.isActivated()) {
            //提交
            //TODO
        } else {
            onMoreActionClick();
        }
    }

    void onMoreActionClick() {
        //TODO
    }
}
