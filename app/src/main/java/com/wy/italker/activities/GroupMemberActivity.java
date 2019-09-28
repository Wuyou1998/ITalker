package com.wy.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wy.common.app.PresenterToolbarActivity;
import com.wy.common.widget.PortraitView;
import com.wy.common.widget.recycler.RecyclerAdapter;
import com.wy.factory.model.db.view.MemberUserModel;
import com.wy.factory.presenter.group.GroupMembersContract;
import com.wy.factory.presenter.group.GroupMembersPresenter;
import com.wy.italker.R;

import butterknife.BindView;
import butterknife.OnClick;

public class GroupMemberActivity extends PresenterToolbarActivity<GroupMembersContract.Presenter> implements GroupMembersContract.View {
    private static final String KEY_GROUP_ID = "KEY_GROUP_ID";
    private static final String KEY_GROUP_ADMIN = "KEY_GROUP_ADMIN";

    @BindView(R.id.rv_members)
    RecyclerView rv_members;

    private RecyclerAdapter<MemberUserModel> adapter;

    private String groupId;

    private boolean isAdmin;

    public static void show(Context context, String groupId) {
        show(context, groupId, false);
    }

    public static void showAdmin(Context context, String groupId) {
        show(context, groupId, true);
    }

    public static void show(Context context, String groupId, boolean isAdmin) {
        if (TextUtils.isEmpty(groupId))
            return;
        Intent intent = new Intent(context, GroupMemberActivity.class);
        intent.putExtra(KEY_GROUP_ID, groupId);
        intent.putExtra(KEY_GROUP_ADMIN, isAdmin);
        context.startActivity(intent);
    }

    @Override
    protected boolean initArgs(Bundle bundle) {
        groupId = bundle.getString(KEY_GROUP_ID);
        isAdmin = bundle.getBoolean(KEY_GROUP_ADMIN);
        return !TextUtils.isEmpty(groupId);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_member;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle(R.string.title_member_list);
        rv_members.setLayoutManager(new LinearLayoutManager(this));
        rv_members.setAdapter(adapter = new RecyclerAdapter<MemberUserModel>() {
            @Override
            protected int getItemViewType(int position, MemberUserModel memberUserModel) {
                return R.layout.cell_group_create_contact;
            }

            @Override
            protected ViewHolder<MemberUserModel> onCreateViewHolder(View root, int viewType) {
                return new GroupMemberActivity.ViewHolder(root);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        //开始数据刷新
        mPresenter.refresh();
    }

    @Override
    protected GroupMembersContract.Presenter initPresenter() {
        return new GroupMembersPresenter(this);
    }

    @Override
    public RecyclerAdapter<MemberUserModel> getRecyclerAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {
        hideLoading();
    }

    @Override
    public String getGroupId() {
        return groupId;
    }


    class ViewHolder extends RecyclerAdapter.ViewHolder<MemberUserModel> {
        @BindView(R.id.iv_avatar)
        PortraitView iv_avatar;
        @BindView(R.id.tv_name)
        TextView tv_name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.findViewById(R.id.cb_select).setVisibility(View.GONE);
        }

        @Override
        protected void onBind(MemberUserModel model) {
            iv_avatar.setup(Glide.with(GroupMemberActivity.this), model.avatar);
            tv_name.setText(model.name);
        }

        @OnClick(R.id.iv_avatar)
        void onAvatarClick() {
            PersonalActivity.show(GroupMemberActivity.this, mData.userId);
        }

    }
}
