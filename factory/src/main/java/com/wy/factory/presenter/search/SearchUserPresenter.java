package com.wy.factory.presenter.search;

import com.wy.common.factory.data.DataSource;
import com.wy.common.factory.presenter.BasePresenter;
import com.wy.factory.data.helper.UserHelper;
import com.wy.factory.model.card.UserCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/* 名称: ITalker.com.wy.factory.presenter.search.SearchUserPresenter
 * 用户: _VIEW
 * 时间: 2019/9/13,22:55
 * 描述: searchActivity 搜索人的实现
 */
public class SearchUserPresenter extends BasePresenter<SearchContact.UserView>
        implements SearchContact.Presenter, DataSource.Callback<List<UserCard>> {
    private Call searchCall;

    public SearchUserPresenter(SearchContact.UserView mView) {
        super(mView);
    }

    @Override
    public void search(String content) {
        start();
        Call call = searchCall;
        if (call != null && !call.isCanceled()) {
            //如果有上一次的请求，并且没有取消，则调用取消
            call.cancel();
        }
        //之后重新请求这次的
        searchCall = UserHelper.search(content, this);
    }

    @Override
    public void onDataLoad(final List<UserCard> userCards) {
        final SearchContact.UserView userView = getView();
        if (userView != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    userView.onSearchDone(userCards);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        //搜索失败了
        final SearchContact.UserView userView = getView();
        if (userView != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    userView.showError(strRes);
                }
            });
        }
    }
}
