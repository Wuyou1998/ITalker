package com.wy.factory.presenter.group;

import android.text.TextUtils;

import com.wy.common.factory.data.DataSource;
import com.wy.common.factory.presenter.BaseRecyclerPresenter;
import com.wy.factory.Factory;
import com.wy.factory.R;
import com.wy.factory.data.helper.GroupHelper;
import com.wy.factory.data.helper.UserHelper;
import com.wy.factory.model.api.group.GroupCreateModel;
import com.wy.factory.model.card.GroupCard;
import com.wy.factory.model.db.view.UserSampleModel;
import com.wy.factory.net.UploadHelper;
import com.wy.factory.presenter.group.GroupCreateContract.Presenter;
import com.wy.factory.presenter.group.GroupCreateContract.View;
import com.wy.factory.presenter.group.GroupCreateContract.ViewModel;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/* 名称: ITalker.com.wy.factory.presenter.group.GroupCreatePresenter
 * 用户: _VIEW
 * 时间: 2019/9/26,10:06
 * 描述: 群创建界面的Presenter
 */
public class GroupCreatePresenter extends BaseRecyclerPresenter<View, ViewModel>
        implements Presenter, DataSource.Callback<GroupCard> {
    private Set<String> users = new HashSet<>();

    public GroupCreatePresenter(View mView) {
        super(mView);
    }

    @Override
    public void start() {
        super.start();
        //加载
        Factory.runOnAsync(loader);
    }

    @Override
    public void create(final String name, final String desc, final String picture) {
        View view = getView();
        view.showLoading();
        //判断参数
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc) || TextUtils.isEmpty(picture) || users.size() == 0) {
            view.showError(R.string.label_group_create_invalid);
        }

        //上传图片
        Factory.runOnAsync(new Runnable() {
            @Override
            public void run() {
                String url = uploadPicture(picture);
                if (TextUtils.isEmpty(url))
                    return;
                GroupCreateModel model = new GroupCreateModel(name, desc, url, users);
                GroupHelper.create(model, GroupCreatePresenter.this);
            }
        });
        //请求接口

        //处理回调
    }

    @Override
    public void changeSelected(ViewModel model, boolean isSelected) {
        if (isSelected) {
            users.add(model.author.getId());
        } else {
            users.remove(model.author.getId());
        }
    }

    private Runnable loader = new Runnable() {
        @Override
        public void run() {
            List<UserSampleModel> sampleModels = UserHelper.getSimpleContact();
            List<ViewModel> models = new ArrayList<>();
            for (UserSampleModel sampleModel : sampleModels) {
                ViewModel model = new ViewModel();
                model.author = sampleModel;
                models.add(model);
            }
            refreshData(models);
        }
    };

    private String uploadPicture(String path) {
        //同步上传
        String url = UploadHelper.uploadAvatar(path);
        if (TextUtils.isEmpty(url)) {
            //切换到UI线程，提示信息
            Run.onUiAsync(new Action() {
                @Override
                public void call() {
                    View view = getView();
                    if (view != null) {
                        view.showError(R.string.data_upload_error);
                    }
                }
            });
        }
        return url;
    }

    @Override
    public void onDataLoad(GroupCard groupCard) {
        //成功
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view = getView();
                if (view != null) {
                    view.onCreateSucceed();
                }
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        //失败
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                View view = getView();
                if (view != null) {
                    view.showError(strRes);
                }
            }
        });
    }
}
