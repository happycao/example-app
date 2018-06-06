package com.cl.testapp.mvc;

import com.cl.testapp.model.HttpResult;
import com.cl.testapp.util.JsonCallback;
import com.zhy.http.okhttp.OkHttpUtils;

import java.util.List;

import okhttp3.Call;

/**
 * 方法实现
 */
public class UserModelImpl implements UserModel {

    private static final String TAG = "xl";

    @Override
    public void getUsers(final onUserListener listener) {
        OkHttpUtils.post()
                .url("http://47.100.245.128/lingxi-test/user/rc/list")
                .build()
                .execute(new JsonCallback<HttpResult<List<UserInfo>>>() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        listener.onError();
                    }

                    @Override
                    public void onResponse(HttpResult<List<UserInfo>> response, int id) {
                        listener.onSuccess(response.getData());
                    }
                });
    }
}
