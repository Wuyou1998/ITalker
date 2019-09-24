package com.wy.factory.presenter.message;

import com.wy.common.factory.presenter.BaseContract;
import com.wy.factory.model.db.Session;

/* 名称: ITalker.com.wy.factory.presenter.message.SessionContract
 * 用户: _VIEW
 * 时间: 2019/9/24,19:31
 * 描述: 消息列表契约
 */
public interface SessionContract {
    //无需再额外定义，开始即为调用start即可
    interface Presenter extends BaseContract.Presenter {

    }

    //基类已经完成全部功能
    interface View extends BaseContract.RecyclerView<Session, Presenter> {

    }
}
