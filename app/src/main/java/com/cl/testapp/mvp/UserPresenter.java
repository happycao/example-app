package com.cl.testapp.mvp;

import com.cl.testapp.mvc.UserInfo;

import java.util.List;

/**
 *
 * Created by Administrator on 2017-02-27.
 */
public class UserPresenter {

    private IUserView mUserView;
    private IUserModel mUserModel;

    public UserPresenter(IUserView view) {
        mUserView = view;
        mUserModel = new UserModel();
    }

    public void saveUser(List<UserInfo> userList){
        mUserModel.setUser(userList);
    }

    public void loadUser(){
        List<UserInfo> userList = mUserModel.load();
        mUserView.setUserList(userList);
    }
}
