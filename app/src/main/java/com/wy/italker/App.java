package com.wy.italker;

import com.igexin.sdk.PushManager;
import com.wy.common.app.Application;
import com.wy.factory.Factory;

/* 名称: ITalker.com.wy.italker.App
 * 用户: _VIEW
 * 时间: 2019/9/4,15:30
 * 描述: Application
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //调用Factory进行初始化
        Factory.setUp();
        //个推初始化
        PushManager.getInstance().initialize(this, PushService.class);
    }
}
