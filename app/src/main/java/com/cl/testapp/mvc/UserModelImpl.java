package com.cl.testapp.mvc;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;

/**
 *
 * Created by Administrator on 2017-02-27.
 */

public class UserModelImpl implements UserModel {

    private static final String TAG = "xl";

    @Override
    public void getUsers(final onUserListener listener) {
        OkHttpUtils.get()
                .url("http://139.224.128.232:10660/LiaoLiao/api/user/getUsers")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.onError();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        Type jsonType = new TypeToken<HttpResult<List<UserInfo>>>() {
                        }.getType();
                        HttpResult<List<UserInfo>> result = gson.fromJson(response, jsonType);
                        List<UserInfo> userList = result.getData();
                        listener.onSuccess(userList);
                    }
                });
    }
}
