package com.cl.testapp.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.cl.testapp.R;
import com.cl.testapp.widget.WebVideoView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }


    private void init() {
        mWebVideo.loadUrl("http://keepapp.yamon.com.cn/FitnessApp/index.html");
        mWebVideo.addJavascriptInterface(WebViewFragment.this, "training");
    }

    @JavascriptInterface
    public void callJavaMethod() {
        Toast.makeText(getActivity(), "训练历史", Toast.LENGTH_SHORT).show();
    }

}
