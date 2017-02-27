package com.cl.testapp.mvp;

import com.cl.testapp.mvc.UserInfo;

import java.util.List;

/**
 *
 * Created by Administrator on 2017-02-27.
 */
public interface IUserView {

    List<UserInfo> getUserList();

    void setUserList(List<UserInfo> userList);
}
