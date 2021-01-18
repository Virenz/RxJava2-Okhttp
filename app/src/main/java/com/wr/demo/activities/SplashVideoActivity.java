package com.wr.demo.activities;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.wr.demo.BaseActivity;
import com.wr.demo.R;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by weir on 2020/12/22.
 * 视频启动页
 */

public class SplashVideoActivity extends BaseActivity {
    @BindView(R.id.sfv_splash)
    SurfaceView mSfvSplash;
    private MediaPlayer mMediaPlayer;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //透明状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        setContentView(R.layout.activity_video_splash);
        ButterKnife.bind(this);
        initView();
    }

    protected void initView() {
        mMediaPlayer = new MediaPlayer();
        mSfvSplash.getHolder().setKeepScreenOn(true);
        mSfvSplash.getHolder().addCallback(new SfvListener());
    }

    /**
     * 播放视频背景
     */
    public void play() {
        AudioAttributes aab = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build() ;
        mMediaPlayer.setAudioAttributes(aab);
        AssetFileDescriptor fd = null;
        try {
            fd = this.getAssets().openFd("start.mp4");
            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mMediaPlayer.setLooping(false);
            mMediaPlayer.setDisplay(mSfvSplash.getHolder());
            // 通过异步的方式装载媒体资源
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // 装载完毕回调
                    mMediaPlayer.start();
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Intent intent = new Intent(SplashVideoActivity.this, MainActivity.class);
                    startActivity(intent);
                    SplashVideoActivity.this.finish();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SfvListener implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            play();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    }

}
