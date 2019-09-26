package com.wy.italker.fragments.mian;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wy.common.app.PresenterFragment;
import com.wy.common.widget.EmptyView;
import com.wy.common.widget.PortraitView;
import com.wy.common.widget.recycler.RecyclerAdapter;
import com.wy.factory.model.db.Group;
import com.wy.factory.presenter.group.GroupContract;
import com.wy.factory.presenter.group.GroupPresenter;
import com.wy.italker.R;
import com.wy.italker.activities.MessageActivity;
import com.wy.italker.activities.PersonalActivity;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupFragment extends PresenterFragment<GroupContract.Presenter>
        implements GroupContract.View {
    @BindView(R.id.rv_user)
    RecyclerView rv_user;
    @BindView(R.id.ev_empty)
    EmptyView ev_empty;
    private RecyclerAdapter<Group> adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_group;
    }

    @Override
    protected void onFirstInit() {
        super.onFirstInit();
        presenter.start();
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        rv_user.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv_user.setAdapter(adapter = new RecyclerAdapter<Group>() {

            @Override
            protected int getItemViewType(int position, Group group) {
                return R.layout.cell_group_list;
            }

            @Override
            protected ViewHolder<Group> onCreateViewHolder(View root, int viewType) {
                return new GroupFragment.ViewHolder(root);
            }
        });
        //设置点击事件监听
        adapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<Group>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder viewHolder, Group group) {
                //跳转到聊天界面
                MessageActivity.show(Objects.requireNonNull(getContext()), group);
            }
        });
        //初始化占位布局
        ev_empty.bind(rv_user);
        setPlaceHolderView(ev_empty);
    }

    @Override
    protected GroupContract.Presenter initPresenter() {
        return new GroupPresenter(this);
    }

    @Override
    public RecyclerAdapter<Group> getRecyclerAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {
        placeHolderView.triggerOkOrEmpty(adapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<Group> {
        @BindView(R.id.iv_avatar)
        PortraitView iv_avatar;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.tv_desc)
        TextView tv_desc;
        @BindView(R.id.tv_member)
        TextView tv_member;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        @OnClick(R.id.iv_avatar)
        void onAvatarClick() {
            //跳转到个人信息界面
            PersonalActivity.show(getContext(), mData.getId());
        }

        @Override
        protected void onBind(Group group) {
            iv_avatar.setup(Glide.with(GroupFragment.this.getContext()), group.getPicture());
            tv_name.setText(group.getName());
            tv_desc.setText(group.getDescription());
            if (group.holder instanceof String) {
                tv_member.setText((String) group.holder);
            } else {
                tv_member.setText("");
            }
        }
    }
}
