package com.wr.demo.activities;

import android.animation.ArgbEvaluator;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;

import com.wr.demo.R;
import com.wr.demo.BaseActivity;
import com.wr.demo.utils.SwipeBackLayout;

/**
 * Created by weir on 2020/12/22.
 * 所有侧滑返回的activity的父类
 */

public class SwipeBackActivity extends BaseActivity implements SwipeBackLayout.SwipeListener {
    protected SwipeBackLayout layout;
    private ArgbEvaluator argbEvaluator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = (SwipeBackLayout) LayoutInflater.from(this).inflate(
                R.layout.swipeback_base, null);
        layout.attachToActivity(this);
        argbEvaluator = new ArgbEvaluator();
        layout.addSwipeListener(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            currentStatusColor = getResources().getColor(R.color.colorPrimaryDark, null);
        }
    }

    public void addViewPager(ViewPager pager) {
        layout.addViewPager(pager);
    }

    @Override
    public void swipeValue(double value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int statusColor = (int) argbEvaluator.evaluate((float) value, currentStatusColor,
                    ContextCompat.getColor(this, R.color.transparent_00ffffff));
            getWindow().setStatusBarColor(statusColor);
        }
    }

}
