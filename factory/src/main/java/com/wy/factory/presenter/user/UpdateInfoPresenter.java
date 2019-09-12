package com.wy.factory.presenter.user;

import android.text.TextUtils;

import com.wy.common.factory.data.DataSource;
import com.wy.common.factory.presenter.BasePresenter;
import com.wy.factory.Factory;
import com.wy.factory.R;
import com.wy.factory.data.helper.UserHelper;
import com.wy.factory.model.api.user.UserUpdateModel;
import com.wy.factory.model.card.UserCard;
import com.wy.factory.model.db.User;
import com.wy.factory.net.UploadHelper;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/* 名称: ITalker.com.wy.factory.presenter.user.UpdateInfoPresenter
 * 用户: _VIEW
 * 时间: 2019/9/12,15:07
 * 描述: UpdateInfoFragment 的 Presenter
 */
public class UpdateInfoPresenter extends BasePresenter<UpdateInfoContract.View>
        implements UpdateInfoContract.Presenter, DataSource.Callback<UserCard> {
    public UpdateInfoPresenter(UpdateInfoContract.View mView) {
        super(mView);
    }

    @Override
    public void update(final String photoFilePath, final String desc, final boolean isMan) {
        start();
        final UpdateInfoContract.View view = getView();
        if (TextUtils.isEmpty(photoFilePath) || TextUtils.isEmpty(desc)) {
            view.showError(R.string.data_account_login_invalid_parameter);
        } else {
            //上传头像
            Factory.runOnAsync(new Runnable() {
                @Override
                public void run() {
                    String url = UploadHelper.uploadAvatar(photoFilePath);
                    if (TextUtils.isEmpty(url)) {
                        //上传失败
                        view.showError(R.string.data_upload_error);
                    } else {
                        //构建model
                        UserUpdateModel model = new UserUpdateModel("", url, desc, isMan ? User.SEX_MAN : User.SEX_WOMAN);
                        //网络请求，上传
                        UserHelper.update(model, UpdateInfoPresenter.this);
                    }
                }
            });
        }
    }

    @Override
    public void onDataLoad(UserCard userCard) {
        //告知界面更新成功
        final UpdateInfoContract.View view = getView();
        if (view == null)
            return;
        //该方法是从网络回调的，需要回主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.updateSucceed();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final UpdateInfoContract.View view = getView();
        if (view == null)
            return;
        //该方法是从网络回调的，需要回主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //告知界面更新失败，显示错误
                view.showError(strRes);
            }
        });
    }
}
