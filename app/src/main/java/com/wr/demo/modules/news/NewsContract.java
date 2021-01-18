package com.wr.demo.modules.news;

import com.wr.demo.modules.ImpBasePresenter;
import com.wr.demo.modules.ImpBaseView;
import com.wr.demo.utils.magicrecyclerView.BaseItem;

import java.util.ArrayList;

/**
 * Created by weir on 2020/12/23.
 */

public interface NewsContract {
    interface View extends ImpBaseView{
        void showRefreshBar();

        void hideRefreshBar();

        void refreshNews();

        void refreshNewsFail(String errorMsg);

        void refreshNewsSuccessed(ArrayList<BaseItem> topNews);

        void loadMoreNews();

        void loadMoreFail(String errorMsg);

        void loadMoreSuccessed(ArrayList<BaseItem> topNewses);

        void loadAll();
    }

    interface Presenter extends ImpBasePresenter {
        void refreshNews();

        void loadMore();

        void loadCache();
    }
}
