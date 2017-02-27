package com.cl.testapp.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Base64;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cl.testapp.BuildConfig;

import java.io.InputStream;

/**
 * 视频播放的WebView
 * Created by Administrator on 2016-12-28.
 */

public class WebVideoView extends WebView {

    private final Context mContext;

    public WebVideoView(Context context) {
        this(context, null);
    }

    public WebVideoView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WebVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        setWebViewClient(new WebClient());
        setWebChromeClient(new ChromeClient());
        WebSettings webSettings = getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setSaveFormData(false);
        webSettings.setAppCacheEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setLoadWithOverviewMode(false);
        webSettings.setUseWideViewPort(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (BuildConfig.DEBUG) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
    }


    private class WebClient extends WebViewClient {

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }

        // 添加css
        private void injectCSS(String filename) {
            try {
                InputStream inputStream = mContext.getAssets().open(filename);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);
                inputStream.close();
                String encoded = Base64.encodeToString(buffer, Base64.NO_WRAP);
                loadUrl("javascript:(function() {" +
                        "var parent = document.getElementsByTagName('head').item(0);" +
                        "var style = document.createElement('style');" +
                        "style.type = 'text/css';" +
                        // Tell the browser to BASE64-decode the string into your script !!!
                        "style.innerHTML = window.atob('" + encoded + "');" +
                        "parent.appendChild(style)" +
                        "})()");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class ChromeClient extends WebChromeClient implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer player) {
            if (player != null) {
                if (player.isPlaying()) player.stop();
                player.reset();
                player.release();
            }
        }
    }
}
