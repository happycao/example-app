package com.cl.testapp.ui.base;

import android.content.pm.ActivityInfo;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.cl.testapp.R;
import com.cl.testapp.util.Utils;

/**
 * BaseActivity
 * Created by Administrator on 2016-12-29.
 */
public class BaseActivity extends AppCompatActivity {

    protected static final String TAG = "xl";

    // svg
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public void showToast(@StringRes int msgId) {
        Utils.toastShow(this, getString(msgId));
    }

    public void showToast(String msg) {
        Utils.toastShow(this, msg);
    }

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
        TextView textView = toolbar.findViewById(R.id.toolbar_title);
        if (textView!= null) {
            textView.setText(title);
        }
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

    /**
     * 切换全屏，屏幕常量
     * @param fullscreen 是否全屏
     */
    public void setFullscreen(boolean fullscreen) {
        if (fullscreen) {
            // 设置横屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            // 隐藏状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // 常亮
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            // 设置竖屏
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            // 显示状态栏
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // 清除常亮
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }
}
