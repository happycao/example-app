package com.cl.testapp.dili;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cl.testapp.R;
import com.cl.testapp.dili.constant.DApi;
import com.cl.testapp.dili.entity.DBangumi;
import com.cl.testapp.dili.entity.DCategory;
import com.cl.testapp.dili.entity.DResult;
import com.cl.testapp.okhttp.OkUtil;
import com.cl.testapp.okhttp.ResultCallback;
import com.cl.testapp.ui.base.BaseFragment;
import com.cl.testapp.util.GsonUtil;
import com.cl.testapp.util.SPUtil;
import com.cl.testapp.util.Utils;
import com.cl.testapp.widget.recycle.GridItemDecoration;
import com.cl.testapp.widget.recycle.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class BangumiFragment extends BaseFragment {

    private static final String ARC_TYPE = "arc_type";
    public static final String DEFAULT = "default";

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tip_action)
    TextView mTipAction;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private String mArcTypeStr;
    private BangumiAdapter mAdapter;
    private DCategory mCategory;
    private int mPage = 0;

    public BangumiFragment() {

    }

    public static BangumiFragment newInstance(String arcType) {
        BangumiFragment fragment = new BangumiFragment();
        Bundle args = new Bundle();
        args.putString(ARC_TYPE, arcType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArcTypeStr = getArguments().getString(ARC_TYPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.d_bangumi_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        initRecyclerView();
        initData();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        List<DBangumi> arcList = new ArrayList<>();
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        GridItemDecoration gridItemDecoration = new GridItemDecoration();
//        gridItemDecoration.setDrawable(Color.parseColor("#f2f2f2"));
        gridItemDecoration.setDecoration(Utils.dp2px(8));
        mRecyclerView.addItemDecoration(gridItemDecoration);

        mAdapter = new BangumiAdapter(arcList);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BangumiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View paramView, DBangumi bangumi) {
                goToPlay(bangumi);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPage = 0;
                getData();
            }
        });
        mRecyclerView.addOnScrollListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                mPage++;
                getData();
            }
        });
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mArcTypeStr = SPUtil.build().getString(ARC_TYPE, DEFAULT);
        switch (mArcTypeStr) {
            case DEFAULT:
                getDefaultData();
                break;
            default:
                initArcData(mArcTypeStr);
                break;
        }
    }

    /**
     * 初始化分类番剧
     */
    private void initArcData(String acrTypeStr) {
        DCategory category = GsonUtil.toObject(acrTypeStr, DCategory.class);
        mCategory = category;
        mPage = 0;
        setTipAction(category);
        getArcData(category);
    }

    /**
     * 设置提示
     */
    private void setTipAction(DCategory category) {
        String name = category.getTypename();
        mTipAction.setText(String.format("当前选择=%s=", name));
    }

    /**
     * 番剧分类选择
     */
    public void onCategoryChange(DCategory category) {
        mArcTypeStr = GsonUtil.toJson(category);
        SPUtil.build().putString(ARC_TYPE, mArcTypeStr);

        mCategory = category;
        mPage = 0;
        setTipAction(category);
        getArcData(category);
    }

    /**
     * 获取数据
     */
    private void getData() {
        if (DEFAULT.equals(mArcTypeStr)) {
            getDefaultData();
        } else {
            getArcData(mCategory);
        }
    }

    /**
     * 默认获取新番榜单
     */
    private void getDefaultData() {

    }

    /**
     * 获取分类番剧
     */
    private void getArcData(DCategory category) {
        OkUtil.get()
                .url(DApi.bangumiByCategory)
                .addParam("category", category.getTypename())
                .addParam("size", 15)
                .addParam("page", mPage)
                .execute(new ResultCallback<DResult<List<DBangumi>>>() {
                    @Override
                    public void onSuccess(DResult<List<DBangumi>> response) {
                        if (response != null && response.getData() != null) {
                            List<DBangumi> bangumiList = response.getData();
                            if (mPage == 0) {
                                setData(bangumiList);
                            } else {
                                updateData(bangumiList);
                            }
                        } else {
                            setError();
                        }
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        setError();
                    }
                });
    }

    /**
     * 设置Adapter数据
     */
    private void setData(List<DBangumi> list) {
        setRefreshFalse();
        mAdapter.setData(list);
    }
    /**
     * 更新Adapter数据
     */
    private void updateData(List<DBangumi> list) {
        setRefreshFalse();
        mAdapter.updateData(list);
    }

    /**
     * 设置异常提示
     */
    private void setError() {
        setRefreshFalse();
        showToast("没有更多了╮(╯▽╰)╭");
    }

    /**
     * 结束刷新
     */
    private void setRefreshFalse() {
        boolean refreshing = mSwipeRefreshLayout.isRefreshing();
        if (refreshing) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    /**
     * 前往播放
     */
    private void goToPlay(DBangumi bangumi) {
        Intent intent = new Intent(getActivity(), WebPlayActivity.class);
        intent.putExtra("bangumi", bangumi);
        startActivity(intent);
    }
}
