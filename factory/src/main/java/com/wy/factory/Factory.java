package com.wy.factory;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* 名称: ITalker.com.wy.factory.Factory
 * 用户: _VIEW
 * 时间: 2019/9/5,22:13
 * 描述: 一些工厂方法
 */
public class Factory {
    //单例模式
    private static volatile Factory instance;
    private final Executor executor;

    private Factory() {
        //新建一个4线程线程池
        executor = Executors.newFixedThreadPool(4);
    }

    public static Factory getInstance() {
        if (instance == null) {
            synchronized (Factory.class) {
                if (instance == null) {
                    instance = new Factory();
                }
            }
        }
        return instance;
    }

    /**
     * 异步运行的方法
     *
     * @param runnable Runnable
     */
    public static void runOnAsync(Runnable runnable) {
        //拿到单例，拿到线程池，之后异步执行
        getInstance().executor.execute(runnable);
    }
}
