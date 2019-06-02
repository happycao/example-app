package com.cl.testapp.ui.activity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.cl.testapp.R;
import com.cl.testapp.dili.DiliActivity;
import com.cl.testapp.model.GoBean;
import com.cl.testapp.mvc.UserActivity;
import com.cl.testapp.mvp.MVPActivity;
import com.cl.testapp.ui.adapter.WaterfallAdapter;
import com.cl.testapp.ui.base.BaseActivity;
import com.cl.testapp.util.Shares;

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
        setContentView(R.layout.main_activity);
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
                    case R.id.action_share:
                        Shares.share(MainActivity.this, "http://ww4.sinaimg.cn/large/610dc034jw1f8xz7ip2u5j20u011h78h.jpg");
                        break;
                }
                return false;
            }
        });
    }

    @SuppressLint("ShowToast")
    private void init() {
        toast = Toast.makeText(getApplicationContext(), "再按一次退出APP", Toast.LENGTH_SHORT);
        setToolbar(mToolbar, "DEMO演示", false);
        setSwipeRefresh(mSwipeRefresh, new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(false);
            }
        });
        mGoList = listData();
        mRcList.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        mAdapter = new WaterfallAdapter(this, mGoList);
        mRcList.setAdapter(mAdapter);
    }

    private void initEvent() {
        mAdapter.setOnItemClickListener(new WaterfallAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position, GoBean goBean) {
                if (goBean.getCls().equals(AnimateActivity.class)) {
                    Intent animateIntent = new Intent(MainActivity.this, AnimateActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("animate", mGoList.get(position).getImgUrl());
                    animateIntent.putExtras(bundle);
                    startActivity(animateIntent, ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, view, "animate").toBundle());
                } else {
                    startActivity(new Intent(MainActivity.this, goBean.getCls()));
                }
            }

            @Override
            public void onItemLongClick(View view, int position, GoBean goBean) {
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

    private List<GoBean> listData() {
        List<GoBean> list = new ArrayList<>();
        list.add(new GoBean("图标着色，及地区选择演示"
                , "http://ww2.sinaimg.cn/large/610dc034jw1f8o2ov8xi0j20u00u0q61.jpg"
                , IconActivity.class));
        list.add(new GoBean("垂直的ViewPager演示"
                , "http://ww3.sinaimg.cn/large/610dc034jw1f8mssipb9sj20u011hgqk.jpg"
                , DetailsActivity.class));
        list.add(new GoBean("CheckBox选择演示"
                , "http://ww4.sinaimg.cn/large/610dc034jw1f8lox7c1pbj20u011h12x.jpg"
                , CheckBoxActivity.class));
        list.add(new GoBean("WebView视频与交互"
                , "http://ww1.sinaimg.cn/large/610dc034jw1f8kmud15q1j20u011hdjg.jpg"
                , WebVideoActivity.class));
        list.add(new GoBean("Bottom sheet"
                , "http://ww4.sinaimg.cn/large/610dc034jw1f8xz7ip2u5j20u011h78h.jpg"
                , BottomSheetActivity.class));
        list.add(new GoBean("支付宝微信支付"
                , "http://ww2.sinaimg.cn/large/610dc034jw1f8o2ov8xi0j20u00u0q61.jpg"
                , PayActivity.class));
        list.add(new GoBean("MVC简单样例"
                , "http://ww4.sinaimg.cn/large/610dc034jw1f95hzq3p4rj20u011htbm.jpg"
                , UserActivity.class));
        list.add(new GoBean("MVP简单样例"
                , "http://ww1.sinaimg.cn/large/610dc034jw1f8xff48zauj20u00x5jws.jpg"
                , MVPActivity.class));
        list.add(new GoBean("Animate"
                , "http://ww4.sinaimg.cn/large/610dc034jw1f8bc5c5n4nj20u00irgn8.jpg"
                , AnimateActivity.class));
        list.add(new GoBean("Dilidili"
                , "https://ws1.sinaimg.cn/large/0065oQSqly1g0ajj4h6ndj30sg11xdmj.jpg"
                , DiliActivity.class));
        return list;
    }
}
