package com.wy.common.app;

import java.io.File;

/* 名称: ITalker.com.wy.common.app.Application
 * 用户: _VIEW
 * 时间: 2019/9/4,15:11
 * 描述: ToDo
 */
public class Application extends android.app.Application {
    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    /**
     * 获取缓存文件夹地址
     *
     * @return 当前App缓存文件夹地址
     */
    public static File getCacheDirFile() {
        return instance.getCacheDir();
    }

    /**
     * @return
     */
    public static File getAvatarTempFile() {
        //得到头像目录的缓存地址
        File dir = new File(getCacheDirFile(), "avatar");
        //创建所有的对应的文件夹
        dir.mkdirs();
        //删除旧的缓存文件
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File file : files) {
                file.delete();
            }
        }
        //返回一个当前时间戳的目录文件地址
        File path = new File(dir, System.currentTimeMillis() + "jpg");
        return path.getAbsoluteFile();
    }
}
