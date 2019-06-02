package com.cl.testapp.dili;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cl.testapp.R;
import com.cl.testapp.dili.constant.DApi;
import com.cl.testapp.dili.entity.DBangumi;
import com.cl.testapp.dili.entity.DResult;
import com.cl.testapp.dili.entity.DWeek;
import com.cl.testapp.okhttp.OkUtil;
import com.cl.testapp.okhttp.ResultCallback;
import com.cl.testapp.ui.adapter.ViewPagerAdapter;
import com.cl.testapp.ui.base.BaseFragment;
import com.cl.testapp.util.GsonUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 嘀哩嘀哩动画appApi版本
 */
public class DHomeFragment extends BaseFragment {

    private static final String PARAM = "param";

    private String mParam;
    private String[] weekArray = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private ViewPagerAdapter mPagerAdapter;

    public DHomeFragment() {

    }

    public static DHomeFragment newInstance() {
        DHomeFragment fragment = new DHomeFragment();
        Bundle args = new Bundle();
        args.putString(PARAM, "");
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam = getArguments().getString(PARAM);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.d_home_fragment, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        mTabLayout.setupWithViewPager(mViewPager);
        getWeekData();
    }

    /**
     * 获取番剧时间表
     */
    private void getWeekData() {
        OkUtil.get()
                .url(DApi.home)
                .execute(new ResultCallback<DResult<DWeek>>() {
                    @Override
                    public void onSuccess(DResult<DWeek> response) {
                        if (response != null && response.getData() != null) {
                            DWeek data = response.getData();
                            List<List<DBangumi>> weeks = data.getWeeks();
                            if (weeks != null) {
                                setFragment(weeks);
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

    private void setFragment(List<List<DBangumi>> weeks) {
        int size = weeks.size();
        for (int i = 0, length = weekArray.length; i < length; i++) {
            List<DBangumi> weekList = new ArrayList<>();
            if (i < size) {
                weekList = weeks.get(i);
            }
            WeekBangumiFragment fragment = WeekBangumiFragment.newInstance(GsonUtil.toJson(weekList));
            mPagerAdapter.addFragment(fragment, weekArray[i]);
        }
        mViewPager.post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setAdapter(mPagerAdapter);
                mViewPager.setCurrentItem(getWeekDay());
            }
        });
    }

    private int getWeekDay() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        int weekday = c.get(Calendar.DAY_OF_WEEK);
        if (weekday == 1) {
            return 6;
        } else  {
            return weekday - 2;
        }
    }

    /**
     * 设置异常提示
     */
    private void setError() {
        showToast("获取失败╮(╯▽╰)╭");
    }
}
