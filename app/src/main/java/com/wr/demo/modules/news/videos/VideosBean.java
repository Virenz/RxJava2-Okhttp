package com.wr.demo.modules.news.videos;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by weir on 2016/9/20.
 */

public class VideosBean implements Serializable {

    /**
     * id
     */
    @SerializedName("aweme_id")
    private String id;
    /**
     * 小内容
     */
    @SerializedName("desc")
    private String desc;
    /**
     * url
     */
    @SerializedName("video")
    private VideoListBean video;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public VideoListBean getVideo() {
        return this.video;
    }

    public void setVideo(VideoListBean video) {
        this.video = video;
    }


    public static class VideoListBean implements Serializable {

        @SerializedName("play_addr")
        private VideoListPlayAddrBean play_addr;

        @SerializedName("cover")
        private VideoListPlayAddrBean cover_addr;

        public VideoListPlayAddrBean getPlay_addr() {
            return this.play_addr;
        }

        public void setPlay_addr(VideoListPlayAddrBean play_addr) {
            this.play_addr = play_addr;
        }

        public VideoListPlayAddrBean getCover_addr() {
            return this.cover_addr;
        }

        public void setCover_addr(VideoListPlayAddrBean cover_addr) {
            this.cover_addr = cover_addr;
        }
    }

    public static class VideoListPlayAddrBean implements Serializable {

        @SerializedName("url_list")
        private String[] url_list;

        public String[] getUrl_list() {
            return this.url_list;
        }

        public void setUrl_list(String[] url_list) {
            this.url_list = url_list;
        }
    }

}
