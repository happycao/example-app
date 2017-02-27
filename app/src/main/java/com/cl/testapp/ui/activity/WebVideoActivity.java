package com.cl.testapp.ui.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.cl.testapp.R;
import com.cl.testapp.widget.WebVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 网页视频播放
 */
public class WebVideoActivity extends AppCompatActivity {

    @BindView(R.id.web_video)
    WebVideoView mWebVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_video);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mWebVideo.loadUrl("http://www.bilibili.com/video/av7449411/");
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
