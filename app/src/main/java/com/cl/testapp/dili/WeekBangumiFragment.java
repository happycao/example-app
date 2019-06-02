package com.cl.testapp.dili;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cl.testapp.R;
import com.cl.testapp.dili.entity.DBangumi;
import com.cl.testapp.ui.base.BaseFragment;
import com.cl.testapp.util.GsonUtil;
import com.cl.testapp.util.Utils;
import com.cl.testapp.widget.recycle.GridItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WeekBangumiFragment extends BaseFragment {

    private static final String WEEK_STRING = "week_string";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private List<DBangumi> mCategoryList;

    public WeekBangumiFragment() {

    }

    public static WeekBangumiFragment newInstance(String weekStr) {
        WeekBangumiFragment fragment = new WeekBangumiFragment();
        Bundle args = new Bundle();
        args.putString(WEEK_STRING, weekStr);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String weekStr = getArguments().getString(WEEK_STRING);
            mCategoryList = GsonUtil.toList(weekStr, DBangumi[].class);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.d_week_bangumi_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        initRecyclerView();
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        GridItemDecoration gridItemDecoration = new GridItemDecoration();
//        gridItemDecoration.setDrawable(Color.parseColor("#f2f2f2"));
        gridItemDecoration.setDecoration(Utils.dp2px(8));
        mRecyclerView.addItemDecoration(gridItemDecoration);

        BangumiAdapter adapter = new BangumiAdapter(mCategoryList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BangumiAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View paramView, DBangumi bangumi) {
                goToArcPlay(bangumi);
            }
        });
    }


    /**
     * 前往播放
     */
    private void goToArcPlay(DBangumi bangumi) {
        Intent intent = new Intent(getActivity(), WebPlayActivity.class);
        intent.putExtra("bangumi", bangumi);
        startActivity(intent);
    }
}
