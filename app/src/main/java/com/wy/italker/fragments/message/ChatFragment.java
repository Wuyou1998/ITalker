package com.wy.italker.fragments.message;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.wy.common.app.PresenterFragment;
import com.wy.common.widget.PortraitView;
import com.wy.common.widget.adapter.TextWatcherAdapter;
import com.wy.common.widget.recycler.RecyclerAdapter;
import com.wy.factory.model.db.Message;
import com.wy.factory.model.db.User;
import com.wy.factory.persistence.Account;
import com.wy.factory.presenter.message.ChatContact;
import com.wy.italker.R;

import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.widget.Loading;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static com.wy.italker.activities.MessageActivity.KEY_RECEIVER_ID;

/* 名称: ITalker.com.wy.italker.fragments.message.ChatFragment
 * 用户: _VIEW
 * 时间: 2019/9/20,20:33
 * 描述: 聊天界面基类
 */
public abstract class ChatFragment<InitModel> extends PresenterFragment<ChatContact.Presenter>
        implements AppBarLayout.OnOffsetChangedListener,
        ChatContact.View<InitModel> {
    protected String mReceiverId;
    protected Adapter adapter;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_recycler)
    RecyclerView rv_recycler;
    @BindView(R.id.abl_app_bar)
    AppBarLayout abl_app_bar;
    @BindView(R.id.ctl_app_bar)
    CollapsingToolbarLayout ctl_app_bar;
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
        rv_recycler.setAdapter(adapter = new Adapter());
        initEditContent();
    }

    @Override
    protected void initData() {
        super.initData();
        //开始进行初始化操作
        presenter.start();
    }

    //初始化Toolbar
    protected void initToolbar() {
        Toolbar toolbar = this.toolbar;
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(v -> Objects.requireNonNull(getActivity()).finish());
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
            String content = edt_content.getText().toString().trim();
            edt_content.setText("");
            presenter.pushText(content);
        } else {
            onMoreActionClick();
        }
    }

    void onMoreActionClick() {
        //TODO
    }

    @Override
    public RecyclerAdapter<Message> getRecyclerAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {
        //界面没有显示布局，recyclerView一直显示，故该方法不需要任何实现
    }

    private class Adapter extends RecyclerAdapter<Message> {

        @Override
        protected int getItemViewType(int position, Message message) {
            boolean isRight = Objects.equals(message.getSender().getId(), Account.getAccount());
            switch (message.getType()) {
                case Message.TYPE_AUDIO:
                    return isRight ? R.layout.cell_chat_audio_right : R.layout.cell_chat_audio_left;
                case Message.TYPE_PIC:
                    return isRight ? R.layout.cell_chat_pic_right : R.layout.cell_chat_pic_left;
                //默认文字内容,我发送的在右边，收到的在左边
                default:
                    return isRight ? R.layout.cell_chat_text_right : R.layout.cell_chat_text_right;
            }
        }

        @Override
        protected ViewHolder<Message> onCreateViewHolder(View root, int viewType) {
            switch (viewType) {
//                case R.layout.cell_chat_text_right:
//                case R.layout.cell_chat_text_left:
//                    return new TextHolder(root);
                case R.layout.cell_chat_audio_right:
                case R.layout.cell_chat_audio_left:
                    return new AudioHolder(root);
                case R.layout.cell_chat_pic_right:
                case R.layout.cell_chat_pic_left:
                    return new PicHolder(root);
                default:
                    //默认情况下返回的就是Text类型的Holder进行处理
                    return new TextHolder(root);
            }
        }
    }

    //holder 基类
    class BaseHolder extends RecyclerAdapter.ViewHolder<Message> {
        @BindView(R.id.iv_avatar)
        PortraitView iv_avatar;
        //允许为空，左边没有，右边有
        @Nullable
        @BindView(R.id.loading)
        Loading loading;

        public BaseHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            User sender = message.getSender();
            //进行数据加载
            sender.load();
            iv_avatar.setup(Glide.with(getContext()), sender);
            if (loading != null) {
                int status = message.getStatus();
                //当前布局应该是在右边
                if (status == Message.STATUS_DONE) {
                    loading.stop();
                    loading.setVisibility(View.GONE);
                } else if (status == Message.STATUS_CREATED) {
                    //正在发送
                    loading.setVisibility(View.VISIBLE);
                    loading.setProgress(0);
                    loading.setForegroundColor(UiCompat.getColor(getResources(), R.color.colorAccent));
                    loading.start();
                } else if (status == Message.STATUS_FAILED) {
                    //发送失败,点击头像重新发送
                    loading.setVisibility(View.VISIBLE);
                    loading.stop();
                    loading.setProgress(1);
                    loading.setForegroundColor(UiCompat.getColor(getResources(), R.color.red_400));
                }
                //只有当前状态是发送失败，才允许点击
                iv_avatar.setEnabled(status == Message.STATUS_FAILED);
            }

        }

        @OnClick(R.id.iv_avatar)
        void onRePushClick() {
            //重新发送
            if (loading != null && presenter.rePush(mData)) {
                //必须是右边的才有可能重新发送
                //状态改变需要重新刷新当前界面的信息
                updateData(mData);

            }
        }
    }

    //文字holder
    class TextHolder extends BaseHolder {
        @BindView(R.id.tv_content)
        TextView tv_content;

        public TextHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            //内容设置到布局
            tv_content.setText(message.getContent());
        }
    }

    //audio holder
    class AudioHolder extends BaseHolder {
        @BindView(R.id.tv_content)
        TextView tv_content;

        public AudioHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            //TODO
        }
    }

    //图片holder
    class PicHolder extends BaseHolder {
        @BindView(R.id.tv_content)
        TextView tv_content;

        public PicHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(Message message) {
            super.onBind(message);
            //TODO
        }
    }
}
