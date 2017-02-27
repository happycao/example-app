package com.cl.testapp.ui.base;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.cl.testapp.R;

/**
 * BaseActivity
 * Created by Administrator on 2016-12-29.
 */

public class BaseActivity extends AppCompatActivity {

    protected static final String TAG = "xl";

    /**
     * 设置Toolbar
     * @param toolbar Toolbar
     * @param titleId 标题id
     * @param isBack 是否显示返回按钮
     */
    protected void setToolbar(@NonNull Toolbar toolbar,@NonNull int titleId,@NonNull boolean isBack){
        setToolbar(toolbar, getString(titleId), isBack);
    }

    /**
     * 设置Toolbar
     * @param toolbar Toolbar
     * @param title 标题
     * @param isBack 是否显示返回按钮
     */
    protected void setToolbar(@NonNull Toolbar toolbar,@NonNull String title,@NonNull boolean isBack){
        TextView mTvTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTvTitle.setText(title);
        if (isBack) {
            toolbar.setNavigationIcon(android.support.design.R.drawable.abc_ic_ab_back_material);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
    }

    /**
     * 设置刷新
     * @param swipeRefresh SwipeRefreshLayout
     * @param listener 监听
     */
    protected void setSwipeRefresh(@NonNull final SwipeRefreshLayout swipeRefresh, @NonNull final SwipeRefreshLayout.OnRefreshListener listener){
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(listener);
    }
}
