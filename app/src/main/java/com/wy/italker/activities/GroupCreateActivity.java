package com.wy.italker.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.wy.common.app.Application;
import com.wy.common.app.PresenterToolbarActivity;
import com.wy.common.widget.PortraitView;
import com.wy.common.widget.recycler.RecyclerAdapter;
import com.wy.factory.presenter.group.GroupCreateContract;
import com.wy.factory.presenter.group.GroupCreatePresenter;
import com.wy.italker.R;
import com.wy.italker.fragments.media.GalleryFragment;
import com.yalantis.ucrop.UCrop;

import java.io.File;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class GroupCreateActivity extends PresenterToolbarActivity<GroupCreateContract.Presenter>
        implements GroupCreateContract.View {
    @BindView(R.id.rv_members)
    RecyclerView rv_members;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edt_desc)
    EditText edt_desc;
    @BindView(R.id.iv_avatar)
    PortraitView iv_avatar;

    private Adapter adapter;
    private String avatarUrl;

    public static void show(Context context) {
        context.startActivity(new Intent(context, GroupCreateActivity.class));
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_group_create;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("");
        rv_members.setLayoutManager(new LinearLayoutManager(this));
        rv_members.setAdapter(adapter = new Adapter());
    }

    @Override
    protected void initData() {
        super.initData();
        mPresenter.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.group_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_create) {
            //进行群的创建
            onCreateClick();
        }
        return super.onOptionsItemSelected(item);
    }

    //进行创建操作
    private void onCreateClick() {
        hideSoftKeyboard();
        String name = edt_name.getText().toString().trim();
        String desc = edt_desc.getText().toString().trim();
        mPresenter.create(name, desc, avatarUrl);
    }

    @Override
    protected GroupCreateContract.Presenter initPresenter() {
        return new GroupCreatePresenter(this);
    }

    //隐藏软键盘
    private void hideSoftKeyboard() {
        //当前焦点的view
        View view = getCurrentFocus();
        if (view == null)
            return;
        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        assert methodManager != null;
        methodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onCreateSucceed() {
        //提示成功
        hideLoading();
        Application.showToast(R.string.label_group_create_succeed);
    }

    @Override
    public RecyclerAdapter<GroupCreateContract.ViewModel> getRecyclerAdapter() {
        return adapter;
    }

    @Override
    public void onAdapterDataChanged() {
        hideLoading();
    }

    private class Adapter extends RecyclerAdapter<GroupCreateContract.ViewModel> {

        @Override
        protected int getItemViewType(int position, GroupCreateContract.ViewModel viewModel) {
            return R.layout.cell_group_create_contact;
        }

        @Override
        protected ViewHolder<GroupCreateContract.ViewModel> onCreateViewHolder(View root, int viewType) {
            return new GroupCreateActivity.ViewHolder(root);
        }
    }

    class ViewHolder extends RecyclerAdapter.ViewHolder<GroupCreateContract.ViewModel> {
        @BindView(R.id.iv_avatar)
        PortraitView iv_avatar;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.cb_select)
        CheckBox cb_select;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(GroupCreateContract.ViewModel viewModel) {
            iv_avatar.setup(Glide.with(GroupCreateActivity.this), viewModel.author);
            tv_name.setText(viewModel.author.getName());
            cb_select.setChecked(viewModel.isSelected);
        }

        @OnCheckedChanged(R.id.cb_select)
        void onCheckedChanged(boolean checked) {
            //进行状态更改
            mPresenter.changeSelected(mData, checked);
        }
    }

    @OnClick(R.id.iv_avatar)
    void onAvatarClick() {
        hideSoftKeyboard();
        new GalleryFragment()
                .setOnSelectedListener((path -> {
                    UCrop.Options options = new UCrop.Options();
                    //设置图片处理的格式 JPEG
                    options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
                    //设置压缩后的图片精度
                    options.setCompressionQuality(96);
                    //设置状态栏颜色，新版的uCrop状态栏颜色为白色导致看不清文字
                    options.setStatusBarColor(getColor(R.color.colorDark));
                    //得到头像缓存地址
                    File dPath = Application.getAvatarTempFile();
                    //发起剪切
                    UCrop.of(Uri.fromFile(new File(path)), Uri.fromFile(dPath))
                            .withAspectRatio(1, 1)//1:1比例
                            .withMaxResultSize(512, 512)//512*512大小
                            .withOptions(options)//相关参数
                            .start(this);//启动

                }))//show的时候建议使用getChildFragmentManager()，tag即为class名字
                .show(getSupportFragmentManager(), GalleryFragment.class.getName());
    }

    //收到从Activity传过来的回调，取出其中的值进行图片加载
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //是当前fragment能够处理的类型
        super.onActivityResult(requestCode, resultCode, data);
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
        Glide.with(this).load(uri).asBitmap().centerCrop().into(iv_avatar);
    }


}
