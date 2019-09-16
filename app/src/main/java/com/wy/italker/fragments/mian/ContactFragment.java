package com.wy.italker.fragments.mian;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wy.common.app.PresenterFragment;
import com.wy.common.widget.EmptyView;
import com.wy.common.widget.PortraitView;
import com.wy.common.widget.recycler.RecyclerAdapter;
import com.wy.factory.model.db.User;
import com.wy.factory.presenter.contact.ContactContract;
import com.wy.factory.presenter.contact.ContactPresenter;
import com.wy.italker.R;
import com.wy.italker.activities.MessageActivity;
import com.wy.italker.activities.PersonalActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;


public class ContactFragment extends PresenterFragment<ContactContract.Presenter>
        implements ContactContract.View {
    @BindView(R.id.rv_user)
    RecyclerView rv_user;
    @BindView(R.id.ev_empty)
    EmptyView ev_empty;
    private RecyclerAdapter<User> adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contact;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        rv_user.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_user.setAdapter(adapter = new RecyclerAdapter<User>() {

            @Override
            protected int getItemViewType(int position, User user) {
                return R.layout.cell_contact_list;
            }

            @Override
            protected ViewHolder<User> onCreateViewHolder(View root, int viewType) {
                return new ContactFragment.ViewHolder(root);
            }
        });
        //设置点击事件监听
        adapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<User>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder viewHolder, User user) {
                //跳转到聊天界面
                MessageActivity.show(Objects.requireNonNull(getContext()));
            }
        });
        //初始化占位布局
        ev_empty.bind(rv_user);
        setPlaceHolderView(ev_empty);
    }

    @Override
    protected void onFirstInit() {
        //进行一次数据加载
        presenter.start();
    }

    @Override
    protected ContactContract.Presenter initPresenter() {
        //初始化Presenter
        return new ContactPresenter(this);
    }

    @Override
    public RecyclerAdapter<User> getRecyclerAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {
        placeHolderView.triggerOkOrEmpty(adapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<User> {
        @BindView(R.id.iv_avatar)
        PortraitView iv_avatar;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_desc)
        TextView tv_desc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
        @OnClick(R.id.iv_avatar)
        void onAvatarClick() {
            //跳转到个人信息界面
            PersonalActivity.show(getContext(), mData.getId());
        }

        @Override
        protected void onBind(User user) {
            iv_avatar.setup(Glide.with(ContactFragment.this.getContext()), user);
            tv_name.setText(user.getName());
            tv_desc.setText(user.getDescription());
        }
    }
}
