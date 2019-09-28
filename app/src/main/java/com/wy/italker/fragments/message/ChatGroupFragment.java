package com.wy.italker.fragments.message;


import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.wy.common.widget.PortraitView;
import com.wy.factory.model.db.Group;
import com.wy.factory.model.db.view.MemberUserModel;
import com.wy.factory.presenter.message.ChatContact;
import com.wy.factory.presenter.message.ChatGroupPresenter;
import com.wy.italker.R;
import com.wy.italker.activities.GroupMemberActivity;
import com.wy.italker.activities.PersonalActivity;

import java.util.List;

import butterknife.BindView;

/**
 * 群聊天界面
 */

public class ChatGroupFragment extends ChatFragment<Group> implements ChatContact.GroupView {
    @BindView(R.id.iv_header)
    ImageView iv_header;
    @BindView(R.id.ll_members)
    LinearLayout ll_members;
    @BindView(R.id.tv_more)
    TextView tv_more;

    @Override
    protected int getHeaderLayoutId() {
        return R.layout.lay_chat_header_group;
    }

    @Override
    protected ChatContact.Presenter initPresenter() {
        return new ChatGroupPresenter(this, mReceiverId);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        Glide.with(getContext()).load(R.mipmap.default_banner_group)
                .centerCrop()
                .into(new ViewTarget<CollapsingToolbarLayout, GlideDrawable>(ctl_app_bar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setContentScrim(resource.getCurrent());
                    }
                });
    }


    @Override
    public void onInit(Group group) {
        ctl_app_bar.setTitle(group.getName());
        Glide.with(getContext())
                .load(group.getPicture())
                .centerCrop()
                .placeholder(R.mipmap.default_banner_group)
                .into(iv_header);
    }

    @Override
    public void onInitGroupMembers(List<MemberUserModel> members, long moreCount) {
        if (members == null || members.size() == 0)
            return;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        for (MemberUserModel member : members) {
            PortraitView p = (PortraitView) inflater.inflate(R.layout.lay_chat_group_avatar, ll_members, false);
            ll_members.addView(p, 0);
            Glide.with(getContext())
                    .load(member.avatar)
                    .centerCrop()
                    .placeholder(R.mipmap.default_portrait)
                    .dontAnimate()
                    .into(p);
            //查看个人信息界面
            p.setOnClickListener(v -> PersonalActivity.show(getContext(), member.userId));
        }
        //更多成员
        if (moreCount > 0) {
            tv_more.setText(String.format("+%s", moreCount));
            tv_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //显示成员列表
                    //mReceiverId 就是群的id
                    GroupMemberActivity.show(getContext(), mReceiverId);
                }
            });
        } else {
            tv_more.setVisibility(View.GONE);
        }
    }

    @Override
    public void showAdminOption(boolean isAdmin) {
        if (isAdmin) {
            toolbar.inflateMenu(R.menu.chat_group);
            toolbar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_add) {
                    //成员添加操作
                    //mReceiverId 就是群的id
                    GroupMemberActivity.showAdmin(getContext(), mReceiverId);
                    return true;
                }
                return false;
            });
        }
    }

    //进行高度的综合运算，透明我们的头像和Icon
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        super.onOffsetChanged(appBarLayout, verticalOffset);
        View view = ll_members;
        if (view == null)
            return;
        if (verticalOffset == 0) {
            //完全展开
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);

        } else {
            //abs运算
            verticalOffset = Math.abs(verticalOffset);
            final int totalScrollRange = appBarLayout.getTotalScrollRange();
            if (verticalOffset >= totalScrollRange) {
                //关闭状态
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);

            } else {
                //中间状态
                float progress = 1 - (verticalOffset / (float) totalScrollRange);
                view.setVisibility(View.VISIBLE);
                view.setScaleX(progress);
                view.setScaleY(progress);
                view.setAlpha(progress);

            }

        }
    }
}
