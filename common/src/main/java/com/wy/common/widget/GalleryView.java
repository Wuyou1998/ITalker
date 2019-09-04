package com.wy.common.widget;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.wy.common.R;
import com.wy.common.widget.recycler.RecyclerAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;


public class GalleryView extends RecyclerView {
    public static final int LOAD_ID = 0x0100;
    public static final int MAX_IMAGE_COUNT = 3;//最大选中图片数量
    public static final int MIN_IMAGE_FILE_SIZE = 10 * 1024;//10kb
    private Adapter adapter = new Adapter();
    private LoaderCallback loaderCallback = new LoaderCallback();
    private List<ImageInfo> imageInfoSelectedList = new LinkedList<>();
    private SelectedChangeListener selectedChangeListener;

    public GalleryView(Context context) {
        super(context);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new GridLayoutManager(getContext(), 4));
        setAdapter(adapter);
        adapter.setAdapterListener(new RecyclerAdapter.AdapterListenerImpl<ImageInfo>() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder viewHolder, ImageInfo imageInfo) {
                //如果cell的点击是允许的，更新cell的状态，然后更新界面
                if (onItemSelect(imageInfo)) {
                    //noinspection unchecked
                    viewHolder.updateData(imageInfo);
                }
            }
        });
    }

    /**
     * loader初始化
     *
     * @param loaderManager loader管理器
     * @return loader id,可用于销毁loader
     */
    public int setUp(LoaderManager loaderManager, SelectedChangeListener selectedChangeListener) {
        loaderManager.initLoader(LOAD_ID, null, loaderCallback);
        this.selectedChangeListener = selectedChangeListener;
        return LOAD_ID;
    }

    /**
     * 得到选中图片的全部地址
     *
     * @return 返回地址数组
     */
    public String[] getSelectedPath() {
        String[] paths = new String[imageInfoSelectedList.size()];
        int index = 0;
        for (ImageInfo image : imageInfoSelectedList) {
            paths[index++] = image.path;
        }
        return paths;
    }

    public void clear() {
        for (ImageInfo image : imageInfoSelectedList) {
            //先重置状态，再重置更新
            image.isSelected = false;
        }
        imageInfoSelectedList.clear();
        adapter.notifyDataSetChanged();
    }

    /**
     * 点击的具体逻辑
     *
     * @param imageInfo image
     * @return true 进行了数据更改，需要刷新，反之则反
     */
    private boolean onItemSelect(ImageInfo imageInfo) {
        //是否需要刷新
        boolean notifyRefresh;
        if (imageInfoSelectedList.contains(imageInfo)) {
            //如果之前选中那么就移除
            imageInfoSelectedList.remove(imageInfo);
            imageInfo.isSelected = false;
            //状态改变需要更新
            notifyRefresh = true;
        } else {
            if (imageInfoSelectedList.size() >= MAX_IMAGE_COUNT) {
                //得到提示文字
                String str = getResources().getString(R.string.label_gallery_select_max_size);
                //格式化填充
                str = String.format(str, MAX_IMAGE_COUNT);
                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                notifyRefresh = false;
            } else {
                imageInfoSelectedList.add(imageInfo);
                imageInfo.isSelected = true;
                notifyRefresh = true;
            }
        }
        //如果数据有变，通知外界监听者数据变化了
        if (notifyRefresh)
            notifySelectChanged();
        return notifyRefresh;
    }

    /**
     * 通知选中状态改变
     */
    private void notifySelectChanged() {
        //判断监听器是否存在，进行回调数量变化
        if (selectedChangeListener != null) {
            selectedChangeListener.onSelectedCountChanged(imageInfoSelectedList.size());
        }
    }

    /**
     * 通知adapter数据更改
     *
     * @param list 新的数据集
     */
    private void updateSource(List<ImageInfo> list) {
        adapter.replace(list);
    }

    /**
     * 用于实际的数据加载的loader
     */
    private class LoaderCallback implements LoaderManager.LoaderCallbacks<Cursor> {
        final String[] IMAGE_PROJECTION = new String[]{
                MediaStore.Images.Media._ID,//id
                MediaStore.Images.Media.DATA,//图片路径
                MediaStore.Images.Media.DATE_ADDED//创建日期
        };

        @NonNull
        @Override
        public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
            //创建一个loader
            if (id == LOAD_ID) {
                //如果是内置的id则可以进行初始化
                return new CursorLoader(getContext(),
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        IMAGE_PROJECTION,
                        null,
                        null,
                        IMAGE_PROJECTION[2] + " DESC");//按时间倒序排列
            }
            return null;
        }

        @Override
        public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
            //当loader加载完成时
            List<ImageInfo> imageInfoList = new ArrayList<>();
            //判断是否有数据
            if (data != null) {
                int count = data.getCount();
                if (count > 0) {
                    //移动游标到开始
                    data.moveToFirst();
                    //拿到对应列的坐标
                    int indexId = data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]);
                    int indexPath = data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]);
                    int indexDate = data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]);
                    do {
                        int id = data.getInt(indexId);
                        String path = data.getString(indexPath);
                        long dateTime = data.getLong(indexDate);
                        File file = new File(path);
                        if (!file.exists() || file.length() < MIN_IMAGE_FILE_SIZE) {
                            //图片不存在或者图片太小了，跳过
                            continue;
                        }
                        ImageInfo info = new ImageInfo();
                        info.id = id;
                        info.path = path;
                        info.date = dateTime;
                        //添加一条新数据
                        imageInfoList.add(info);
                    } while (data.moveToNext());
                }
            }
            updateSource(imageInfoList);
        }

        @Override
        public void onLoaderReset(@NonNull Loader<Cursor> loader) {
            //当loader销毁或重置,界面清空
            updateSource(null);
        }
    }

    /**
     * 内部数据结构
     */
    private static class ImageInfo {
        int id;//数据id
        String path;//图片路径
        long date;//图片创建日期
        boolean isSelected;//是否选中

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ImageInfo imageInfo = (ImageInfo) o;
            return Objects.equals(path, imageInfo.path);
        }

        @Override
        public int hashCode() {
            return Objects.hash(path);
        }
    }

    private class Adapter extends RecyclerAdapter<ImageInfo> {

        @Override
        protected int getItemViewType(int position, ImageInfo imageInfo) {
            return R.layout.cell_gallery;
        }

        @Override
        protected ViewHolder<ImageInfo> onCreateViewHolder(android.view.View root, int viewType) {
            return new GalleryView.ViewHolder(root);
        }

    }

    /**
     * cell对应的holder
     */
    private class ViewHolder extends RecyclerAdapter.ViewHolder<ImageInfo> {
        ImageView iv_image;
        View v_shade;
        CheckBox cb_select;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_image = itemView.findViewById(R.id.iv_image);
            v_shade = itemView.findViewById(R.id.v_shade);
            cb_select = itemView.findViewById(R.id.cb_select);
        }

        @Override
        protected void onBind(ImageInfo imageInfo) {
            Glide.with(getContext())
                    .load(imageInfo.path)//加载路径
                    .centerCrop()//居中剪切
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//不使用缓存
                    .placeholder(R.color.grey_200)//默认颜色
                    .into(iv_image);
            v_shade.setVisibility(imageInfo.isSelected ? VISIBLE : GONE);
            cb_select.setVisibility(imageInfo.isSelected ? VISIBLE : GONE);
            cb_select.setChecked(imageInfo.isSelected);
        }
    }

    /**
     * 对外的监听器
     */
    public interface SelectedChangeListener {
        void onSelectedCountChanged(int count);
    }

}
