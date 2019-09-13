package com.wy.italker.fragments.search;


import com.wy.common.app.PresenterFragment;
import com.wy.factory.model.card.GroupCard;
import com.wy.factory.presenter.search.SearchContact;
import com.wy.factory.presenter.search.SearchGroupPresenter;
import com.wy.italker.R;
import com.wy.italker.activities.SearchActivity;

import java.util.List;


public class SearchGroupFragment extends PresenterFragment<SearchContact.Presenter>
        implements SearchActivity.SearchFragment, SearchContact.GroupView {


    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_search_group;
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

    }
}
