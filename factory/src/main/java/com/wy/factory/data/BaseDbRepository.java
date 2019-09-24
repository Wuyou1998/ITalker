package com.wy.factory.data;

import androidx.annotation.NonNull;

import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.wy.common.factory.data.DbDataSource;
import com.wy.common.utils.CollectionUtil;
import com.wy.factory.data.helper.DbHelper;
import com.wy.factory.model.db.BaseDbModel;

import net.qiujuer.genius.kit.reflect.Reflector;

import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;

/* 名称: ITalker.com.wy.factory.data.BaseDbRepository
 * 用户: _VIEW
 * 时间: 2019/9/19,14:13
 * 描述: 基础的数据库仓库，实现对数据库基本的监听操作
 */
public abstract class BaseDbRepository<Data extends BaseDbModel<Data>> implements DbDataSource<Data>,
        DbHelper.ChangedListener<Data>,
        QueryTransaction.QueryResultListCallback<Data> {
    //和Presenter交互的回调
    private SuccessCallback<List<Data>> callback;
    //当前缓存的数据
    protected final LinkedList<Data> datas = new LinkedList<>();

    private Class<Data> dataClass;//当前泛型对应的真实class信息

    @SuppressWarnings("unchecked")
    public BaseDbRepository() {
        // 拿当前类的范型数组信息
        Type[] types = Reflector.getActualTypeArguments(BaseDbRepository.class, this.getClass());
        dataClass = (Class<Data>) types[0];
    }

    @Override
    public void load(SuccessCallback<List<Data>> callback) {
        this.callback = callback;
        //进行数据库监听
        registerDbChangedListener();
    }

    @Override
    public void dispose() {
        //取消监听，销毁数据
        this.callback = null;
        DbHelper.removeChangedListener(dataClass, this);
        datas.clear();
    }

    @Override
    @SuppressWarnings("unchecked")
    //数据库统一通知的地方：增加更改
    public void onDataSave(Data... list) {
        boolean isChanged = false;
        for (Data data : list) {
            //是关注的人，同时这个人不是我自己
            if (isRequired(data)) {
                insertOrUpdate(data);
                isChanged = true;
            }
        }
        //有数据变更则进行界面刷新
        if (isChanged) {
            notifyDataChange();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    //数据库统一通知的地方：删除更改
    public void onDataDelete(Data... list) {
        //删除情况下不用进行过滤判断
        boolean isChanged = false;
        for (Data data : list) {
            if (datas.remove(data)) {
                isChanged = true;
            }
        }
        //有数据变更则进行界面刷新
        if (isChanged)
            notifyDataChange();
    }

    //插入或者更新
    private void insertOrUpdate(Data data) {
        int index = indexOf(data);
        if (index >= 0) {
            replace(index, data);
        } else {
            insert(data);
        }
    }

    //添加
    protected void insert(Data data) {
        datas.add(data);
    }

    //更新某个坐标下的数据
    private void replace(int index, Data data) {
        datas.remove(index);
        datas.add(index, data);
    }

    //查询一个数据是否在当前的缓存数据中，如果在则返回当前坐标
    private int indexOf(Data data) {
        int index = -1;
        for (Data data1 : datas) {
            index++;
            if (data1.isSame(data)) {
                return index;
            }
        }
        return -1;
    }

    @Override
    //DbFlow通知的回调
    public void onListQueryResult(QueryTransaction transaction, @NonNull List<Data> tResult) {
        if (tResult.size() == 0) {
            datas.clear();
            notifyDataChange();
            return;
        }
        //转变为数组
        Data[] users = CollectionUtil.toArray(tResult, dataClass);
        //回到数据集更新的操作中
        onDataSave(users);
    }

    //通知界面刷新的方法
    private void notifyDataChange() {
        SuccessCallback<List<Data>> callback = this.callback;
        if (callback != null) {
            callback.onDataLoad(datas);
        }
    }

    /**
     * 添加数据库的监听操作
     */
    protected void registerDbChangedListener() {
        DbHelper.addChangedListener(dataClass, this);
    }

    /**
     * 判断当前数据是否是我们需要
     *
     * @param data 数据
     * @return true 是
     */
    protected abstract boolean isRequired(Data data);
}
