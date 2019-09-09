package com.wy.factory.presenter.account;

import android.text.TextUtils;

import com.wy.common.Common;
import com.wy.common.factory.presenter.BasePresenter;
import com.wy.factory.data.helper.AccountHelper;
import com.wy.factory.model.api.RegisterModel;

import java.util.regex.Pattern;

/* 名称: ITalker.com.wy.factory.presenter.account.RegisterPresenter
 * 用户: _VIEW
 * 时间: 2019/9/9,15:57
 * 描述: 实现类
 */
public class RegisterPresenter extends BasePresenter<RegisterContract.View> implements RegisterContract.Presenter {
    public RegisterPresenter(RegisterContract.View mView) {
        super(mView);
    }

    @Override
    public void register(String phone, String name, String password) {
        start();
        if (!checkMobile(phone)) {
            //提示
        } else if (name.length() < 2) {
            //姓名需要大于两位
        } else if (password.length() < 6) {
            //密码需要大于6位
        } else {
            //进行网络请求

            //构造model，进行请求调用
            RegisterModel registerModel = new RegisterModel(phone, password, name);
            AccountHelper.register(registerModel);
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
        return !TextUtils.isEmpty(phone) && Pattern.matches(Common.REGEX_MOBILE, phone);
    }

}
