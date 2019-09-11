package com.wy.factory.net;

import com.wy.common.Common;
import com.wy.factory.Factory;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/* 名称: ITalker.com.wy.factory.net.Network
 * 用户: _VIEW
 * 时间: 2019/9/9,22:00
 * 描述: 网络请求的封装
 */
public class Network {
    //构建一个retrofit
    public static Retrofit getRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Retrofit.Builder builder = new Retrofit.Builder();
        return builder.baseUrl(Common.API_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(Factory.getGson()))
                .build();
    }

    /**
     * 返回一个请求代理
     * @return RemoteService
     */
    public static RemoteService remote() {
        return Network.getRetrofit().create(RemoteService.class);
    }
}
