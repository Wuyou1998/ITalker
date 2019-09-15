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
import com.wy.factory.model.card.UserCard;
import com.wy.factory.presenter.contact.FollowContract;
import com.wy.factory.presenter.contact.FollowPresenter;
import com.wy.factory.presenter.search.SearchContact;
import com.wy.factory.presenter.search.SearchUserPresenter;
import com.wy.italker.R;
import com.wy.italker.activities.SearchActivity;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.compat.UiCompat;
import net.qiujuer.genius.ui.drawable.LoadingCircleDrawable;
import net.qiujuer.genius.ui.drawable.LoadingDrawable;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class SearchUserFragment extends PresenterFragment<SearchContact.Presenter>
        implements SearchActivity.SearchFragment, SearchContact.UserView {
    @BindView(R.id.rv_user)
    RecyclerView rv_user;
    @BindView(R.id.ev_empty)
    EmptyView ev_empty;

    private RecyclerAdapter<UserCard> adapter;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_user;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        rv_user.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_user.setAdapter(adapter = new RecyclerAdapter<UserCard>() {
            @Override
            protected int getItemViewType(int position, UserCard userCard) {
                //返回item的布局id
                return R.layout.cell_search_list;
            }

            @Override
            protected ViewHolder<UserCard> onCreateViewHolder(View root, int viewType) {
                return new SearchUserFragment.ViewHolder(root);
            }
        });
        //初始化占位布局
        ev_empty.bind(rv_user);
        setPlaceHolderView(ev_empty);
    }

    @Override
    protected void initData() {
        super.initData();
        //Fragment初始化完成后即发起首次搜索
        search("");
    }

    @Override
    public void search(String content) {
        //Activity->Fragment->Presenter->Network
        presenter.search(content);
    }

    @Override
    protected SearchContact.Presenter initPresenter() {
        return new SearchUserPresenter(this);
    }

    @Override
    public void onSearchDone(List<UserCard> userCards) {
        //成功的情况下返回数据
        adapter.replace(userCards);
        //如果有数据，就展示数据，没有数据就显示空布局
        placeHolderView.triggerOkOrEmpty(adapter.getItemCount() > 0);
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<UserCard> implements FollowContract.View {
        @BindView(R.id.iv_avatar)
        PortraitView iv_avatar;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.iv_follow)
        ImageView iv_follow;

        private FollowContract.Presenter presenter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //将当前view和presenter绑定
            new FollowPresenter(this);
        }

        @Override
        protected void onBind(UserCard userCard) {
            iv_avatar.setup(Glide.with(getContext()), userCard);
            tv_name.setText(userCard.getName());
            iv_follow.setEnabled(!userCard.isFollow());
        }

        @OnClick(R.id.iv_follow)
        void onFollowClick() {
            // 发起关注
            presenter.follow(mData.getId());
        }

        @Override
        public void onFollowSucceed(UserCard userCard) {
            // 更改当前界面状态
            if (iv_follow.getDrawable() instanceof LoadingDrawable) {
                ((LoadingDrawable) iv_follow.getDrawable()).stop();
                // 设置为默认的
                iv_follow.setImageResource(R.drawable.sel_opt_done_add);
            }
            // 发起更新
            updateData(userCard);
        }

        @Override
        public void showError(int str) {
            // 更改当前界面状态
            if (iv_follow.getDrawable() instanceof LoadingDrawable) {
                // 失败则停止动画，并且显示一个圆圈
                LoadingDrawable drawable = (LoadingDrawable) iv_follow.getDrawable();
                drawable.setProgress(1);
                drawable.stop();
            }
        }

        @Override
        public void showLoading() {
            int minSize = (int) Ui.dipToPx(getResources(), 22);
            int maxSize = (int) Ui.dipToPx(getResources(), 30);
            // 初始化一个圆形的动画的Drawable
            LoadingDrawable drawable = new LoadingCircleDrawable(minSize, maxSize);
            drawable.setBackgroundColor(0);

            int[] color = new int[]{UiCompat.getColor(getResources(), R.color.white_alpha_208)};
            drawable.setForegroundColor(color);
            // 设置进去
            iv_follow.setImageDrawable(drawable);
            // 启动动画
            drawable.start();
        }

        @Override
        public void setPresenter(FollowContract.Presenter presenter) {
            this.presenter = presenter;
        }
    }
}
