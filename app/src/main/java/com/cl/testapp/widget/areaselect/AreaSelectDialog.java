package com.cl.testapp.widget.areaselect;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cl.testapp.R;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 地区选择
 * 相关资源如下
 * {@link src.main:assets/area.json},{@link src.main:layout.dialog_area_select}
 * {@link src.main:layout.item_area},{@link src.main:drawable.ic_vector_close}
 * {@link src.main:drawable.ic_vector_select},{@link Area},{@link AreaAdapter}
 * Created by cl on 2016-12-20.
 */

public class AreaSelectDialog extends BottomSheetDialog {

    private BottomSheetBehavior bottomSheetBehavior;

    public AreaSelectDialog(@NonNull Context context) {
        super(context);
    }

    public AreaSelectDialog(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
    }

    protected AreaSelectDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        /**
         * 监听解决hidden未dismiss的问题
         */
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setBottomSheetCallback(mBottomSheetCallback);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int screenHeight = getScreenHeight(getContext());
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight);
    }

    private int getScreenHeight(Context context) {
        Point point = new Point();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getSize(point);
        return point.y;
    }

    public static class Builder {

        private Context mContext;
        private AreaSelectDialog mDialog;
        private ImageView mClose;
        private TabLayout mTabLayout;
        private RecyclerView mRecyclerView;
        //地区数据
        private List<Area> areaList = new ArrayList<>();
        private List<Area.SubBeanX> mSubBeanXs = new ArrayList<>();
        private List<Area.SubBeanX.SubBean> mSubBeans = new ArrayList<>();
        //展示数据
        private List<String> provinceList = new ArrayList<>();
        private List<String> cityList = new ArrayList<>();
        private List<String> regionList = new ArrayList<>();
        //选中位置
        private int mProvincePosition = -1;
        private int mCityPosition = -1;
        private int mRegionPosition = -1;
        //选中地区
        private String mProvince = "";
        private String mCity = "";
        private String mRegion = "";

        private OnSelectListener mOnSelectListener;

        public interface OnSelectListener {
            void OnSelect(String province, String city, String region);
        }

        public Builder setOnSelectListener(OnSelectListener onSelectListener) {
            mOnSelectListener = onSelectListener;
            return this;
        }

        public Builder(@NonNull Context context) {
            mContext = context;
            mDialog = new AreaSelectDialog(mContext);
            View layout = LayoutInflater.from(mContext).inflate(R.layout.dialog_area_select, null);
            mDialog.setContentView(layout);
            mClose = (ImageView) layout.findViewById(R.id.iv_close);
            mTabLayout = (TabLayout) layout.findViewById(R.id.tab_layout);
            mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
            setRecyclerView();
            setClose();
        }

        /**
         * 创建视图
         *
         * @return AreaSelectDialog
         */
        public AreaSelectDialog create() {
            return mDialog;
        }

        /**
         * 关闭事件
         */
        private void setClose() {
            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }

        /**
         * 加载列表
         */
        private void setRecyclerView() {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setNestedScrollingEnabled(false);//嵌套滑动{true or false}
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            areaList = getAreaData();
            mTabLayout.addTab(mTabLayout.newTab().setText(areaList.get(0).getName()));
            setProvince();
            mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    switch (tab.getPosition()) {
                        case 0:
                            setProvince();
                            break;
                        case 1:
                            setCity(mProvincePosition);
                            break;
                        case 2:
                            setRegion(mCityPosition);
                            break;
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }

        /**
         * 设置待选省/市
         */
        private void setProvince() {
            provinceList.clear();
            for (Area area : areaList) {
                provinceList.add(area.getName());
            }
            //此处删除待选中的"请选择"
            provinceList.remove(0);
            final AreaAdapter provinceAdapter = new AreaAdapter(mContext, provinceList);
            mRecyclerView.setAdapter(provinceAdapter);
            //这里-1是原始数据并没有remove"请选择"
            if (mProvincePosition != -1) {
                provinceAdapter.setSelectPosition(mProvincePosition - 1);
                mRecyclerView.scrollToPosition(mProvincePosition - 1);
            }
            provinceAdapter.setOnItemClickListener(new AreaAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    List<Area.SubBeanX> cityList = areaList.get(position + 1).getSub();
                    //这里判断点击的和选中的是否是同一个，如果是tab只改变选中状态，否则移除并清除状态重新加载
                    if (mProvincePosition == position + 1) {
                        if (mTabLayout.getTabCount() == 2) mTabLayout.getTabAt(1).select();
                    } else {
                        mProvincePosition = position + 1;
                        mCityPosition = -1;
                        mRegionPosition = -1;
                        mProvince = areaList.get(mProvincePosition).getName();
                        //如果有没有下级，直接返回数据，有则改变状态
                        if (cityList == null || cityList.size() == 0) {
                            mProvince = areaList.get(mProvincePosition).getName();
                            mTabLayout.getTabAt(0).setText(mProvince);
                            provinceAdapter.setSelectPosition(mCityPosition - 1);
                            if (mOnSelectListener != null)
                                mOnSelectListener.OnSelect(mProvince, "", "");
                            dismiss();
                        } else {
                            mTabLayout.removeAllTabs();
                            mTabLayout.addTab(mTabLayout.newTab().setText(mProvince));
                            mTabLayout.addTab(mTabLayout.newTab().setText(areaList.get(mProvincePosition).getSub().get(0).getName()));
                            mTabLayout.getTabAt(1).select();
                        }
                    }
                }
            });
        }

        /**
         * 设置待选市/区/县
         *
         * @param position 上级位置
         */
        private void setCity(int position) {
            mSubBeanXs = areaList.get(position).getSub();
            cityList.clear();
            for (Area.SubBeanX city : mSubBeanXs) {
                cityList.add(city.getName());
            }
            //此处删除待选中的"请选择"
            cityList.remove(0);
            final AreaAdapter cityAdapter = new AreaAdapter(mContext, cityList);
            mRecyclerView.setAdapter(cityAdapter);
            //这里-1是原始数据并没有remove"请选择"
            if (mCityPosition != -1) {
                cityAdapter.setSelectPosition(mCityPosition - 1);
                mRecyclerView.scrollToPosition(mCityPosition - 1);
            }
            cityAdapter.setOnItemClickListener(new AreaAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    mCityPosition = position + 1;
                    List<Area.SubBeanX.SubBean> regions = mSubBeanXs.get(mCityPosition).getSub();
                    mCity = mSubBeanXs.get(mCityPosition).getName();
                    //如果有没有下级，直接返回数据，有则改变状态
                    if (regions == null || regions.size() == 0) {
                        mTabLayout.getTabAt(1).setText(mCity);
                        cityAdapter.setSelectPosition(mCityPosition - 1);
                        if (mOnSelectListener != null)
                            mOnSelectListener.OnSelect(mProvince, mCity, "");
                        dismiss();
                    } else {
                        mRegionPosition = -1;
                        mTabLayout.removeAllTabs();
                        mTabLayout.addTab(mTabLayout.newTab().setText(mProvince));
                        mTabLayout.addTab(mTabLayout.newTab().setText(mCity));
                        mTabLayout.addTab(mTabLayout.newTab().setText(regions.get(0).getName()));
                        mTabLayout.getTabAt(2).select();
                    }
                }
            });
        }

        /**
         * 设置待选区/县
         *
         * @param position 上级位置
         */
        private void setRegion(int position) {
            mSubBeans = mSubBeanXs.get(position).getSub();
            regionList.clear();
            for (Area.SubBeanX.SubBean region : mSubBeans) {
                regionList.add(region.getName());
            }
            //此处删除待选中的"请选择"
            regionList.remove(0);
            final AreaAdapter regionAdapter = new AreaAdapter(mContext, regionList);
            mRecyclerView.setAdapter(regionAdapter);
            //这里-1是原始数据并没有remove"请选择"
            if (mRegionPosition != -1) {
                regionAdapter.setSelectPosition(mRegionPosition - 1);
                mRecyclerView.scrollToPosition(mRegionPosition - 1);
            }
            regionAdapter.setOnItemClickListener(new AreaAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    mRegionPosition = position + 1;
                    mRegion = mSubBeans.get(mRegionPosition).getName();
                    regionAdapter.setSelectPosition(mRegionPosition);
                    //和上面逻辑不一样，确定有三个tab，所以直接修改值
                    mTabLayout.getTabAt(2).setText(mRegion);
                    if (mOnSelectListener != null)
                        mOnSelectListener.OnSelect(mProvince, mCity, mRegion);
                    dismiss();
                }
            });
        }

        /**
         * 设置已选地区
         *
         * @param province 省/市
         * @param city     市/区/县
         * @param region   区/县
         * @return Builder
         */
        public Builder setSelectedArea(@NonNull String province, @NonNull String city, @NonNull String region) {
            if (province.equals("")) return this;
            mTabLayout.removeAllTabs();
            for (int i = 0; i < areaList.size(); i++) {
                if (TextUtils.equals(areaList.get(i).getName(), province)) {
                    mProvincePosition = i;
                    mProvince = province;
                    mTabLayout.addTab(mTabLayout.newTab().setText(province));
                    mTabLayout.getTabAt(0).select();
                }
            }
            if (city.equals("")) return this;
            List<Area.SubBeanX> cityList = areaList.get(mProvincePosition).getSub();
            if (cityList != null || cityList.size() > 0) {
                for (int i = 0; i < cityList.size(); i++) {
                    if (TextUtils.equals(cityList.get(i).getName(), city)) {
                        mCityPosition = i;
                        mCity = city;
                        mTabLayout.addTab(mTabLayout.newTab().setText(city));
                        mTabLayout.getTabAt(1).select();
                    }
                }
            }
            if (region.equals("")) return this;
            List<Area.SubBeanX.SubBean> regionList = cityList.get(mCityPosition).getSub();
            if (regionList != null || regionList.size() > 0) {
                for (int i = 0; i < regionList.size(); i++) {
                    if (TextUtils.equals(regionList.get(i).getName(), region)) {
                        mRegionPosition = i;
                        mRegion = region;
                        mTabLayout.addTab(mTabLayout.newTab().setText(region));
                        mTabLayout.getTabAt(2).select();
                    }
                }
            }
            return this;
        }

        /**
         * 是否可以被取消{true or false}
         *
         * @return Builder
         */
        public Builder setCancelable(boolean cancelable) {
            mDialog.setCancelable(cancelable);
            return this;
        }

        /**
         * 显示
         *
         * @return Builder
         */
        public Builder show() {
            if (!mDialog.isShowing()) mDialog.show();
            return this;
        }

        /**
         * 隐藏
         *
         * @return Builder
         */
        public Builder dismiss() {
            mDialog.dismiss();
            return this;
        }

        /**
         * 获取地区数据
         *
         * @return List<Area>
         */
        private List<Area> getAreaData() {
            List<Area> areaList = new ArrayList<>();
            try {
                InputStream input = mContext.getAssets().open("area.json");
                String areaStr = InputStreamToString(input);
                Area[] array = new Gson().fromJson(areaStr, Area[].class);
                areaList = new ArrayList(Arrays.asList(array));//Arrays.asList(array)获取固定长度list无法进行操作
            } catch (IOException e) {
                e.printStackTrace();
            }
            return areaList;
        }

        /**
         * InputStream转String
         *
         * @param is InputStream
         * @return String
         * @throws IOException
         */
        private String InputStreamToString(InputStream is) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int i;
            while ((i = is.read()) != -1) {
                baos.write(i);
            }
            return baos.toString();
        }

    }

    /**
     * BottomSheet监听
     */
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, @BottomSheetBehavior.State int newState) {
            //newState 有四个状态 ：
            //展开 BottomSheetBehavior.STATE_EXPANDED
            //收起 BottomSheetBehavior.STATE_COLLAPSED
            //拖动 BottomSheetBehavior.STATE_DRAGGING
            //下滑 BottomSheetBehavior.STATE_SETTLING
            switch (newState) {
                case BottomSheetBehavior.STATE_HIDDEN:
                    dismiss();
                    break;
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            //这里是拖拽中的回调，slideOffset为0-1 完全收起为0 完全展开为1
        }
    };
}
