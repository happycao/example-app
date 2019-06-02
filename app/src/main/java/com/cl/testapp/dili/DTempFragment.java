package com.cl.testapp.dili;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cl.testapp.R;
import com.cl.testapp.dili.entity.DCategory;
import com.cl.testapp.ui.adapter.ViewPagerAdapter;
import com.cl.testapp.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 嘀哩嘀哩动画appApi版本
 */
public class DTempFragment extends BaseFragment implements OnCategoryChangeListener {

    private static final String ARC_TYPE = "type";
    private static final int ARC_LIST_INDEX = 1;

    private String mType;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private ViewPagerAdapter mPagerAdapter;
    private String[] tabNamArray = {"季度列表", "番剧列表", "类型列表"};

    public DTempFragment() {

    }

    public static DTempFragment newInstance(String type) {
        DTempFragment fragment = new DTempFragment();
        Bundle args = new Bundle();
        args.putString(ARC_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARC_TYPE);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.d_temp_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        addFragment(mPagerAdapter);
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setAdapter(mPagerAdapter);
                mTabLayout.setupWithViewPager(mViewPager);
                mViewPager.setCurrentItem(ARC_LIST_INDEX);
            }
        });
    }

    /**
     * 番剧类型选择
     */
    @Override
    public void onCategoryChange(DCategory category) {
        mViewPager.setCurrentItem(ARC_LIST_INDEX);
        BangumiFragment fragment = (BangumiFragment) mPagerAdapter.getItem(ARC_LIST_INDEX);
        fragment.onCategoryChange(category);
    }

    /**
     * 添加Fragment
     */
    private void addFragment(ViewPagerAdapter mPagerAdapter) {
        CategoryFragment oneFragment = CategoryFragment.newInstance(CategoryFragment.TWO);
        CategoryFragment twoFragment = CategoryFragment.newInstance(CategoryFragment.ONE);
        WeekBangumiFragment bangumiFragment = WeekBangumiFragment.newInstance(null);
        mPagerAdapter.addFragment(oneFragment, tabNamArray[0]);
        mPagerAdapter.addFragment(bangumiFragment, tabNamArray[1]);
        mPagerAdapter.addFragment(twoFragment, tabNamArray[2]);

        // 绑定番剧类型改变事件
        oneFragment.setCategoryChangeListener(this);
        twoFragment.setCategoryChangeListener(this);
    }
}
