package com.cl.testapp.mvc;

import java.util.List;

/**
 *
 * Created by Administrator on 2017-02-27.
 */

public interface onUserListener {

    void onSuccess(List<UserInfo> userList);
    void onError();
}
