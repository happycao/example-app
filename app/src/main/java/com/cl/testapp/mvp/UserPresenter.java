package com.cl.testapp.mvp;

import android.app.Activity;
import android.content.Context;

import com.cl.testapp.db.DBManager;
import com.cl.testapp.model.User;
import com.cl.testapp.ui.activity.LoginActivity;

import java.util.List;

/**
 *
 * Created by Administrator on 2017-02-27.
 */
public class UserPresenter implements UserContract.Presenter{

    private final UserContract.View mUserView;

    public UserPresenter(UserContract.View view) {
        mUserView = view;
        mUserView.setPresenter(this);
    }

    @Override
    public void start(Context context) {
        loadUsers(context);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        if (LoginActivity.REQUEST_LOGIN == requestCode && Activity.RESULT_OK == resultCode)
            mUserView.showSuccess();
    }

    @Override
    public void loadUsers(Context context) {
        List<User> userList = DBManager.getInstance(context).getUserList();
        mUserView.showUsers(userList);
    }
}
