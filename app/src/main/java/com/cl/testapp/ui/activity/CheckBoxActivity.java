package com.cl.testapp.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;

import com.cl.testapp.R;
import com.cl.testapp.ui.adapter.CheckboxAdapter;
import com.cl.testapp.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;


/**
 * CheckBox演示demo
 */
public class CheckBoxActivity extends BaseActivity {

    private static final String TAG = "CheckBoxActivity";

    @BindView(R.id.checkbox)
    CheckBox mCheckbox;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    private CheckboxAdapter adapter;
    private List<String> data = new ArrayList<>();
    private boolean isClick = false;
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_box);
        ButterKnife.bind(this);
        setToolbar(mToolbar, "CHECKBOX选择操作", true);
        setRecyclerView();
    }

    private void setRecyclerView() {
        for (int i = 0; i < 45; i++) {
            data.add("this is checkbox " + i);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CheckBoxActivity.this, LinearLayoutManager.VERTICAL, false));
        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);//滑动视图后使停止位置正好停在某页的正中间
        adapter = new CheckboxAdapter(CheckBoxActivity.this, data);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CheckboxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                boolean[] check = adapter.getCheckAll();
                isClick = true;
                num = 0;
                int length = 0;
                for (int i = 0; i < data.size(); i++) {
                    if (check[i]) {
                        num = num + i;
                        length++;
                    }
                }
                if (data.size() == length) {
                    mCheckbox.setChecked(true);
                } else {
                    mCheckbox.setChecked(false);
                }
                adapter.setCheckAll(check);
                mCheckbox.setText(String.valueOf(num));
                isClick = false;
            }
        });
    }

    @OnCheckedChanged(R.id.checkbox)
    public void onCheckedChanged(boolean isChecked) {
        boolean[] check = new boolean[data.size()];
        int a = 0;
        for (int i = 0; i < data.size(); i++) {
            check[i] = isChecked;
            a = a + i;
        }
        adapter.setCheckAll(check);
        if (isClick) {
            isClick = false;
        } else {
            if (isChecked) {
                num = a;
            } else {
                num = 0;
            }
            mCheckbox.setText(String.valueOf(num));
        }
    }
}
