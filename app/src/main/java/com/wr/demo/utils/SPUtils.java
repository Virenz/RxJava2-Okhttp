package com.wr.demo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.wr.demo.CustomApplication;
import com.wr.demo.config.Constants;

/**
 * Created by weir on 2020/12/23.
 */

public class SPUtils {
    private static SharedPreferences sp;

    public static SharedPreferences instance(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(Constants.SP_NAME, Context.MODE_PRIVATE);
        }
        return sp;
    }

    public static void clearAllData() {
        checkSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();
    }

    private SharedPreferences.Editor edit() {
        checkSp();
        return sp.edit();
    }

    private static void checkSp() {
        if (sp == null) {
            sp = instance(CustomApplication.getContext());
        }
    }
}
