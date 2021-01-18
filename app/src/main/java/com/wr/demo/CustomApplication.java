package com.wr.demo;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.squareup.leakcanary.LeakCanary;

/**
 * Created by weir on 2020/12/22.
 */

public class CustomApplication extends Application {
    public static CustomApplication sApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        if (sApplication == null) {
            sApplication = this;
        }
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }

    public static Context getContext() {
        return sApplication.getApplicationContext();
    }

    /**
     * 获取应用的版本号
     *
     * @return 应用版本号，默认返回1
     */
    public static int getAppVersionCode() {
        Context context = getContext();
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
