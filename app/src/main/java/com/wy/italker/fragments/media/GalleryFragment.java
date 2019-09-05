package com.wy.italker.fragments.media;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.wy.common.app.BaseFragment;
import com.wy.common.tools.UiTool;
import com.wy.common.widget.GalleryView;
import com.wy.italker.R;

import net.qiujuer.genius.ui.Ui;

import java.util.Objects;

/* 名称: ITalker.com.wy.italker.fragments.media.GalleryFragment
 * 用户: _VIEW
 * 时间: 2019/9/4,13:50
 * 描述: 图片选择器 fragment
 */
public class GalleryFragment extends BottomSheetDialogFragment implements GalleryView.SelectedChangeListener {
    private GalleryView galleryView;
    private OnSelectedListener onSelectedListener;

    public GalleryFragment() {
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(Objects.requireNonNull(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        final Context context = Objects.requireNonNull(getActivity());
//        final FrameLayout frameLayout = new FrameLayout(context);
//
//        inflater.inflate(R.layout.fragment_gallery, frameLayout, true);
//        galleryView = frameLayout.findViewById(R.id.gv_gallery);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        galleryView = root.findViewById(R.id.gv_gallery);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        galleryView.setUp(LoaderManager.getInstance(this), this);
    }

    @Override
    public void onSelectedCountChanged(int count) {
        //选中一张图片
        if (count > 0)
            //隐藏
            dismiss();
        if (onSelectedListener != null) {
            //得到所有选中图片的路径
            String[] paths = galleryView.getSelectedPath();
            //返回第一张图片路径
            onSelectedListener.onSelectedImage(paths[0]);
            //取消和唤起者之间的引用，加快内存回收
            onSelectedListener = null;
        }

    }

    /**
     * 设置事件监听 返回自己
     *
     * @param onSelectedListener 监听器
     * @return 自己本身
     */
    public GalleryFragment setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.onSelectedListener = onSelectedListener;
        return this;
    }

    /**
     * 选中图片的监听器
     */
    public interface OnSelectedListener {
        void onSelectedImage(String path);
    }

   public static class TransStatusBottomSheetDialog extends BottomSheetDialog {

        public TransStatusBottomSheetDialog(@NonNull Context context) {
            super(context);
        }

        public TransStatusBottomSheetDialog(@NonNull Context context, int theme) {
            super(context, theme);
        }

        protected TransStatusBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            final Window window = getWindow();
            if (window == null)
                return;
            Activity ownerActivity = getOwnerActivity();
            if (ownerActivity == null) {
                return;
            }
            //得到屏幕高度
            int screenHeight = UiTool.getScreenHeight(ownerActivity);
            //得到状态栏高度
            int statusHeight = UiTool.getStatusBarHeight(ownerActivity);
            //dialog 高度
            int dialogHeight = screenHeight - statusHeight;
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight <= 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);

        }
    }
}
