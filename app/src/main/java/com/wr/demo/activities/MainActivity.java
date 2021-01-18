package com.wr.demo.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.wr.demo.BaseActivity;
import com.wr.demo.R;
import com.wr.demo.modules.news.NewsMainFragment;
import com.wr.demo.rxbus.RxBus;
import com.wr.demo.rxbus.RxConstants;
import com.wr.demo.utils.BlurImageUtils;
import com.wr.demo.utils.DataCleanManager;
import com.wr.demo.utils.ViewUtils;
import com.wr.demo.widget.NavItem;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by weir on 2020/12/22.
 */

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private final int ABOUT_ME = 10;
    private final int FAVORITE = 11;
    private final int VIDEO = 12;
    private final int SHARE = 13;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.userimage)
    CircleImageView mUserimage;
    @BindView(R.id.navigation_header_container)
    LinearLayout mNavigationHeaderContainer;
    private Fragment mCurrentFrag;
    private FragmentManager fm;
    private Fragment mNewsFragment;
    private boolean drawerOpen = false;
    private NavItem mCleanItem;
    private int drawerIntentAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        fm = getSupportFragmentManager();

        initView();
    }

    /**
     * 几个Fragment都采用懒加载的方式，只有当用户可见状态下才做数据初始化操作
     */
    private void initView() {
        mNewsFragment = new NewsMainFragment();
        Picasso.get()
                .load("file://" + ViewUtils.getAppFile(this, "images/user.png"))
                .error(getResources().getDrawable(R.drawable.userimage, null))
                .into(mUserimage, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap overlay = BlurImageUtils.blur(mUserimage, 3, 3);
                        mNavigationHeaderContainer.setBackground(new BitmapDrawable(getResources(), overlay));
                    }

                    @Override
                    public void onError(Exception e) {
                        Bitmap overlay = BlurImageUtils.blur(mUserimage, 3, 3);
                        mNavigationHeaderContainer.setBackground(new BitmapDrawable(getResources(), overlay));
                    }
                });

        switchContent(mNewsFragment);
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                drawerView.setClickable(true);
                drawerOpen = true;
                mCleanItem.setTvActionState(DataCleanManager.getTotalCacheSize(MainActivity.this));
                // action 初始化
                drawerIntentAction = 0;
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                drawerOpen = false;
                switch (drawerIntentAction) {
                    default:
                        break;
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        mUserimage.setOnClickListener(this);
        mDrawerLayout.findViewById(R.id.nav_favorite).setOnClickListener(this);
        mDrawerLayout.findViewById(R.id.nav_download).setOnClickListener(this);
        mDrawerLayout.findViewById(R.id.nav_share).setOnClickListener(this);
        mDrawerLayout.findViewById(R.id.nav_about).setOnClickListener(this);
        mCleanItem = (NavItem) mDrawerLayout.findViewById(R.id.nav_clean);
        mCleanItem.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    /**
     * 动态添加fragment，不会重复创建fragment
     *
     * @param to 将要加载的fragment
     */
    public void switchContent(Fragment to) {
        if (mCurrentFrag != to) {
            if (!to.isAdded()) {// 如果to fragment没有被add则增加一个fragment
                if (mCurrentFrag != null) {
                    fm.beginTransaction().hide(mCurrentFrag).commit();
                }
                fm.beginTransaction()
                        .add(R.id.main_view, to)
                        .commit();
            } else {
                fm.beginTransaction().hide(mCurrentFrag).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            mCurrentFrag = to;
        }
    }

    private Long firstTime = 0L;

    @Override
    public void onBackPressed() {
        if (drawerOpen) {
            mDrawerLayout.closeDrawers();
            return;
        }

        // RxBus.getDefault().postWithCode(RxConstants.BACK_PRESSED_CODE, RxConstants.BACK_PRESSED_DATA);
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 1500) {
            showLongToast(this, getString(R.string.back_again_exit));
            firstTime = secondTime;
        } else {
            System.exit(0);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.userimage:
                break;
            case R.id.nav_favorite:
                drawerIntentAction = FAVORITE;
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_download:
                drawerIntentAction = VIDEO;
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_share:
                drawerIntentAction = SHARE;
                break;
            case R.id.nav_about:
                drawerIntentAction = ABOUT_ME;
                mDrawerLayout.closeDrawers();
                break;
            case R.id.nav_clean:
                DataCleanManager.clearAllCache(this);
                mCleanItem.setTvActionState(DataCleanManager.getTotalCacheSize(this));
                break;
        }
    }

    private void saveUserImage(Bitmap bitmap) {
        // 保存头像到sdcard
        FileOutputStream fos;
        try {
            File file = new File(ViewUtils.getAppFile(this, "images"));
            File image = new File(ViewUtils.getAppFile(this, "images/user.png"));
            if (!file.exists()) {
                file.mkdirs();
                if (!image.exists()) {
                    image.createNewFile();
                }
            }
            fos = new FileOutputStream(image);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showShare(String url, String shareTitle) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(shareIntent, shareTitle));
    }
}
