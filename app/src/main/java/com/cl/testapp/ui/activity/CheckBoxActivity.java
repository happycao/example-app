package com.cl.testapp.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.cl.testapp.R;
import com.cl.testapp.ui.adapter.CheckboxAdapter;
import com.cl.testapp.ui.base.BaseActivity;
import com.cl.testapp.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;


/**
 * CheckBox演示demo
 */
public class CheckBoxActivity extends BaseActivity {

    private static final String TAG = "CheckBoxActivity";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.checkbox)
    CheckBox mCheckbox;
    @BindView(R.id.btn_del)
    Button mBtnDel;
    @BindView(R.id.edit_layout)
    LinearLayout mEditLayout;

    private CheckboxAdapter adapter;
    private List<String> data = new ArrayList<>();
    private boolean isClick = false;
    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_box_activity);
        ButterKnife.bind(this);
        initToolBar();
        setRecyclerView();
    }

    private void initToolBar() {
        mEditLayout.setVisibility(View.GONE);
        setToolbar(mToolbar, "CHECKBOX选择操作", true);
        mToolbar.inflateMenu(R.menu.menu_edit);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_edit:
                        if (item.getTitle().equals("完成")) {
                            item.setTitle("编辑");
                            mEditLayout.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        } else {
                            item.setTitle("完成");
                            mEditLayout.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        }
                        adapter.setEdit(!adapter.getEditStatus());
                        break;
                }
                return false;
            }
        });
    }

    private void setRecyclerView() {
        for (int i = 0; i < 45; i++) {
            data.add("this is checkbox " + i);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(CheckBoxActivity.this, LinearLayoutManager.VERTICAL, false));
        // 滑动视图后使停止位置正好停在某页的正中间
        new LinearSnapHelper().attachToRecyclerView(mRecyclerView);
        adapter = new CheckboxAdapter(CheckBoxActivity.this, data);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new CheckboxAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Utils.toastShow(view.getContext(), data.get(position));
            }

            @Override
            public void onEditItemClick(View view, int position) {
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
        if (!adapter.getEditStatus()) return;
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

    @OnClick(R.id.btn_del)
    public void onClick(View view) {

    }
}
