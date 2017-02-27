package com.cl.testapp.mvp;

import com.cl.testapp.mvc.UserInfo;

import java.util.List;

/**
 *
 * Created by Administrator on 2017-02-27.
 */
public class UserModel implements IUserModel{

    private List<UserInfo> mUserList;

    @Override
    public void setUser(List<UserInfo> userList) {
        mUserList = userList;
    }

    @Override
    public List<UserInfo> load() {
        return mUserList;
    }
}
