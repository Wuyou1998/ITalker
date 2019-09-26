package com.wy.italker.fragments.search;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wy.common.app.PresenterFragment;
import com.wy.common.widget.EmptyView;
import com.wy.common.widget.PortraitView;
import com.wy.common.widget.recycler.RecyclerAdapter;
import com.wy.factory.model.card.GroupCard;
import com.wy.factory.presenter.contact.FollowContract;
import com.wy.factory.presenter.search.SearchContact;
import com.wy.factory.presenter.search.SearchGroupPresenter;
import com.wy.italker.R;
import com.wy.italker.activities.PersonalActivity;
import com.wy.italker.activities.SearchActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class SearchGroupFragment extends PresenterFragment<SearchContact.Presenter>
        implements SearchActivity.SearchFragment, SearchContact.GroupView {
    @BindView(R.id.rv_group)
    RecyclerView rv_group;
    @BindView(R.id.ev_empty)
    EmptyView ev_empty;

    private RecyclerAdapter<GroupCard> adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        rv_group.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_group.setAdapter(adapter = new RecyclerAdapter<GroupCard>() {
            @Override
            protected int getItemViewType(int position, GroupCard groupCard) {
                //返回item的布局id
                return R.layout.cell_serch_group_list;
            }

            @Override
            protected ViewHolder<GroupCard> onCreateViewHolder(View root, int viewType) {
                return new SearchGroupFragment.ViewHolder(root);
            }
        });
        //初始化占位布局
        ev_empty.bind(rv_group);
        setPlaceHolderView(ev_empty);
    }

    @Override
    protected void initData() {
        super.initData();
        search("");
    }

    @Override
    public void search(String content) {
        presenter.search(content);
    }

    @Override
    protected SearchContact.Presenter initPresenter() {
        return new SearchGroupPresenter(this);
    }

    @Override
    public void onSearchDone(List<GroupCard> groupCards) {
        //成功的情况下返回数据
        adapter.replace(groupCards);
        //如果有数据，就展示数据，没有数据就显示空布局
        placeHolderView.triggerOkOrEmpty(adapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCard> {
        @BindView(R.id.iv_avatar)
        PortraitView iv_avatar;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.iv_join)
        ImageView iv_join;

        private FollowContract.Presenter presenter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        @Override
        protected void onBind(GroupCard groupCard) {
            iv_avatar.setup(Glide.with(getContext()), groupCard.getPicture());
            tv_name.setText(groupCard.getName());
            //根据加入时间判断是否加入群，有加入时间则一定是加入了群
            iv_join.setEnabled(groupCard.getJoinAt() == null);
        }

        @OnClick(R.id.iv_join)
        void onJoinClick() {
            //进入创建者个人界面
            PersonalActivity.show(getContext(), mData.getOwnerId());
        }
    }
}
