package com.cl.testapp.mvc;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cl.testapp.R;
import com.cl.testapp.ui.adapter.CheckboxAdapter;
import com.cl.testapp.ui.base.BaseActivity;

import java.util.ArrayList;
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
        mUserModel.getUsers(new onUserListener() {
            @Override
            public void onSuccess(List<UserInfo> userList) {
                List<String> data = new ArrayList<>();
                for (UserInfo user: userList){
                    data.add(user.getAccount());
                }
                setRecyclerView(data);
            }

            @Override
            public void onError() {

            }
        });
    }

    public void setRecyclerView(List<String> data) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        CheckboxAdapter adapter = new CheckboxAdapter(this, data);
        mRecyclerView.setAdapter(adapter);
    }
}
