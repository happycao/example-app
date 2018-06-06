package com.cl.testapp.mvc;

import java.util.List;

interface UserModel {

    void getUsers(onUserListener listener);

    interface onUserListener {
        void onSuccess(List<UserInfo> userList);
        void onError();
    }
}
