package com.wr.demo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.wr.demo.R;
import com.wr.demo.BaseActivity;
import com.wr.demo.utils.ViewUtils;
import com.squareup.picasso.Picasso;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by weir on 2020/12/22.
 * 闪屏页，优化启动体验
 */

public class SplashActivity extends BaseActivity {
    @BindView(R.id.iv_splash_image)
    ImageView mIvSplashImage;
    @BindView(R.id.iv_background)
    ImageView mIvBackground;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        String[] images = getResources().getStringArray(R.array.splash_background);
        int position = new Random().nextInt(images.length - 1) % (images.length);
        Picasso.get()
                .load(images[position])
                .into(mIvBackground);
        Picasso.get()
                .load("file://" + ViewUtils.getAppFile(this, "images/user.png"))
                .error(getResources().getDrawable(R.drawable.userimage, null))
                .into(mIvSplashImage);
        AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
        animation.setDuration(1000);
        mIvSplashImage.startAnimation(animation);
        animation.setAnimationListener(new AnimationImpl());
    }

    private class AnimationImpl implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
