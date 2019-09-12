package com.wy.italker.fragments.user;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wy.common.app.Application;
import com.wy.common.app.PresenterFragment;
import com.wy.common.widget.PortraitView;
import com.wy.factory.presenter.user.UpdateInfoContract;
import com.wy.factory.presenter.user.UpdateInfoPresenter;
import com.wy.italker.R;
import com.wy.italker.activities.MainActivity;
import com.wy.italker.fragments.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import net.qiujuer.genius.ui.widget.Button;
import net.qiujuer.genius.ui.widget.EditText;
import net.qiujuer.genius.ui.widget.Loading;

import java.io.File;
import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 更新账户信息的界面
 */
public class UpdateInfoFragment extends PresenterFragment<UpdateInfoContract.Presenter>
        implements UpdateInfoContract.View {
    private static final String TAG = "UpdateInfoFragment";

    @BindView(R.id.iv_avatar)
    PortraitView iv_avatar;

    @BindView(R.id.edt_desc)
    EditText edt_desc;

    @BindView(R.id.loading)
    Loading loading;

    @BindView(R.id.btn_submit)
    Button btn_submit;

    @BindView(R.id.iv_sex_man)
    ImageView iv_sex_man;

    private String avatarUrl;
    private boolean isMan = true;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
    }

    @Override
    protected UpdateInfoContract.Presenter initPresenter() {
        return new UpdateInfoPresenter(this);
    }

    @OnClick(R.id.iv_avatar)
    void onAvatarClick() {
        new GalleryFragment()
                .setOnSelectedListener((path -> {
                    UCrop.Options options = new UCrop.Options();
                    //设置图片处理的格式 JPEG
                    options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                    //设置压缩后的图片精度
                    options.setCompressionQuality(96);
                    //设置状态栏颜色，新版的uCrop状态栏颜色为白色导致看不清文字
                    options.setStatusBarColor(Objects.requireNonNull(getContext()).getColor(R.color.colorDark));
                    //得到头像缓存地址
                    File dPath = Application.getAvatarTempFile();
                    //发起剪切
                    UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                            .withAspectRatio(1, 1)//1:1比例
                            .withMaxResultSize(512, 512)//512*512大小
                            .withOptions(options)//相关参数
                            .start(getActivity());//启动

                }))//show的时候建议使用getChildFragmentManager()，tag即为class名字
                .show(getChildFragmentManager(), GalleryFragment.class.getName());
    }

    //收到从Activity传过来的回调，取出其中的值进行图片加载
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //是当前fragment能够处理的类型
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            //获取uri进行加载
            final Uri resultUri = UCrop.getOutput(data);
            if (resultUri != null) {
                loadAvatar(resultUri);
            }
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Application.showToast(R.string.data_rsp_error_unknown);
        }
    }

    /**
     * 加载uri到头像中
     *
     * @param uri 图片uri
     */
    private void loadAvatar(Uri uri) {

        //得到头像地址
        avatarUrl = uri.getPath();
        //拿到本地地址
        Glide.with(getContext()).load(uri).asBitmap().centerCrop().into(iv_avatar);
    }

    @Override
    public void updateSucceed() {
        //更新成功跳转到主界面
        MainActivity.show(Objects.requireNonNull(getActivity()));
        getActivity().finish();
    }

    @Override
    public void showError(int str) {
        super.showError(str);
        loading.stop();
        edt_desc.setEnabled(true);
        btn_submit.setEnabled(true);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        loading.start();
        edt_desc.setEnabled(false);
        btn_submit.setEnabled(false);
    }

    @OnClick(R.id.btn_submit)
    void onSubmitClick() {
        String desc = edt_desc.getText().toString().trim();
        //调用P层进行更新
        presenter.update(avatarUrl, desc, isMan);
    }

    @OnClick(R.id.iv_sex_man)
    void onSexClick() {
        isMan = !isMan;
        iv_sex_man.setImageResource(isMan ? R.drawable.ic_sex_man : R.drawable.ic_sex_woman);
        iv_sex_man.getBackground().setLevel(isMan ? 0 : 1);
    }

}
