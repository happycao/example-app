package com.cl.testapp.mvp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cl.testapp.R;
import com.cl.testapp.model.User;
import com.cl.testapp.ui.activity.LoginActivity;
import com.cl.testapp.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MVPActivity extends BaseActivity implements UserContract.View{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private UserContract.Presenter mPresenter;
    private UserInfoAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setToolbar(mToolbar, "MVP演示", true);
        mAdapter = new UserInfoAdapter(this, new ArrayList<User>(0));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new UserPresenter(this);
//        mPresenter.loadUsers(this);
    }

    @Override
    public void setPresenter(UserContract.Presenter presenter) {

    }

    @Override
    public void showUsers(List<User> users) {
        mAdapter.replaceData(users);
    }

    @Override
    public void showAddUser() {

    }

    @Override
    public void showNoUsers() {
        startActivityForResult(new Intent(this, LoginActivity.class), LoginActivity.REQUEST_LOGIN);
//        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    public void showSuccess() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPresenter.result(requestCode, resultCode);
    }

}
