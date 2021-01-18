package com.wr.demo.modules.news.videos;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wr.demo.BaseFragment;
import com.wr.demo.R;
import com.wr.demo.utils.magicrecyclerView.BaseItem;
import com.wr.demo.utils.magicrecyclerView.BaseRecyclerAdapter;
import com.wr.demo.utils.magicrecyclerView.MagicRecyclerView;
import com.wr.demo.modules.news.NewsContract;
import com.wr.demo.rxbus.RxBus;
import com.wr.demo.rxbus.RxConstants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by weir on 2020/12/23.
 * 抖音视频
 */

public class VideosFragment extends BaseFragment implements NewsContract.View, SwipeRefreshLayout.OnRefreshListener, BaseRecyclerAdapter.OnItemClickListener {
    @BindView(R.id.newsRecycler)
    MagicRecyclerView mNewsRecycler;
    @BindView(R.id.refresh)
    SwipeRefreshLayout mRefresh;
    @BindView(R.id.empty_msg)
    TextView mEmptyMsg;
    private NewsContract.Presenter mPresenter;
    private VideosListAdapter mAdapter;
    private boolean loading = false;
    private Disposable mDisposable;
    private LinearLayoutManager mLinearLayoutManager;
    private Unbinder mUnbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = getActivity().getLayoutInflater().inflate(R.layout.frame_video, null, false);
        mUnbinder = ButterKnife.bind(this, view);
        bindPresenter();
        mLinearLayoutManager = new LinearLayoutManager(this.getContext());
        mNewsRecycler.setLayoutManager(mLinearLayoutManager);
        //屏蔽掉默认的动画，房子刷新时图片闪烁
        mNewsRecycler.getItemAnimator().setChangeDuration(0);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        onHiddenChanged(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        mRefresh.setRefreshing(false);
        unbindPresenter();
        onHiddenChanged(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        destoryPresenter();
        mAdapter = null;
    }

    private void initView() {
        mNewsRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mNewsRecycler.refreshAble()) {
                    mRefresh.setEnabled(true);
                }
                if (mNewsRecycler.loadAble()) {
                    loadMoreNews();
                }

                autoPlayVideo(recyclerView);
            }
        });
        mRefresh.setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.white_FFFFFF, null));
        mRefresh.setOnRefreshListener(this);
        mRefresh.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mRefresh.setRefreshing(true);
        refreshNews();
        mPresenter.loadCache();
        View footer = mNewsRecycler.getFooterView();
        if (footer != null) {
            footer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadMoreNews();
                }
            });
        }
    }

    /**
     * 滑动停止自动播放视频
     */
    private void autoPlayVideo(RecyclerView view) {

        if (view != null) {
            JCVideoPlayerStandard currPlayer = (JCVideoPlayerStandard) view.getChildAt(0).findViewById(R.id.jc_video_player);

            if (currPlayer.currentState == JCVideoPlayer.CURRENT_STATE_NORMAL
                    || currPlayer.currentState == JCVideoPlayer.CURRENT_STATE_ERROR) {
                currPlayer.startButton.performClick();
            }
            return;
        }
    }

    @Override
    public void showRefreshBar() {
        mRefresh.setRefreshing(true);
    }

    @Override
    public void hideRefreshBar() {
        mRefresh.setRefreshing(false);
    }

    @Override
    public void refreshNews() {
        mPresenter.refreshNews();
    }

    @Override
    public void refreshNewsFail(String errorMsg) {
        if (mAdapter == null) {
            mEmptyMsg.setVisibility(View.VISIBLE);
            mNewsRecycler.setVisibility(View.INVISIBLE);
            mRefresh.requestFocus();
        }
    }

    @Override
    public void refreshNewsSuccessed(ArrayList<BaseItem> topNews) {
        if (topNews == null || topNews.size() <= 0) {
            mEmptyMsg.setVisibility(View.VISIBLE);
            mNewsRecycler.setVisibility(View.INVISIBLE);
            mRefresh.requestFocus();
        } else {
            mEmptyMsg.setVisibility(View.GONE);
            mNewsRecycler.setVisibility(View.VISIBLE);
        }
        if (mAdapter == null) {
            mAdapter = new VideosListAdapter(this);
            mAdapter.setBaseDatas(topNews);
            mNewsRecycler.setAdapter(mAdapter);
            //实质是是对 adapter 设置点击事件所以需要在设置 adapter 之后调用
            mNewsRecycler.addOnItemClickListener(this);
        } else {
            mAdapter.setBaseDatas(topNews);
        }
        mNewsRecycler.showFooter();
    }

    @Override
    public void loadMoreNews() {
        if (!loading) {
            mPresenter.loadMore();
            loading = true;
        }
    }

    @Override
    public void loadMoreFail(String errorMsg) {
        loading = false;
    }

    @Override
    public void loadMoreSuccessed(ArrayList<BaseItem> topNewses) {
        loading = false;
        mAdapter.addBaseDatas(topNewses);
    }

    @Override
    public void loadAll() {
        mNewsRecycler.hideFooter();
    }

    @Override
    public void onRefresh() {
        refreshNews();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden && mRefresh.isRefreshing()) { // 隐藏的时候停止 SwipeRefreshLayout 转动
            mRefresh.setRefreshing(false);
        }
        if (!hidden) {
            subscribeEvent();
        } else {
            if (mDisposable != null && !mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
        }
    }

    @Override
    public void onItemClick(int position, BaseItem data, View view) {
    }

    @Override
    public void bindPresenter() {
        if (mPresenter == null) {
            mPresenter = new VideosPresenter();
        }
        mPresenter.bindView(this);
    }

    @Override
    public void unbindPresenter() {
        mPresenter.unbindView();
    }

    @Override
    public void destoryPresenter() {
        mPresenter.onDestory();
    }

    private void subscribeEvent(){
        if (mDisposable!=null){
            mDisposable.dispose();
        }
        RxBus.getDefault()
                .toObservableWithCode(RxConstants.BACK_PRESSED_CODE, String.class)
                .subscribeWith(new Observer<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(String value) {
                        if (value.equals(RxConstants.BACK_PRESSED_DATA) && mNewsRecycler != null) {
                            //滚动到顶部
                            mLinearLayoutManager.smoothScrollToPosition(mNewsRecycler, null, 0);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        subscribeEvent();
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
