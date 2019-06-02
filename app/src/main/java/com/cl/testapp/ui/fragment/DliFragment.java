package com.cl.testapp.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cl.testapp.R;
import com.cl.testapp.constant.Constant;
import com.cl.testapp.model.Animation;
import com.cl.testapp.model.DliAnimation;
import com.cl.testapp.ui.activity.WebVideoActivity;
import com.cl.testapp.ui.adapter.DliAnimationAdapter;
import com.cl.testapp.ui.base.BaseFragment;
import com.cl.testapp.util.GsonUtil;
import com.cl.testapp.util.SPUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 番剧信息
 */
public class DliFragment extends BaseFragment {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.animate_select)
    TextView mAnimateSelect;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;

    private String baseMobileUrl = "http://m.dilidili.name";
    private String baseAnimateUrl = "http://www.dilidili.name/anime/";
    private String[] selectAnimate = {"201904", "201901", "201810", "201807", "201804", "201801", "201710", "201707", "201704", "201701"};
    private String animateUrl = baseAnimateUrl + selectAnimate[0];
    private int mSelect = 0;
    private boolean refresh = false;
    private ListPopupWindow mPopupWindow;

    private DliAnimationAdapter mAdapter;
    private List<DliAnimation> mDliAnimationList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dili_fragment, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    private void init() {
        setupToolbar(mToolbar, "番剧", 0, null);
        initAnimateSelect();
        initRecyclerView();
        if (SPUtil.build().getBoolean(Constant.ANIMATE_CACHE) && !refresh) {
            mAdapter.setData(getData());
            Log.d(TAG, "initAnimateSelect: " + refresh);
        } else {
            analysisDli();
        }
    }

    @OnClick({R.id.animate_select})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.animate_select:
                mPopupWindow.show();
                break;
        }
    }

    private void initAnimateSelect() {
        mSelect = SPUtil.build().getInt(Constant.ANIMATE_SELECT, 0);
        String quarter = SPUtil.build().getString(Constant.ANIMATE_QUARTER, "197001");
        // 此次用于添加了新季度番,刷新本地缓存
        if (!quarter.equals(selectAnimate[mSelect])) {
            mSelect = 0;
            refresh = true;
            SPUtil.build().putString(Constant.ANIMATE_QUARTER, selectAnimate[mSelect]);
        }
        mAnimateSelect.setText(selectAnimate[mSelect]);
        mPopupWindow = new ListPopupWindow(getActivity());
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dili_spinner_item, selectAnimate);
        mPopupWindow.setAdapter(adapter);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setModal(true);
        mPopupWindow.setDropDownGravity(Gravity.END);
        mPopupWindow.setAnchorView(mAnimateSelect);
        mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mAnimateSelect.setText(selectAnimate[position]);
                animateUrl = baseAnimateUrl + selectAnimate[position];
                SPUtil.build().putInt(Constant.ANIMATE_SELECT, position);
                cleanData();
                analysisDli();
                mPopupWindow.dismiss();
            }
        });
    }

    private void initRecyclerView() {
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                cleanData();
                analysisDli();
            }
        });
        mAdapter = new DliAnimationAdapter(getActivity(), this.mDliAnimationList);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, 1));
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new DliAnimationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View paramView, Animation animation) {
                gotoWeb(animation);
            }
        });
    }

    // 解析网页获取番剧信息
    private void analysisDli() {
        if (!mSwipeRefresh.isRefreshing())
            mSwipeRefresh.setRefreshing(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Elements els = Jsoup.connect(animateUrl).timeout(5000).get().select(".anime_list");
                    for (Element el : els) {
                        DliAnimation mDliAnimation = new DliAnimation();
                        List<Animation> animationList = new ArrayList<>();
                        String title = el.getElementsByTag("h2").text();
                        mDliAnimation.setCategory(title);
                        Elements emt = el.getElementsByTag("dl");
                        for (Element e : emt) {
                            Animation mAnimation = new Animation();
                            String name = e.getElementsByTag("h3").first().getElementsByTag("a").text();
                            String url = baseMobileUrl + e.getElementsByTag("a").attr("href").trim();
                            String img = e.getElementsByTag("img").attr("src").trim();
                            mAnimation.setName(name);
                            mAnimation.setLink(url);
                            mAnimation.setImgUrl(img);
                            Elements infoList = e.getElementsByClass("d_label");
                            for (Element ee : infoList) {
                                String aa = ee.getElementsByTag("b").text();
                                switch (aa) {
                                    case "地区：":
                                        mAnimation.setArea(ee.text());
                                        break;
                                    case "年代：":
                                        mAnimation.setYear(ee.text());
                                        break;
                                    case "标签：":
                                        mAnimation.setType(ee.text());
                                        break;
                                    case "播放：":
                                        mAnimation.setHook(ee.text());
                                        break;
                                }
                            }
                            Elements infos = e.getElementsByTag("p");
                            for (Element ee : infos) {
                                String bb = ee.getElementsByTag("b").text();
                                switch (bb) {
                                    case "看点：":
                                        mAnimation.setBroadcast(ee.text());
                                        break;
                                    case "简介：":
                                        mAnimation.setIntroduction(ee.text());
                                        break;
                                    case "状态: ":
                                        mAnimation.setState(ee.text());
                                        break;
                                }
                            }
                            animationList.add(mAnimation);
                        }
                        mDliAnimation.setAnimationList(animationList);
                        mDliAnimationList.add(mDliAnimation);
                    }
                    mRecyclerView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (mSwipeRefresh.isRefreshing())
                            mSwipeRefresh.setRefreshing(false);
                            saveData(mDliAnimationList);
                            mAdapter.setData(mDliAnimationList);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    // 保存番剧信息
    private void saveData(List<DliAnimation> data) {
        String json = GsonUtil.toJson(data);
        SPUtil.build().putBoolean(Constant.ANIMATE_CACHE, true);
        SPUtil.build().putString(Constant.ANIMATE_JSON, json);
    }

    // 获取番剧信息
    private List<DliAnimation> getData() {
        String json = SPUtil.build().getString(Constant.ANIMATE_JSON);
        return GsonUtil.toList(json, DliAnimation[].class);
    }

    // 清除保存信息
    private void cleanData() {
        mDliAnimationList.clear();
        SPUtil.build().putString(Constant.ANIMATE_JSON, "{}");
        SPUtil.build().putBoolean(Constant.ANIMATE_CACHE, false);
    }

    // 前往web页
    private void gotoWeb(Animation animation) {
        Intent intent = new Intent(getActivity(), WebVideoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("tittle", animation.getName());
        bundle.putString("url", animation.getLink());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void getVideo(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Element doc = Jsoup.connect(baseMobileUrl + "/watch3/56212/").get();
                    Log.d(TAG, "run: " + doc.html().trim());
//                    String video = doc.getElementById("videoPlayer").attr("src").trim();
//                    Log.d(TAG, "run: " + video);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }
}
