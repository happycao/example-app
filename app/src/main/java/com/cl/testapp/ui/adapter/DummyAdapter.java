package com.cl.testapp.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.cl.testapp.ui.fragment.PlaceholderFragment;

/**
 * 垂直ViewPagerAdapter
 * Created by Administrator on 2017-02-07.
 */

public class DummyAdapter extends FragmentPagerAdapter {

    public DummyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "index" + position;
    }

}