package com.wy.factory.presenter.contact;

import com.wy.common.factory.presenter.BaseContract;
import com.wy.factory.model.db.User;

/* 名称: ITalker.com.wy.factory.presenter.contact.ContactContract
 * 用户: _VIEW
 * 时间: 2019/9/15,14:28
 * 描述: 联系人列表 契约接口
 */
public interface ContactContract {
    //什么都不需要额外定义，开始直接调用start即可
    interface Presenter extends BaseContract.Presenter {

    }

    //都在基类完成了
    interface View extends BaseContract.RecyclerView<User, Presenter> {

    }
}
