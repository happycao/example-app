package com.cl.testapp.ui.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cl.testapp.R;
import com.cl.testapp.model.GoBean;
import com.cl.testapp.mvc.UserActivity;
import com.cl.testapp.ui.adapter.WaterfallAdapter;
import com.cl.testapp.ui.base.BaseActivity;
import com.cl.testapp.util.Shares;
import com.cl.testapp.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    @BindView(R.id.rc_list)
    RecyclerView mRcList;
    private WaterfallAdapter mAdapter;
    private List<GoBean> mGoList;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        initSearchView();
        initEvent();
    }

    private void initSearchView() {
        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_login:
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        break;
                    case R.id.action_search:
                        break;
                }
                return false;
            }
        });
        Glide.with(MainActivity.this)
                .load("http://ww2.sinaimg.cn/large/610dc034jw1f8o2ov8xi0j20u00u0q61.jpg")
//                .bitmapTransform(new CropCircleTransformation(MainActivity.this))
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Bitmap resizeBmp = ThumbnailUtils.extractThumbnail(resource, Util.dip2px(MainActivity.this, 48), Util.dip2px(MainActivity.this, 48));
                        Drawable drawable = new BitmapDrawable(resizeBmp);
                        mToolbar.setLogo(drawable);
                    }
                });
    }

    private void init() {
        toast = Toast.makeText(getApplicationContext(), "再按一次退出APP", Toast.LENGTH_SHORT);
        setToolbar(mToolbar, "DEMO演示", false);
        setSwipeRefresh(mSwipeRefresh, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(false);
            }
        });
        mGoList = new ArrayList<>();
        mGoList.add(new GoBean("图标着色，及地区选择演示"
                , "http://ww2.sinaimg.cn/large/610dc034jw1f8o2ov8xi0j20u00u0q61.jpg"
                , IconActivity.class));
        mGoList.add(new GoBean("垂直的ViewPager演示"
                , "http://ww3.sinaimg.cn/large/610dc034jw1f8mssipb9sj20u011hgqk.jpg"
                , DetailsActivity.class));
        mGoList.add(new GoBean("CheckBox选择演示"
                , "http://ww4.sinaimg.cn/large/610dc034jw1f8lox7c1pbj20u011h12x.jpg"
                , CheckBoxActivity.class));
        mGoList.add(new GoBean("WebView播放视频"
                , "http://ww1.sinaimg.cn/large/610dc034jw1f8kmud15q1j20u011hdjg.jpg"
                , WebVideoActivity.class));
        mGoList.add(new GoBean("波纹扩散与Bottom sheet"
                , "http://ww4.sinaimg.cn/large/610dc034jw1f8xz7ip2u5j20u011h78h.jpg"
                , ShapeRippleActivity.class));
        mGoList.add(new GoBean("系统分享"
                , "http://ww1.sinaimg.cn/large/610dc034jw1f8xff48zauj20u00x5jws.jpg"
                , null));
        mGoList.add(new GoBean("支付宝微信支付"
                , "http://ww2.sinaimg.cn/large/610dc034jw1f8o2ov8xi0j20u00u0q61.jpg"
                , PayActivity.class));
        mGoList.add(new GoBean("MVC简单样例"
                , "http://ww1.sinaimg.cn/large/610dc034jw1f8kmud15q1j20u011hdjg.jpg"
                , UserActivity.class));
        mRcList.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new WaterfallAdapter(this, mGoList);
        mRcList.setAdapter(mAdapter);
    }

    private void initEvent() {
        mAdapter.setOnItemClickListener(new WaterfallAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                GoBean goBean = mGoList.get(position);
                if (goBean.getCls() == null) {
                    Shares.share(MainActivity.this, mGoList.get(position).getImgUrl());
                }else {
                    if (goBean.getCls().equals(AnimateActivity.class)) {
                        Intent animateIntent = new Intent(MainActivity.this, AnimateActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("animate", mGoList.get(position).getImgUrl());
                        animateIntent.putExtras(bundle);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            startActivity(animateIntent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, view, "animate").toBundle());
                        } else {
                            startActivity(animateIntent);
                        }
                    } else {
                        startActivity(new Intent(MainActivity.this, goBean.getCls()));
                    }
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
//                mAdapter.removeData(position);
            }
        });
    }

    @Override
    public void onBackPressed() {
        quitToast();
    }

    private void quitToast() {
        if (toast.getView().getParent() == null) {
            toast.show();
        } else {
            System.exit(0);
        }
    }
}
