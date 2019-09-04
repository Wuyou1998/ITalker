package com.wy.italker.fragments.mian;


import android.view.View;

import androidx.loader.app.LoaderManager;

import com.wy.common.app.BaseFragment;
import com.wy.common.widget.GalleryView;
import com.wy.italker.R;

import butterknife.BindView;


public class ActiveFragment extends BaseFragment {
    @BindView(R.id.gv_gallery)
    GalleryView gv_gallery;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_active;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
    }

    @Override
    protected void initData() {
        super.initData();
        gv_gallery.setUp(LoaderManager.getInstance(this), new GalleryView.SelectedChangeListener() {
            @Override
            public void onSelectedCountChanged(int count) {

            }
        });
    }
}
