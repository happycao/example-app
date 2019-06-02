package com.cl.testapp.ui.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cl.testapp.R;
import com.cl.testapp.widget.webview.WebVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebViewFragment extends Fragment {

    @BindView(R.id.web_video)
    WebVideoView mWebVideo;

    public WebViewFragment() {

    }

    public static WebViewFragment newInstance() {
        return new WebViewFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.web_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }


    private void init() {
        mWebVideo.loadUrl("http://www.bilibili.com");
    }

    @Override
    public void onDestroy() {
        mWebVideo.destroy();
        super.onDestroy();
    }
}
