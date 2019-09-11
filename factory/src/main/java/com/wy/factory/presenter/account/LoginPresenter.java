package com.wy.factory.presenter.account;

import android.text.TextUtils;

import com.wy.common.factory.data.DataSource;
import com.wy.common.factory.presenter.BasePresenter;
import com.wy.factory.R;
import com.wy.factory.data.helper.AccountHelper;
import com.wy.factory.model.api.account.LoginModel;
import com.wy.factory.model.db.User;
import com.wy.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

/* 名称: ITalker.com.wy.factory.presenter.account.LoginPresenter
 * 用户: _VIEW
 * 时间: 2019/9/11,12:28
 * 描述: LoginPresenter 登录的逻辑实现
 */
public class LoginPresenter extends BasePresenter<LoginContract.View> implements LoginContract.Presenter,
        DataSource.Callback<User> {
    public LoginPresenter(LoginContract.View mView) {
        super(mView);
    }

    @Override
    public void login(String phone, String password) {
        start();
        final LoginContract.View view = getView();
        if (!TextUtils.isEmpty(phone) || !TextUtils.isEmpty(password) || checkMobile(phone)) {
            view.showError(R.string.data_account_login_invalid_parameter);
        } else {
            //尝试获取PushId
            LoginModel loginModel = new LoginModel(phone, password, Account.getPushId());
            AccountHelper.login(loginModel, this);
        }

    }

    @Override
    public boolean checkMobile(String phone) {
        return false;
    }

    @Override
    public void onDataLoad(User user) {
        //告知界面登录成功
        final LoginContract.View view = getView();
        if (view == null)
            return;
        //该方法是从网络回调的，需要回主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.loginSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final LoginContract.View view = getView();
        if (view == null)
            return;
        //该方法是从网络回调的，需要回主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                //告知界面注册失败，显示错误
                view.showError(strRes);
            }
        });
    }
}
