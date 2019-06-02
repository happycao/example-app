package com.cl.testapp.ui.base;

import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.cl.testapp.R;
import com.cl.testapp.util.Utils;

/**
 * BaseFragment
 * Created by Administrator on 2017-02-07.
 */

public class BaseFragment extends Fragment {

    public static final String TAG = "BaseFragment";

    public void showToast(@StringRes int msgId) {
        Utils.toastShow(getContext(), getString(msgId));
    }

    public void showToast(String msg) {
        Utils.toastShow(getContext(), msg);
    }

    /**
     * setupToolbar
     * @param toolbar Toolbar
     * @param titleId TitleId
     * @param menuId MenuId
     * @param listener Menu监听
     */
    public void setupToolbar(@NonNull Toolbar toolbar, @StringRes int titleId, int menuId, Toolbar.OnMenuItemClickListener listener) {
        setupToolbar(toolbar, getString(titleId), menuId, listener);
    }

    /**
     * setupToolbar
     * @param toolbar Toolbar
     * @param title Title
     * @param menuId MenuId
     * @param listener Menu监听
     */
    public void setupToolbar(@NonNull Toolbar toolbar, @NonNull String title , int menuId, Toolbar.OnMenuItemClickListener listener) {
        toolbar.setTitle(title);
        toolbar.setTitleTextAppearance(getActivity(), R.style.AppTheme_TextAppearance);
        if (listener != null) {
            toolbar.inflateMenu(menuId);
            toolbar.setOnMenuItemClickListener(listener);
        }
    }
}
