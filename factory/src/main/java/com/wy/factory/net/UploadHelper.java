package com.wy.factory.net;

import android.text.format.DateFormat;
import android.util.Log;

import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.wy.common.Common;
import com.wy.common.app.Application;
import com.wy.common.utils.HashUtil;

import java.io.File;
import java.util.Date;

/* 名称: ITalker.com.wy.factory.net.UploadHelper
 * 用户: _VIEW
 * 时间: 2019/9/5,22:14
 * 描述: 上传工具类 用于上传任意文件到阿里OSS存储
 */
public class UploadHelper {
    private static final String TAG = "UploadHelper";

    private static OSS getClient() {
        // 在移动端建议使用STS的方式初始化OSSClient。
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Common.ALI_ACCESS_KEY_ID,
                Common.ALI_ACCESS_KEY_SECRET);
        return new OSSClient(Application.getInstance(),Common.ALI_END_POINT, credentialProvider);
    }

    /**
     * 上传文件，成功后返回一个路径
     *
     * @param key  上传后在服务器上独立的key
     * @param path 需要上传的文件的路径
     * @return 存储的地址
     */
    private static String upload(String key, String path) {
        // 构造上传请求
        PutObjectRequest request = new PutObjectRequest(Common.ALI_BUCKET_NAME, key, path);
        try {
            //初始化上传的client
            OSS client = getClient();
            //开始同步上传
            client.putObject(request);
            //得到一个外网可访问的url
            Log.e(TAG, "上传后的文件URL: " + client.presignPublicObjectURL(Common.ALI_BUCKET_NAME, key));
            return client.presignPublicObjectURL(Common.ALI_BUCKET_NAME, key);

        } catch (Exception e) {
            e.printStackTrace();
            //如果有异常，返回空
            return null;
        }
    }

    /**
     * 上传普通图片
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadImage(String path) {
        String key = getImageKey(path);
        return upload(key, path);
    }

    /**
     * 上传头像
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadAvatar(String path) {
        String key = getAvatarKey(path);
        return upload(key, path);
    }

    /**
     * 上传音频
     *
     * @param path 本地地址
     * @return 服务器地址
     */
    public static String uploadAudio(String path) {
        String key = getAudioKey(path);
        return upload(key, path);
    }

    /**
     * 分月存储，避免一个文件夹文件太多
     *
     * @return YYYY-MM
     */
    private static String getDataString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    //image/201909/wuyou.jpg
    private static String getImageKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String date = getDataString();
        return String.format("image/%s/%s.jpg", getDataString(), fileMd5);

    }

    //avatar/201909/wuyou.jpg
    private static String getAvatarKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String date = getDataString();
        return String.format("avatar/%s/%s.jpg", getDataString(), fileMd5);

    }

    //audio/201909/wuyou.mp3
    private static String getAudioKey(String path) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String date = getDataString();
        return String.format("audio/%s/%s.mp3", getDataString(), fileMd5);
    }
}
