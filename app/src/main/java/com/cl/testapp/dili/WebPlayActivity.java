package com.cl.testapp.dili;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cl.testapp.R;
import com.cl.testapp.dili.constant.DApi;
import com.cl.testapp.dili.entity.DBangumi;
import com.cl.testapp.dili.entity.DEpisode;
import com.cl.testapp.dili.entity.DResult;
import com.cl.testapp.okhttp.OkUtil;
import com.cl.testapp.okhttp.ResultCallback;
import com.cl.testapp.ui.base.BaseActivity;
import com.cl.testapp.widget.recycle.ItemAnimator;
import com.cl.testapp.widget.recycle.ItemDecoration;
import com.cl.testapp.widget.webview.ChromeClient;
import com.cl.testapp.widget.webview.WebVideoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 播放
 */
public class WebPlayActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.app_bar)
    AppBarLayout mAppBar;
    @BindView(R.id.button_bar)
    RelativeLayout mButtonBar;
    @BindView(R.id.title_name)
    TextView mTitleName;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @BindView(R.id.web_view)
    WebVideoView mWebView;
    @BindView(R.id.video_view)
    FrameLayout mVideoView;
    @BindView(R.id.hive_recycler_view)
    RecyclerView mHiveRecyclerView;
    @BindView(R.id.arc_pic)
    ImageView mArcPic;
    @BindView(R.id.arc_name)
    TextView mArcName;
    @BindView(R.id.arc_type)
    TextView mArcType;
    @BindView(R.id.arc_label)
    TextView mArcLabel;
    @BindView(R.id.arc_desc)
    TextView mArcDesc;
    @BindView(R.id.nested_scroll_view)
    NestedScrollView mNestedScrollView;

    private EpisodeAdapter mEpisodeAdapter;
    private String mVideoUrl = "";

    private ChromeClient mChromeClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_play_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setToolbar(mToolbar, "", true);

        Intent intent = getIntent();
        DBangumi bangumi = (DBangumi) intent.getSerializableExtra("bangumi");
        if (bangumi == null) {
            return;
        }

        initWebView();
        setCategoryInfo(bangumi);

        initRecyclerView();
        getData(bangumi.getId());
        switchTitle();
    }

    /**
     * 初始化WebView
     */
    private void initWebView() {
        String payHtml = "file:///android_asset/arcplayer.html";
        mChromeClient = new ChromeClient(mVideoView, new ChromeClient.onChangedListener() {
            @Override
            public void onFullscreen(boolean fullscreen) {
                if (fullscreen) {
                    mWebView.setVisibility(View.GONE);
                } else {
                    mWebView.setVisibility(View.VISIBLE);
                }
                setFullscreen(fullscreen);
            }
        });
        mWebView.setWebChromeClient(mChromeClient);

        mWebView.loadUrl(payHtml);

    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        ItemDecoration itemDecoration = new ItemDecoration(ItemDecoration.HORIZONTAL, 10, Color.parseColor("#ffffff"));
        itemDecoration.setGoneLast(true);
        // 剧集
        LinearLayoutManager hiveLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mHiveRecyclerView.setLayoutManager(hiveLayoutManager);
        mHiveRecyclerView.setItemAnimator(new ItemAnimator());
        mHiveRecyclerView.addItemDecoration(itemDecoration);

        List<DEpisode> hives = new ArrayList<>();
        mEpisodeAdapter = new EpisodeAdapter(hives);
        mHiveRecyclerView.setAdapter(mEpisodeAdapter);
        mEpisodeAdapter.setOnItemListener(new EpisodeAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, DEpisode episode, int position) {
                getPlayUrls(episode);
            }
        });
    }

    /**
     * 获取网络数据
     */
    private void getData(int arcId) {
        getSeasons(arcId);
        getEpisodes(arcId);
    }

    /**
     * 获取季度
     */
    private void getSeasons(int arcId) {
        OkUtil.get()
                .url(DApi.seasons)
                .addParam("arctype_id", arcId)
                .execute(new ResultCallback<DResult<List<DBangumi>>>() {
                    @Override
                    public void onSuccess(DResult<List<DBangumi>> response) {

                    }

                    @Override
                    public void onError(Call call, Exception e) {

                    }
                });
    }

    /**
     * 获取番剧剧集
     */
    private void getEpisodes(int arcId) {
        OkUtil.get()
                .url(DApi.episodes)
                .addParam("arctype_id", arcId)
                .addParam("size", 100)
                .execute(new ResultCallback<DResult<List<DEpisode>>>() {
                    @Override
                    public void onSuccess(DResult<List<DEpisode>> response) {
                        if (response != null && response.getData() != null) {
                            List<DEpisode> episodeList = response.getData();
                            mEpisodeAdapter.setData(episodeList);
                            mHiveRecyclerView.scrollToPosition(episodeList.size() - 1);
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {

                    }
                });
    }

    /**
     * 获取番剧详情
     */
    private void getPlayUrls(DEpisode episode) {
        int id = episode.getId();
        OkUtil.get()
                .url(DApi.playerUrl)
                .addParam("archive_id", id)
                .execute(new ResultCallback<DResult<List<String>>>() {

                    @Override
                    public void onSuccess(DResult<List<String>> response) {
                        if (response != null && response.getData() != null) {
                            mVideoUrl = response.getData().get(0);
                            loadPlay();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {

                    }
                });
    }

    /**
     * 设置番剧信息
     */
    private void setCategoryInfo(DBangumi bangumi) {
        mTitleName.setText(bangumi.getTypename());
        mArcName.setText(bangumi.getTypename());
        mArcType.setText(bangumi.getZhuangtai());
        mArcLabel.setText(bangumi.getBiaoqian());
        mArcDesc.setText(bangumi.getDescription());

        Glide.with(mArcPic.getContext())
                .load(bangumi.getSuoluetudizhi())
                .into(mArcPic);
    }

    /**
     * 加载播放
     */
    private void loadPlay() {
        if (TextUtils.isEmpty(mVideoUrl)) {
            showToast("获取资源失败，请重试或观看其他番剧");
            return;
        }

        String url = DApi.httpPlayer + mVideoUrl;
        if (mVideoUrl.endsWith("m3u8")) {
            url = DApi.m3u8Player = mVideoUrl;
        }
        setPlayUrl(url);
//        mWebView.loadUrl(url);
    }


    /**
     * 标题切换
     */
    private void switchTitle() {
        mButtonBar.setAlpha(0);
        mAppBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int h = appBarLayout.getTotalScrollRange();
                int offset = Math.abs(verticalOffset);
                if (h == offset) return;

                int bbr = offset - 50 < 0 ? 0 : offset;
                mButtonBar.setAlpha(1f * bbr / h);
            }
        });
    }

    @OnClick({R.id.arc_pic, R.id.web_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.arc_pic:
                onClickWebView();
                break;
            case R.id.web_view:

                break;
        }
    }

    /**
     * 隐藏状态栏
     */
    private void onClickWebView() {
        int visibility = mToolbar.getVisibility();
        showToast(String.valueOf(visibility));
        switch (visibility) {
            case View.VISIBLE:
                mToolbar.setVisibility(View.GONE);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case View.GONE:
                mToolbar.setVisibility(View.VISIBLE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case View.INVISIBLE:
                break;
        }

    }

    /**
     * 设置WebView链接，模拟器5.0失效，暂不使用
     */
    private void setPlayUrl(String playUrl) {
        String script = "javascript:setPlayerUrl('" + playUrl + "')";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWebView.evaluateJavascript(script, new ValueCallback<String>() {
                @Override
                public void onReceiveValue(String value) {
                    Log.d(TAG, "onReceiveValue: " + value);
                }
            });
        } else {
            mWebView.loadUrl(script);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (inCustomView()) {
                    hideCustomView();
                } else {
                    clearWebView();
                    onBackPressed();
                }
                return true;
            default:
                return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 判断是否是全屏
     */
    public boolean inCustomView() {
        return mChromeClient.getCustomView() != null;
    }

    /**
     * 全屏时按返加键执行退出全屏方法
     */
    public void hideCustomView() {
        mChromeClient.onHideCustomView();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        resumeWebView();
        super.onResume();
    }

    @Override
    protected void onPause() {
        pauseWebView();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clearWebView();
        super.onDestroy();
    }

    private void pauseWebView() {
        mWebView.onPause();
        mWebView.pauseTimers();
    }

    private void resumeWebView() {
        mWebView.resumeTimers();
        mWebView.onResume();
    }

    private void clearWebView() {
        mWebView.clearHistory();
        mWebView.clearCache(true);
        mWebView.loadUrl("about:blank");
        mWebView.pauseTimers();
    }
}
