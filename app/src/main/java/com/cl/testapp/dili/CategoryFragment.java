package com.cl.testapp.dili;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cl.testapp.R;
import com.cl.testapp.dili.constant.DApi;
import com.cl.testapp.dili.entity.DCategory;
import com.cl.testapp.dili.entity.DResult;
import com.cl.testapp.okhttp.OkUtil;
import com.cl.testapp.okhttp.ResultCallback;
import com.cl.testapp.ui.base.BaseFragment;
import com.cl.testapp.util.GsonUtil;
import com.cl.testapp.util.SPUtil;
import com.cl.testapp.widget.recycle.GridItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 番剧分类
 */
public class CategoryFragment extends BaseFragment {

    private static final String ARC_TYPE = "arc_type";
    public static final String ONE = "one";
    public static final String TWO = "two";

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private String mArcType;
    private CategoryAdapter mCategoryAdapter;
    private OnCategoryChangeListener mCategoryChangeListener;

    public CategoryFragment() {

    }

    public void setCategoryChangeListener(OnCategoryChangeListener categoryChangeListener) {
        mCategoryChangeListener = categoryChangeListener;
    }

    public static CategoryFragment newInstance(String arcType) {
        CategoryFragment fragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(ARC_TYPE, arcType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArcType = getArguments().getString(ARC_TYPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.d_category_fragment, container, false);
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
        List<DCategory> typeList = new ArrayList<>();
        mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(), 3));
        mRecyclerView.addItemDecoration(new GridItemDecoration());
        mCategoryAdapter = new CategoryAdapter(typeList);
        mRecyclerView.setAdapter(mCategoryAdapter);
        mCategoryAdapter.setOnItemListener(new CategoryAdapter.OnItemListener() {
            @Override
            public void onItemClick(View view, DCategory category) {
                onCategoryChange(category);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    /**
     * 初始化数据，先从缓存拿，没有则网络请求
     */
    private void initData() {
        String arcTypeJson = SPUtil.build().getString(mArcType);
        if (TextUtils.isEmpty(arcTypeJson)) {
            getData();
        } else {
            List<DCategory> list = GsonUtil.toList(arcTypeJson, DCategory[].class);
            setData(list);
        }
    }

    /**
     * 获取网络数据
     */
    private void getData() {
        OkUtil.get()
                .url(DApi.categories)
                .execute(new ResultCallback<DResult<List<DCategory>>>() {
                    @Override
                    public void onSuccess(DResult<List<DCategory>> response) {
                        if (response != null && response.getData() != null) {
                            List<DCategory> categoryList = response.getData();
                            cacheData(categoryList);
                            setData(categoryList);
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
    private void setData(List<DCategory> list) {
        setRefreshFalse();
        mCategoryAdapter.setData(list);
    }

    /**
     * 更新Adapter数据
     */
    private void updateData(List<DCategory> list) {
        setRefreshFalse();
        mCategoryAdapter.updateData(list);
    }

    /**
     * 缓存数据
     */
    private void cacheData(List<DCategory> list) {
        String arcTypeJson = GsonUtil.toJson(list);
        SPUtil.build().putString(mArcType, arcTypeJson);
    }

    /**
     * 设置异常提示
     */
    private void setError() {
        setRefreshFalse();
        showToast("获取失败");
    }

    /**
     * 番剧类型改变
     *
     * @param category 番剧类型
     */
    private void onCategoryChange(DCategory category) {
        if (mCategoryChangeListener != null) {
            mCategoryChangeListener.onCategoryChange(category);
        }
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
}
