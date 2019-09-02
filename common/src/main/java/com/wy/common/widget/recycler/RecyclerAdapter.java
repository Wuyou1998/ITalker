package com.wy.common.widget.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wy.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/* 名称: ITalker.com.wy.common.widget.recycler.RecyclerAdapter
 * 用户: _VIEW
 * 时间: 2019/9/2,14:06
 * 描述: RecyclerView.Adapter
 */
public abstract class RecyclerAdapter<Data> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener, AdapterCallback<Data> {
    private final List<Data> mDataList;
    private AdapterListener<Data> adapterListener;

    /**
     * 构造函数模块
     *
     * @param dataList        数据列表
     * @param adapterListener 监听器
     */
    public RecyclerAdapter(List<Data> dataList, AdapterListener<Data> adapterListener) {
        this.mDataList = dataList;
        this.adapterListener = adapterListener;
    }

    public RecyclerAdapter(AdapterListener<Data> adapterListener) {
        this(new ArrayList<Data>(), adapterListener);
    }

    public RecyclerAdapter() {
        this(null);
    }

    /**
     * 创建viewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 界面类型 在此约定，viewType即为xml的id
     * @return ViewHolder
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //得到inflater用于将xml初始化为view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        //将xml id 为viewType的文件初始化为一个rootView
        View root = inflater.inflate(viewType, parent, false);
        //通过子类必须实现的方法得到一个viewHolder
        ViewHolder<Data> holder = onCreateViewHolder(root, viewType);
        //设置事件点击
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        //设置view的tag为viewHolder，进行双向绑定
        root.setTag(R.id.tag_recycler_holder, holder);
        //界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        //绑定callback
        holder.adapterCallback = this;
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data data = mDataList.get(position);
        holder.bind(data);

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 复写默认的布局类型返回
     *
     * @param position 坐标
     * @return 类型，本类中约定类型即为 xml 资源id
     */
    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    /**
     * 得到布局的类型
     *
     * @param position 坐标
     * @param data     类型
     * @return xml文件的id，用于创建viewHolder
     */
    @LayoutRes
    protected abstract int getItemViewType(int position, Data data);


    protected abstract ViewHolder<Data> onCreateViewHolder(View root, int viewType);

    /**
     * 插入一条数据并通知插入
     *
     * @param data
     */
    public void add(Data data) {
        mDataList.add(data);
        notifyItemChanged(mDataList.size() - 1);
    }

    /**
     * 插入多条数据并更新
     *
     * @param dataList 数据集合
     */
    public void add(Collection<Data> dataList) {//TODO 我觉得这里有小bug
        if (dataList != null && dataList.size() > 0) {
            int startPosition = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPosition, dataList.size());
        }
    }

    /**
     * 删除全部数据
     */
    public void clear() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 将原始数据替换成一个新的集合
     * 不调用上面的clear方法，减少界面刷新次数
     *
     * @param dataList 新集合
     */
    public void replace(Collection<Data> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0)
            return;
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    /**
     * 给adapter设置监听器
     *
     * @param adapterListener 监听器
     */
    public void setAdapterListener(AdapterListener<Data> adapterListener) {
        this.adapterListener = adapterListener;
    }

    @Override
    public void onClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (adapterListener != null) {
            //得到viewHolder当前对应的适配器id
            int pos = viewHolder.getAdapterPosition();
            //回调
            adapterListener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View view) {
        ViewHolder viewHolder = (ViewHolder) view.getTag(R.id.tag_recycler_holder);
        if (adapterListener != null) {
            //得到viewHolder当前对应的适配器id
            int pos = viewHolder.getAdapterPosition();
            //回调
            adapterListener.onItemLongClick(viewHolder, mDataList.get(pos));
            return true;
        }
        return false;
    }

    /**
     * 自定义的监听器
     *
     * @param <Data> 泛型
     */
    public interface AdapterListener<Data> {
        //点击
        void onItemClick(RecyclerAdapter.ViewHolder viewHolder, Data data);

        //长按
        void onItemLongClick(RecyclerAdapter.ViewHolder viewHolder, Data data);
    }

    /**
     * 自定义的viewHolder
     *
     * @param <Data> 泛型类型
     */
    public static abstract class ViewHolder<Data> extends RecyclerView.ViewHolder {
        protected Data mData;
        private Unbinder unbinder;
        private AdapterCallback<Data> adapterCallback;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 传入的数据
         */
        void bind(Data data) {
            this.mData = data;
            onBind(data);
        }

        /**
         * 当出发绑定数据的时候，必须复写
         *
         * @param data 数据
         */
        protected abstract void onBind(Data data);

        /**
         * holder本身和对应的data进行跟新操作
         *
         * @param data
         */
        public void updateData(Data data) {
            if (this.adapterCallback != null) {
                this.adapterCallback.upDate(data, this);
            }

        }
    }
}
