package com.wr.demo.modules.news.videos;

import com.google.gson.annotations.SerializedName;
import com.wr.demo.config.Constants;

import java.util.ArrayList;

/**
 * Created by weir on 2020/12/23.
 */

public class VideosNewsList {

    @SerializedName(Constants.DOUYIN_VIDEOS_LIST)

    private ArrayList<VideosBean> mVideosArrayList;

    public ArrayList<VideosBean> getVideosArrayList() {
        return mVideosArrayList;
    }
}
