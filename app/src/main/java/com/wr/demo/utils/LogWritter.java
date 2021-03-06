package com.wr.demo.utils;

import android.util.Log;

import com.wr.demo.config.Config;

import java.util.logging.Logger;

/**
 * Created by PandaQ on 2016/9/7.
 * email : 767807368@qq.com
 * log工具类
 */
public class LogWritter extends Logger {

    protected LogWritter(String name, String resourceBundleName) {
        super(name, resourceBundleName);
    }

    public static void LogStr(String string) {
        if (Config.DEBUG) {
            Log.i("weir", string);
        }
    }
}
