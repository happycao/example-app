package com.cl.testapp.mvp;

import com.cl.testapp.mvc.UserInfo;

import java.util.List;

/**
 *
 * Created by Administrator on 2017-02-27.
 */
public interface IUserModel {

    void setUser(List<UserInfo> userList);

    List<UserInfo> load();

}
