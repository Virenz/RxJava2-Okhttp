package com.wr.demo.config;

/**
 * Created by weir on 2020/12/22.
 */

public class Constants {
    /**
     * NetWork Erro
     */
    public static String ERRO = "错误信息:";

    /**
     * SecretUtil
     */
    public static String NOSUCHALGORITHM = "不支持此种加密方式";
    public static String UNSUPPENCODING = "不支持的编码格式";

    /**
     * SharePreference
     */
    public static final String SP_NAME = "com.wr.demo";

    /**
     * DiskCache
     */
    public static final long CACHE_MAXSIZE = 10 * 1024 * 1024;
    public static final String CACHE_DOUYIN_FILE = "douyin_cache_file";
    public static final String CACHE_DOUYIN_VIDEOS = "douyin_cache_videos";

    /**
     * 抖音视频
     */
    public static final String DOUYIN_VIDEOS_API_ID = "aweme/v1/feed/";
    public static final String DOUYIN_VIDEOS_LIST = "aweme_list";
    public static final String DOUYIN_COOKIES = "install_id=1107961669234743; ttreq=1$f4fa2e588e59777797005762cebdfb1d91985ee4; odin_tt=29fb32237e84c66fac856c4fe2d9d7a192276ef54305b3e33c7165e9b3456ebb40b1aa1a331e2c44d4392e8309dd082bbc1527819a73a01ff36254c9b3b6084b";
    public static final String DOUYIN_FEED_PARAMS = "type=0&max_cursor=0&min_cursor=-1&count=6&is_first_feed=false&ts=&_rticket=";
}
