package com.wy.factory.presenter.group;

import com.wy.common.factory.model.Author;
import com.wy.common.factory.presenter.BaseContract;

/* 名称: ITalker.com.wy.factory.presenter.group.GroupCreateContract
 * 用户: _VIEW
 * 时间: 2019/9/25,23:44
 * 描述: 创建群的契约
 */
public interface GroupCreateContract {
    interface Presenter extends BaseContract.Presenter {
        //创建
        void create(String name, String desc, String picture);

        //更改一个model的选中状态
        void changeSelected(ViewModel model, boolean isSelected);
    }

    interface View extends BaseContract.RecyclerView<ViewModel, Presenter> {
        //创建成功
        void onCreateSucceed();
    }

    class ViewModel {
        //用户信息
        public Author author;
        //是否选中
       public boolean isSelected;
    }
}
