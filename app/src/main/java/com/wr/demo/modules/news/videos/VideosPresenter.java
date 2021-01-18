package com.wr.demo.modules.news.videos;

import com.wr.demo.CustomApplication;
import com.wr.demo.api.ApiManager;
import com.wr.demo.config.Constants;
import com.wr.demo.disklrucache.DiskCacheManager;
import com.wr.demo.utils.douyin.DouyinSign;
import com.wr.demo.utils.magicrecyclerView.BaseItem;
import com.wr.demo.modules.BasePresenter;
import com.wr.demo.modules.ImpBaseView;
import com.wr.demo.modules.news.NewsContract;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by weir on 2020/12/23.
 */

class VideosPresenter extends BasePresenter implements NewsContract.Presenter {

    private NewsContract.View mNewsListFrag;
    private String params;
    private Map<String, Object> headers;

    @Override
    public void refreshNews() {
        if (headers != null)
            headers.clear();
        long _rticket = System.currentTimeMillis();
        params = DouyinSign.get_params(_rticket);
        headers = DouyinSign.get_headers(params, _rticket);

        mNewsListFrag.showRefreshBar();
        ApiManager.getInstence().getDouyinVideosServie()
                .getDouyinVideos(headers, params)
                .map(new Function<VideosNewsList, ArrayList<VideosBean>>() {
                    @Override
                    public ArrayList<VideosBean> apply(VideosNewsList videosNewsList) {
                        return videosNewsList.getVideosArrayList();
                    }
                })
                .flatMap(new Function<ArrayList<VideosBean>, Observable<VideosBean>>() {
                    @Override
                    public Observable<VideosBean> apply(ArrayList<VideosBean> topNewses) throws Exception {
                        return Observable.fromIterable(topNewses);
                    }
                })
                .filter(new Predicate<VideosBean>() {
                    @Override
                    public boolean test(VideosBean topNews) throws Exception {
                        return topNews != null || topNews.getVideo().getPlay_addr() != null;
                    }
                })
                .map(new Function<VideosBean, BaseItem>() {
                    @Override
                    public BaseItem apply(VideosBean topNews) {
                        BaseItem<VideosBean> baseItem = new BaseItem<>();
                        baseItem.setData(topNews);
                        return baseItem;
                    }
                })
                .toList()
                //将 List 转为ArrayList 缓存存储 ArrayList Serializable对象
                .map(new Function<List<BaseItem>, ArrayList<BaseItem>>() {
                    @Override
                    public ArrayList<BaseItem> apply(List<BaseItem> baseItems) {
                        ArrayList<BaseItem> items = new ArrayList<>();
                        items.addAll(baseItems);
                        return items;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<ArrayList<BaseItem>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(ArrayList<BaseItem> value) {
                        DiskCacheManager manager = new DiskCacheManager(CustomApplication.getContext(), Constants.CACHE_DOUYIN_FILE);
                        manager.put(Constants.CACHE_DOUYIN_VIDEOS, value);
                        mNewsListFrag.hideRefreshBar();
                        mNewsListFrag.refreshNewsSuccessed(value);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mNewsListFrag.hideRefreshBar();
                        mNewsListFrag.refreshNewsFail(e.getMessage());
                    }

                });
    }

    //两个方法没区别,只是刷新会重新赋值
    @Override
    public void loadMore() {

        if (headers != null)
            headers.clear();
        long _rticket = System.currentTimeMillis();
        params = DouyinSign.get_params(_rticket);
        headers = DouyinSign.get_headers(params, _rticket);

        ApiManager.getInstence().getDouyinVideosServie()
                .getDouyinVideos(headers, params)
                .map(new Function<VideosNewsList, ArrayList<VideosBean>>() {
                    @Override
                    public ArrayList<VideosBean> apply(VideosNewsList videosNewsList) {
                        return videosNewsList.getVideosArrayList();
                    }
                })
                .flatMap(new Function<ArrayList<VideosBean>, Observable<VideosBean>>() {
                    @Override
                    public Observable<VideosBean> apply(ArrayList<VideosBean> topNewses) {
                        return Observable.fromIterable(topNewses);
                    }
                })
                .filter(new Predicate<VideosBean>() {
                    @Override
                    public boolean test(VideosBean topNews) throws Exception {
                        return topNews != null || topNews.getVideo().getPlay_addr() != null;
                    }
                })
                .map(new Function<VideosBean, BaseItem>() {
                    @Override
                    public BaseItem apply(VideosBean topNews) {
                        BaseItem<VideosBean> baseItem = new BaseItem<>();
                        baseItem.setData(topNews);
                        return baseItem;
                    }
                })
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<BaseItem>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onSuccess(List<BaseItem> value) {
                        if (value != null && value.size() > 0) {
                            mNewsListFrag.loadMoreSuccessed((ArrayList<BaseItem>) value);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mNewsListFrag.loadMoreFail(e.getMessage());
                    }

                });

    }

    /**
     * 读取缓存
     */
    public void loadCache() {
        DiskCacheManager manager = new DiskCacheManager(CustomApplication.getContext(), Constants.CACHE_DOUYIN_FILE);
        ArrayList<BaseItem> topNews = manager.getSerializable(Constants.CACHE_DOUYIN_VIDEOS);
        if (topNews != null) {
            mNewsListFrag.refreshNewsSuccessed(topNews);
        }
    }

    @Override
    public void bindView(ImpBaseView view) {
        mNewsListFrag = (NewsContract.View) view;
    }

    @Override
    public void unbindView() {
        dispose();
    }

    @Override
    public void onDestory() {
        mNewsListFrag = null;
    }
}
