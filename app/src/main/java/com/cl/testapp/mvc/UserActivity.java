package com.cl.testapp.mvc;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cl.testapp.R;
import com.cl.testapp.ui.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private UserModel mUserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setToolbar(mToolbar, "MVC样例", true);
        mUserModel = new UserModelImpl();
        mUserModel.getUsers(new UserModel.onUserListener() {
            @Override
            public void onSuccess(List<UserInfo> userList) {
                setRecyclerView(userList);
            }

            @Override
            public void onError() {

            }
        });
    }

    public void setRecyclerView(List<UserInfo> userList) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        UserAdapter adapter = new UserAdapter(this, userList);
        mRecyclerView.setAdapter(adapter);
    }
}
