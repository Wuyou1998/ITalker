package com.wy.italker.fragments.mian;


import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wy.common.app.PresenterFragment;
import com.wy.common.utils.DateTimeUtil;
import com.wy.common.widget.EmptyView;
import com.wy.common.widget.PortraitView;
import com.wy.common.widget.recycler.RecyclerAdapter;
import com.wy.factory.model.db.Session;
import com.wy.factory.presenter.message.SessionContract;
import com.wy.factory.presenter.message.SessionPresenter;
import com.wy.italker.R;
import com.wy.italker.activities.MessageActivity;

import java.util.Objects;

import butterknife.BindView;


public class ActiveFragment extends PresenterFragment<SessionContract.Presenter>
        implements SessionContract.View {
    @BindView(R.id.rv_session)
    RecyclerView rv_session;
    @BindView(R.id.ev_empty)
    EmptyView ev_empty;

    private RecyclerAdapter<Session> adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        rv_session.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_session.setAdapter(adapter = new RecyclerAdapter<Session>() {

            @Override
            protected int getItemViewType(int position, Session session) {
                return R.layout.cell_chat_list;
            }

            @Override
            protected ViewHolder<Session> onCreateViewHolder(View root, int viewType) {
                return new ActiveFragment.ViewHolder(root);
            }
        });
        //设置点击事件监听
        adapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<Session>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder viewHolder, Session session) {
                //跳转到聊天界面
                MessageActivity.show(Objects.requireNonNull(getContext()), session);
            }
        });
        //初始化占位布局
        ev_empty.bind(rv_session);
        setPlaceHolderView(ev_empty);
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        //进行一次数据加载
        presenter.start();
    }

    @Override
    protected SessionContract.Presenter initPresenter() {
        return new SessionPresenter(ActiveFragment.this);
    }

    @Override
    public RecyclerAdapter<Session> getRecyclerAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {
        placeHolderView.triggerOkOrEmpty(adapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Session> {
        @BindView(R.id.iv_avatar)
        PortraitView iv_avatar;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_content)
        TextView tv_content;
        @BindView(R.id.tv_time)
        TextView tv_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }


        @Override
        protected void onBind(Session session) {
            iv_avatar.setup(Glide.with(getContext()), session.getPicture());
            tv_name.setText(session.getTitle());
            tv_content.setText(TextUtils.isEmpty(session.getContent()) ? "" : session.getContent());
            tv_time.setText(DateTimeUtil.getSampleDate(session.getModifyAt()));
        }
    }
}
