package com.wy.factory.presenter.search;

import com.wy.common.factory.presenter.BasePresenter;

/* 名称: ITalker.com.wy.factory.presenter.search.SearchGroupPresenter
 * 用户: _VIEW
 * 时间: 2019/9/13,22:59
 * 描述: searchActivity 搜索群的实现
 */
public class SearchGroupPresenter extends BasePresenter<SearchContact.GroupView>
        implements SearchContact.Presenter {
    public SearchGroupPresenter(SearchContact.GroupView mView) {
        super(mView);
    }

    @Override
    public void search(String content) {

    }
}
