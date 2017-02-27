package com.cl.testapp.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cl.testapp.R;
import com.cl.testapp.ui.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * RecycleView的item选中
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_INDEX = "index";

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_pl)
    TextView mTvPl;
    private ImageAdapter mAdapter;

    private int mIndex;

    public PlaceholderFragment() {

    }

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARG_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        mTvPl.setText("第 " + mIndex + " 页");
        List<String> imgUrl = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            imgUrl.add("http://ww4.sinaimg.cn/large/610dc034jw1f8bc5c5n4nj20u00irgn8.jpg");
        }
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        mAdapter = new ImageAdapter(getActivity(), imgUrl);
        mRecyclerView.setAdapter(mAdapter);
    }

}
