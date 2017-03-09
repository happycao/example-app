package com.cl.testapp.ui.activity;

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
import com.cl.testapp.widget.WebVideoView;

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
    @BindView(R.id.btn_load_bili)
    Button mBtnLoadBili;
    @BindView(R.id.btn_load_j2js)
    Button mBtnLoadJ2js;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_video);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
//        mWebVideo.loadUrl("http://www.bilibili.com/video/av7449411/");
        mWebVideo.loadUrl("file:///android_asset/java2js.html");
//        mWebVideo.loadUrl("http://192.168.10.120:8020/H5android/index.html");
        mWebVideo.addJavascriptInterface(WebVideoActivity.this, "myObj");
        mWebVideo.addJavascriptInterface(WebVideoActivity.this, "main");

    }

    @OnClick({R.id.btn_load_bili, R.id.btn_load_j2js, R.id.btn_java2js})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_load_bili:
                mWebVideo.loadUrl("http://www.bilibili.com/video/av7449411/");
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


    @JavascriptInterface
    public void callJavaMethod(String message) {
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
        clearVideoView();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        resumeVideoView();
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
