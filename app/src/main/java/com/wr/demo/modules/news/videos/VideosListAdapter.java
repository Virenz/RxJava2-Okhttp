package com.wr.demo.modules.news.videos;

import android.content.Context;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.wr.demo.R;
import com.wr.demo.utils.magicrecyclerView.BaseItem;
import com.wr.demo.utils.magicrecyclerView.BaseRecyclerAdapter;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by weir on 2020/12/23.
 */

public class VideosListAdapter extends BaseRecyclerAdapter {

    private Context mContext;

    public VideosListAdapter(Fragment fragment) {
        mContext = fragment.getContext();
    }

    @Override
    public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.frame_video_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBind(RecyclerView.ViewHolder holder, int RealPosition, BaseItem data) {
        if (holder instanceof ViewHolder) {
            VideosBean topNews = (VideosBean) data.getData();
            ((ViewHolder) holder).mNewsTitle.setText(topNews.getDesc());
            String image = topNews.getVideo().getCover_addr().getUrl_list()[0];//避免null引起Picasso崩溃
            if (!TextUtils.isEmpty(image)) {
                Picasso.get()
                        .load(image)
                        .into(((ViewHolder) holder).mJcVideoPlayer.thumbImageView);
            }
            String url = topNews.getVideo().getPlay_addr().getUrl_list()[0];
            if (!TextUtils.isEmpty(url)) {
                ((ViewHolder) holder).mJcVideoPlayer.setUp(
                        url, JCVideoPlayer.SCREEN_LAYOUT_LIST, "");
            }
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.jc_video_player)
        JCVideoPlayerStandard mJcVideoPlayer;
        @BindView(R.id.news_title)
        TextView mNewsTitle;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
