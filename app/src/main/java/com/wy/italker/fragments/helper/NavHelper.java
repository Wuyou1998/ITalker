package com.wy.italker.fragments.helper;

import android.content.Context;
import android.util.SparseArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/* 名称: ITalker.com.wy.italker.fragments.helper.NavHelper
 * 用户: _VIEW
 * 时间: 2019/9/3,21:27
 * 描述: 完成对Fragment的调度与重用问题，达到最优的fragment切换
 */
public class NavHelper<T> {
    private final FragmentManager fragmentManager;
    private final int containerId;
    //所有的tab
    private final SparseArray<Tab<T>> tabs = new SparseArray();
    private final Context context;
    private final OnTabChangeListener<T> listener;
    //当前一个选中的tab
    private Tab<T> currentTab;

    public NavHelper(Context context, FragmentManager fragmentManager, int containerId, OnTabChangeListener<T> listener) {
        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
        this.context = context;
        this.listener = listener;
    }

    /**
     * 添加 Tab
     *
     * @param menuId Tab对应的Id
     * @param tab    Tab
     */
    public NavHelper<T> add(int menuId, Tab<T> tab) {
        tabs.put(menuId, tab);
        return this;
    }

    /**
     * 获取当前显示的tab
     *
     * @return currentTab
     */
    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    /**
     * 执行点击菜单的操作
     *
     * @param menuId 点击菜单的id
     * @return 是否能够处理这个点击
     */
    public boolean performClickMenu(int menuId) {
        //集合中寻找点击菜单对应的tab,如果有则进行处理
        Tab<T> tab = tabs.get(menuId);
        if (tab != null) {
            doSelect(tab);
            return true;
        }
        return false;
    }

    //进行真实的tab选择操作
    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            //如果当前tab就是点击的tab 不做任何处理
            if (oldTab == tab) {
                notifyReselect(tab);
                return;
            }

        }
        //赋值并调用切换方法
        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                //从界面移除，但是还在fragment缓存空间中
                ft.detach(oldTab.fragment);
            }
        }

        if (newTab != null) {
            if (newTab.fragment == null) {
                Fragment fragment = fragmentManager.getFragmentFactory().instantiate(context.getClassLoader(), newTab.clazz.getName());
                //首次新建，缓存起来
                newTab.fragment = fragment;
                //提交到fragmentManager
                ft.add(containerId, fragment, newTab.clazz.getName());
            } else {
                //从FragmentManager的缓存空间中重新加载到界面里
                ft.attach(newTab.fragment);
            }
        }
        ft.commit();
        //通知回调
        notifyTabSelect(newTab, oldTab);
    }

    /**
     * 回调监听器
     *
     * @param newTab 新的Tab
     * @param oldTab 旧的Tab
     */
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
        if (listener != null) {
            listener.onTabChange(newTab, oldTab);
        }
    }

    private void notifyReselect(Tab<T> tab) {
        //TODO 二次点击要做点什么？
    }

    /**
     * 所有的Tab的基础属性
     *
     * @param <T>
     */
    public static class Tab<T> {
        public Tab(Class<? extends Fragment> clazz, T extra) {
            this.clazz = clazz;
            this.extra = extra;
        }

        //fragment对应的class信息
        public Class<? extends Fragment> clazz;
        //额外的信息，用户自己设定
        public T extra;
        //内部缓存的Fragment，package权限
        Fragment fragment;
    }

    /**
     * 事件处理完成后的回调接口
     *
     * @param <T>
     */
    public interface OnTabChangeListener<T> {
        void onTabChange(Tab<T> newTab, Tab<T> oldTab);

    }
}
