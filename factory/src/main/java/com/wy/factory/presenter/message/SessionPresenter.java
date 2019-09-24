package com.wy.factory.presenter.message;

import androidx.recyclerview.widget.DiffUtil;

import com.wy.factory.data.message.SessionDataSource;
import com.wy.factory.data.message.SessionRepository;
import com.wy.factory.model.db.Session;
import com.wy.factory.presenter.BaseSourcePresenter;
import com.wy.factory.utils.DiffUiDataCallback;

import java.util.List;

/* 名称: ITalker.com.wy.factory.presenter.message.SessionPresenter
 * 用户: _VIEW
 * 时间: 2019/9/24,20:05
 * 描述: 最近聊天列表的Presenter
 */
public class SessionPresenter extends BaseSourcePresenter<Session, Session, SessionContract.View, SessionDataSource>
        implements SessionContract.Presenter {

    public SessionPresenter(SessionContract.View mView) {
        super(new SessionRepository(), mView);
    }

    @Override
    public void onDataLoad(List<Session> sessions) {
        SessionContract.View view = getView();
        if (view == null)
            return;
        List<Session> old = view.getRecyclerAdapter().getItems();
        DiffUiDataCallback<Session> callback = new DiffUiDataCallback<>(old, sessions);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(callback);
        refreshData(result, sessions);
    }
}
