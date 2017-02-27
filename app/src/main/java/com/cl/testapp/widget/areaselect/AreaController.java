package com.cl.testapp.widget.areaselect;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * 备用
 * Created by cl on 2016-12-23.
 */

public class AreaController {

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

    private AreaSelectDialog.Builder.OnSelectListener mOnSelectListener;

    public interface OnSelectListener{
        void OnSelect(String province, String city, String region);
    }

    public void setOnSelectListener(AreaSelectDialog.Builder.OnSelectListener onSelectListener) {
        mOnSelectListener = onSelectListener;
    }

    public AreaController(@NonNull Context context, @NonNull AreaSelectDialog dialog) {
        mContext = context;
        mDialog = dialog;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        mContext = context;
    }


    public ImageView getClose() {
        return mClose;
    }

    public void setClose(ImageView close) {
        mClose = close;
    }

    public TabLayout getTabLayout() {
        return mTabLayout;
    }

    public void setTabLayout(TabLayout tabLayout) {
        mTabLayout = tabLayout;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    public List<Area.SubBeanX> getSubBeanXs() {
        return mSubBeanXs;
    }

    public void setSubBeanXs(List<Area.SubBeanX> subBeanXs) {
        mSubBeanXs = subBeanXs;
    }

    public List<Area.SubBeanX.SubBean> getSubBeans() {
        return mSubBeans;
    }

    public void setSubBeans(List<Area.SubBeanX.SubBean> subBeans) {
        mSubBeans = subBeans;
    }

    public List<String> getProvinceList() {
        return provinceList;
    }

    public void setProvinceList(List<String> provinceList) {
        this.provinceList = provinceList;
    }

    public List<String> getCityList() {
        return cityList;
    }

    public void setCityList(List<String> cityList) {
        this.cityList = cityList;
    }

    public List<String> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<String> regionList) {
        this.regionList = regionList;
    }

    public int getProvincePosition() {
        return mProvincePosition;
    }

    public void setProvincePosition(int provincePosition) {
        mProvincePosition = provincePosition;
    }

    public int getCityPosition() {
        return mCityPosition;
    }

    public void setCityPosition(int cityPosition) {
        mCityPosition = cityPosition;
    }

    public int getRegionPosition() {
        return mRegionPosition;
    }

    public void setRegionPosition(int regionPosition) {
        mRegionPosition = regionPosition;
    }


    public void setSelectedArea(String province, String city, String region) {
        mProvince = province;
        mCity = city;
        mRegion = region;
    }

    public void OnSelect(){
        if(mOnSelectListener != null) mOnSelectListener.OnSelect(mProvince, mCity, mRegion);
    }
}
