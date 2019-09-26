package com.wy.factory.presenter.search;

import com.wy.common.factory.data.DataSource;
import com.wy.common.factory.presenter.BasePresenter;
import com.wy.factory.data.helper.GroupHelper;
import com.wy.factory.model.card.GroupCard;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.List;

import retrofit2.Call;

/* 名称: ITalker.com.wy.factory.presenter.search.SearchGroupPresenter
 * 用户: _VIEW
 * 时间: 2019/9/13,22:59
 * 描述: searchActivity 搜索群的实现
 */
public class SearchGroupPresenter extends BasePresenter<SearchContact.GroupView>
        implements SearchContact.Presenter, DataSource.Callback<List<GroupCard>> {
    private Call searchCall;

    public SearchGroupPresenter(SearchContact.GroupView mView) {
        super(mView);
    }

    @Override
    public void search(String content) {
        Call call = searchCall;
        if (call != null && !call.isCanceled()) {
            //如果有上一次的请求，并且没有取消，则调用取消
            call.cancel();
        }
        //之后重新请求这次的
        searchCall = GroupHelper.search(content, this);
    }


    @Override
    public void onDataLoad(final List<GroupCard> groupCards) {
        final SearchContact.GroupView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.onSearchDone(groupCards);
                }
            });
        }
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        //搜索失败了
        final SearchContact.GroupView view = getView();
        if (view != null) {
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    view.showError(strRes);
                }
            });
        }
    }
}
