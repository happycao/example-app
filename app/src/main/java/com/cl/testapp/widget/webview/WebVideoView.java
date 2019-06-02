package com.cl.testapp.widget.webview;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cl.testapp.BuildConfig;

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

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
        setWebViewClient(new WebClient());
        setWebChromeClient(new ChromeClient());
        // 相关设置
        WebSettings webSettings = getSettings();
        // 插件状态
        webSettings.setPluginState(WebSettings.PluginState.ON);
        // 排版适应屏幕
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        // JavaScript
        webSettings.setJavaScriptEnabled(true);
        // 允许访问文件
        webSettings.setAllowFileAccess(true);

        webSettings.setDatabaseEnabled(false);

        webSettings.setDomStorageEnabled(true);
        // 保存表单数据
        webSettings.setSaveFormData(true);
        // 缓存
        webSettings.setAppCacheEnabled(true);
        // 缓存模式
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // setUseWideViewPort方法设置webview推荐使用的窗口
        // setLoadWithOverviewMode方法是设置webview加载的页面的模式。
        webSettings.setLoadWithOverviewMode(false);
        // 隐藏缩放按钮
        webSettings.setBuiltInZoomControls(true);
        // 可任意比例缩放
        webSettings.setUseWideViewPort(true);
        // 不显示webview缩放按钮
        webSettings.setDisplayZoomControls(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.getMixedContentMode();
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            webSettings.setMediaPlaybackRequiresUserGesture(false);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (BuildConfig.DEBUG) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
    }


    private class WebClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 非http调起其他app
            if (!url.startsWith("http")) {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    view.getContext().startActivity(intent);
                    return true;
                } catch (ActivityNotFoundException ignored) {
                    return true;
                }
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

    private class ChromeClient extends WebChromeClient implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer player) {
            if (player != null) {
                if (player.isPlaying()) {
                    player.stop();
                }
                player.reset();
                player.release();
            }
        }
    }
}
