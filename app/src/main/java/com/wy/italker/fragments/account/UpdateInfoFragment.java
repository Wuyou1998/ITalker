package com.wy.italker.fragments.account;


import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.wy.common.app.Application;
import com.wy.common.app.BaseFragment;
import com.wy.common.widget.PortraitView;
import com.wy.italker.R;
import com.wy.italker.fragments.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

/**
 * 更新账户信息的界面
 */
public class UpdateInfoFragment extends BaseFragment {
    @BindView(R.id.iv_avatar)
    PortraitView iv_avatar;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_update_info;
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
                    options.setStatusBarColor(getContext().getColor(R.color.colorDark));
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
            final Throwable cropError = UCrop.getError(data);
        }
    }

    /**
     * 加载uri到头像中
     *
     * @param uri 图片uri
     */
    private void loadAvatar(Uri uri) {
        Glide.with(getContext()).load(uri).asBitmap().centerCrop().into(iv_avatar);
    }
}
