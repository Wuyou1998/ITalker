package com.wy.italker.fragments.account;


import android.content.Context;
import android.widget.EditText;

import com.wy.common.app.PresenterFragment;
import com.wy.factory.presenter.account.RegisterContract;
import com.wy.factory.presenter.account.RegisterPresenter;
import com.wy.italker.R;
import com.wy.italker.activities.MainActivity;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.Loading;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注册
 */
public class RegisterFragment extends PresenterFragment<RegisterContract.Presenter> implements RegisterContract.View {
    private AccountTrigger accountTrigger;
    @BindView(R.id.edt_phone)
    EditText edt_phone;
    @BindView(R.id.edt_password)
    EditText edt_password;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.loading)
    Loading loading;
    @BindView(R.id.btn_submit)
    Button btn_submit;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_register;
    }

    @Override
    protected RegisterContract.Presenter initPresenter() {
        return new RegisterPresenter(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //拿到对Activity的引用
        accountTrigger = (AccountTrigger) context;
    }


    @Override
    public void registerSuccess() {
        //注册成功，账号已经登录，此时需要跳转到MainActivity
        MainActivity.show(Objects.requireNonNull(getContext()));
        Objects.requireNonNull(getActivity()).finish();
    }

    @Override
    public void showLoading() {
        super.showLoading();
        //正在进行注册，界面不可操作
        loading.start();
        //让控件不可以输入
        edt_password.setEnabled(false);
        edt_phone.setEnabled(false);
        edt_name.setEnabled(false);
        //注册按钮不可以继续点击
        btn_submit.setEnabled(false);
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        //当需要显示错误的时候触发，一定是结束了

        loading.stop();
        //让控件可以输入
        edt_password.setEnabled(true);
        edt_phone.setEnabled(true);
        edt_name.setEnabled(true);
        //注册按钮可以继续点击
        btn_submit.setEnabled(true);
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String phone = edt_phone.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        String name = edt_name.getText().toString().trim();
        //调用P层进行注册 com/wy/factory/presenter/account/RegisterPresenter.java
        presenter.register(phone, name, password);
    }

    @OnClick(R.id.tv_go_login)
    void onShowLoginClick() {
        accountTrigger.triggerView();
    }
}
