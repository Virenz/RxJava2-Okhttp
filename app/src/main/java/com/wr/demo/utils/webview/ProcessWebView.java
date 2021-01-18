package com.wr.demo.utils.webview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.wr.demo.R;

/**
 * Created by PandaQ on 2017/6/29.
 * 带进度条的WebView
 */

public class ProcessWebView extends WebView {

    private ProgressBar mProgressBar;

    public ProcessWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProgressBar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        mProgressBar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2, 0, 0));
        Drawable drawable = context.getResources().getDrawable(R.drawable.webview_process_state);
        mProgressBar.setProgressDrawable(drawable);
        addView(mProgressBar);
        setWebViewClient(new MyWebClient());
        setWebChromeClient(new ChromeClient());
    }

    private class ChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                addImageClickListener(view);
                mProgressBar.setVisibility(GONE);
            } else {
                if (mProgressBar.getVisibility() == GONE) {
                    mProgressBar.setVisibility(VISIBLE);
                    mProgressBar.setProgress(newProgress);
                }
            }
        }
    }

    private class MyWebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String request) {
            //view.loadUrl(request);
            return true;
        }
    }

    /**
     * 为所有的图片添加点击事件
     *
     * @param webView 对应的 WebView
     */
    private void addImageClickListener(WebView webView) {
        webView.loadUrl("javascript:(initClick())()");
    }

}
