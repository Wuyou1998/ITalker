package com.wy.factory.presenter.account;

import android.text.TextUtils;

import com.wy.common.Common;
import com.wy.common.factory.data.DataSource;
import com.wy.common.factory.presenter.BasePresenter;
import com.wy.factory.R;
import com.wy.factory.data.helper.AccountHelper;
import com.wy.factory.model.api.account.RegisterModel;
import com.wy.factory.model.db.User;
import com.wy.factory.persistence.Account;

import net.qiujuer.genius.kit.handler.Run;
import net.qiujuer.genius.kit.handler.runable.Action;

import java.util.regex.Pattern;

/* 名称: ITalker.com.wy.factory.presenter.account.RegisterPresenter
 * 用户: _VIEW
 * 时间: 2019/9/9,15:57
 * 描述: 实现类
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.View>
        implements RegisterContract.Presenter, DataSource.Callback<User> {
    public RegisterPresenter(RegisterContract.View mView) {
        super(mView);
    }

    @Override
    public void register(String phone, String name, String password) {
        start();
        //得到View接口
        RegisterContract.View view = getView();
        if (!checkMobile(phone)) {
            //提示
            view.showError(R.string.data_account_register_invalid_parameter_mobile);
        } else if (name.length() < 2) {
            //姓名需要大于两位
            view.showError(R.string.data_account_register_invalid_parameter_name);
        } else if (password.length() < 6) {
            //密码需要大于6位
            view.showError(R.string.data_account_register_invalid_parameter_password);
        } else {
            //进行网络请求

            //构造model，进行请求调用
            RegisterModel registerModel = new RegisterModel(phone, password, name, Account.getPushId());
            //进行网络请求并设置回调接口为自己
            AccountHelper.register(registerModel, this);
        }
    }

    /**
     * 检查手机号码是否合法
     *
     * @param phone 手机号
     * @return true 合法
     */
    @Override
    public boolean checkMobile(String phone) {
        //手机号不为空，并且满足格式
        return !TextUtils.isEmpty(phone) && Pattern.matches(Common.REGEX_MOBILE, phone);
    }

    /**
     * 当网络请求成功，注册好了，回调
     *
     * @param user 回送一个用户信息回来
     */
    @Override
    public void onDataLoad(User user) {
        //告知界面注册成功
        final RegisterContract.View view = getView();
        if (view == null)
            return;
        //该方法是从网络回调的，需要回主线程更新UI
        Run.onUiAsync(new Action() {
            @Override
            public void call() {
                view.registerSuccess();
            }
        });
    }

    @Override
    public void onDataNotAvailable(final int strRes) {
        final RegisterContract.View view = getView();
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
