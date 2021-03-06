package com.cl.testapp.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.Toast;

import com.cl.testapp.R;
import com.cl.testapp.ui.base.BaseActivity;
import com.cl.testapp.widget.webview.WebVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 网页视频播放
 */
public class WebVideoActivity extends BaseActivity {

    @BindView(R.id.web_video)
    WebVideoView mWebVideo;
    @BindView(R.id.btn_java2js)
    Button mBtnJava2js;
    @BindView(R.id.btn_load_url)
    Button mBtnLoadBili;
    @BindView(R.id.btn_load_j2js)
    Button mBtnLoadJ2js;

    private String mUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        Bundle bundle = null;
        if (intent != null) {
            bundle = intent.getExtras();
        }
        if (bundle != null) {
            mUrl = bundle.getString("url");
            bundle.getString("tittle");
        } else {
            mUrl = "http://www.bilibili.com";
        }
        mWebVideo.loadUrl(mUrl);
        mWebVideo.addJavascriptInterface(WebVideoActivity.this, "main");

    }

    @OnClick({R.id.btn_load_url, R.id.btn_load_j2js, R.id.btn_java2js})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_load_url:
                mWebVideo.loadUrl(mUrl);
                break;
            case R.id.btn_load_j2js:
                mWebVideo.loadUrl("file:///android_asset/java2js.html");
                break;
            case R.id.btn_java2js:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    mWebVideo.evaluateJavascript("javascript:getGreetings(" + "'hello world!'" + ")", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            Log.d(TAG, "onReceiveValue: " + value);
                        }
                    });
                } else {
                    mWebVideo.loadUrl("javascript:javaCallJs(" + "'Message From Java'" + ")");
                }
                break;
        }
    }

    @JavascriptInterface
    public void jsCallJava(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        resumeVideoView();
        super.onResume();
    }

    @Override
    protected void onPause() {
        pauseVideoView();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clearVideoView();
        super.onDestroy();
    }

    private void pauseVideoView() {
        if (mWebVideo != null) {
            mWebVideo.onPause();
            mWebVideo.pauseTimers();
        }
    }

    private void resumeVideoView() {
        if (mWebVideo != null) {
            mWebVideo.resumeTimers();
            mWebVideo.onResume();
        }
    }

    private void clearVideoView() {
        if (mWebVideo != null) {
            mWebVideo.clearHistory();
            mWebVideo.clearCache(true);
            mWebVideo.loadUrl("about:blank");
            mWebVideo.pauseTimers();
        }
    }
}
