package com.wy.italker.fragments.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wy.common.app.Application;
import com.wy.common.app.BaseFragment;
import com.wy.italker.R;
import com.wy.italker.fragments.media.GalleryFragment;

import java.util.List;
import java.util.Objects;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;

/**
 * 权限申请弹出框
 */
public class PermissionsFragment extends BottomSheetDialogFragment implements EasyPermissions.PermissionCallbacks {
    //权限回调标识
    private static final int RC = 0x0100;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(Objects.requireNonNull(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_permissions, container, false);
        //button
        root.findViewById(R.id.btn_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //点击后申请权限
                requestPerm();
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshState(Objects.requireNonNull(getView()));
    }

    /**
     * 刷新布局中的图片状态
     *
     * @param root 根视图
     */
    private void refreshState(View root) {
        if (root == null)
            return;
        Context context = getContext();
        root.findViewById(R.id.iv_state_permission_network)
                .setVisibility(haveNetWorkPerm(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.iv_state_permission_read)
                .setVisibility(haveReadPerm(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.iv_state_permission_write)
                .setVisibility(haveWritePerm(context) ? View.VISIBLE : View.GONE);
        root.findViewById(R.id.iv_state_permission_record_audio)
                .setVisibility(haveRecordAudioPerm(context) ? View.VISIBLE : View.GONE);
    }

    private static void show(FragmentManager manager) {
        new PermissionsFragment().show(manager, PermissionsFragment.class.getName());

    }

    /**
     * 是否具有全部权限
     *
     * @param context Context
     * @param manager FragmentManager
     * @return true 全部都有了
     */
    public static boolean haveAll(Context context, FragmentManager manager) {
        //检查是否具有全部权限
        boolean haveAll = haveNetWorkPerm(context) && haveReadPerm(context) && haveWritePerm(context) && haveRecordAudioPerm(context);
        //如果没有则显示申请权限dialog
        if (!haveAll) {
            show(manager);
        }
        return haveAll;
    }

    /**
     * 申请权限方法
     */
    private void requestPerm() {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
        };
        if (EasyPermissions.hasPermissions(Objects.requireNonNull(getContext()), perms)) {
            //toast提示授权成功
            Application.showToast(R.string.label_permission_ok);
            //Fragment中调用getView获取根布局，前提是onCreateView之后
            refreshState(Objects.requireNonNull(getView()));
        } else {
            EasyPermissions.requestPermissions(new PermissionRequest.Builder(this, RC, perms)
                    .setRationale("为了程序正常运行，请授予相关权限")
                    .setTheme(R.style.AppTheme_Dialog_Alert_Light)
                    .build());
        }
    }

    /**
     * 是否有网络权限
     *
     * @param context 上下文
     * @return true 有
     */
    private static boolean haveNetWorkPerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE

        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 是否有读取权限
     *
     * @param context 上下文
     * @return true 有
     */
    private static boolean haveReadPerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE

        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 是否有写入权限
     *
     * @param context 上下文
     * @return true 有
     */
    private static boolean haveWritePerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 是否有录音权限
     *
     * @param context 上下文
     * @return true 有
     */
    private static boolean haveRecordAudioPerm(Context context) {
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO
        };
        return EasyPermissions.hasPermissions(context, perms);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //权限被永久拒绝后，引导用户去设置界面自己打开
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("未获取到权限")
                    .setRationale("您可以到设置中手动配置权限,点击确定前往设置")
                    .build().show();
        }
    }

    /**
     * 权限申请回调，我们将状态交给EasyPermissions框架
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //传递对应的参数进行回调，告知接收权限的处理者是自身
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
