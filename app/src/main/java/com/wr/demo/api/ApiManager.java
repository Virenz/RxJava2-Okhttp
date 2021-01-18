package com.wr.demo.api;

import com.wr.demo.config.Config;
import com.wr.demo.utils.JsonConverter.JsonConverterFactory;
import com.wr.demo.utils.okhttp.CustomInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by weir on 2020/12/22.
 * 集中处理Api相关配置的Manager类
 */
public class ApiManager {

    private DouyinVideosApi mVideosApi;
    private static ApiManager sApiManager;

    private static OkHttpClient mClient;

    private ApiManager() {

    }

    public static ApiManager getInstence() {
        if (sApiManager == null) {
            synchronized (ApiManager.class) {
                if (sApiManager == null) {
                    sApiManager = new ApiManager();
                }
            }
        }
        mClient = new OkHttpClient.Builder()
                .addInterceptor(new CustomInterceptor())
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        return sApiManager;
    }

    /**
     * 封装视频API
     */
    public DouyinVideosApi getDouyinVideosServie() {
        if (mVideosApi == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Config.DOUYIN_VIDEO_API)
                    .client(mClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(JsonConverterFactory.create())
                    .build();
            mVideosApi = retrofit.create(DouyinVideosApi.class);
        }
        return mVideosApi;
    }
}
