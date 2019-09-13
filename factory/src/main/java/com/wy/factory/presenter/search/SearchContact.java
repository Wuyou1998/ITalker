package com.wy.factory.presenter.search;

import com.wy.common.factory.presenter.BaseContract;
import com.wy.factory.model.card.GroupCard;
import com.wy.factory.model.card.UserCard;

import java.util.List;

/* 名称: ITalker.com.wy.factory.presenter.search.SearchContact
 * 用户: _VIEW
 * 时间: 2019/9/13,22:42
 * 描述: searchActivity mvp接口
 */
public interface SearchContact {
    interface Presenter extends BaseContract.Presenter {
        //搜索内容
        void search(String content);
    }

    //搜索人的界面
    interface UserView extends BaseContract.View<Presenter> {
        void onSearchDone(List<UserCard> userCards);
    }

    //搜索群的界面
    interface GroupView extends BaseContract.View<Presenter> {
        void onSearchDone(List<GroupCard> groupCards);
    }
}
