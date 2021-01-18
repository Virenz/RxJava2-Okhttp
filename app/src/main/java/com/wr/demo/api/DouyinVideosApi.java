package com.wr.demo.api;

import com.wr.demo.modules.news.videos.VideosNewsList;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Url;

public interface DouyinVideosApi {

    @GET
    Observable<VideosNewsList> getDouyinVideos(@HeaderMap Map<String, Object> headers, @Url String params);
}
