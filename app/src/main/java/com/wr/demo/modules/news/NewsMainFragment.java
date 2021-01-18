package com.wr.demo.modules.news;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.wr.demo.BaseFragment;
import com.wr.demo.R;
import com.wr.demo.modules.news.videos.VideosFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

/**
 * Created by weir on 2020/12/23.
 * 新闻列表界面
 */

public class NewsMainFragment extends BaseFragment implements ViewPager.OnPageChangeListener {

    private ArrayList<Fragment> mFragmentArrayList;
    @BindView(R.id.vp_news_list)
    ViewPager mVpNewsList;

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        mFragmentArrayList = new ArrayList<>();
        mFragmentArrayList.add(new VideosFragment());
        mVpNewsList.setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager(), BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentArrayList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentArrayList.size();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return getResources().getStringArray(R.array.news_title)[position];
            }
        });
        mVpNewsList.addOnPageChangeListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mVpNewsList.setCurrentItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
