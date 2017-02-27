package com.cl.testapp.mvp;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.cl.testapp.R;
import com.cl.testapp.mvc.UserInfo;
import com.cl.testapp.ui.base.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MVPActivity extends BaseActivity implements IUserView{

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    UserPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        presenter = new UserPresenter(this);
        presenter.loadUser();
    }

    @Override
    public List<UserInfo> getUserList() {
        return null;
    }

    @Override
    public void setUserList(List<UserInfo> userList) {

    }
}
