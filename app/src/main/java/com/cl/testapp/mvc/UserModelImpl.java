package com.cl.testapp.mvc;

import com.cl.testapp.model.HttpResult;
import com.cl.testapp.okhttp.OkUtil;
import com.cl.testapp.okhttp.ResultCallback;

import java.util.List;

import okhttp3.Call;

/**
 * 方法实现
 */
public class UserModelImpl implements UserModel {

    private static final String TAG = "xl";

    @Override
    public void getUsers(final onUserListener listener) {
        OkUtil.post()
                .url("http://47.100.245.128/lingxi-test/user/rc/list")
                .execute(new ResultCallback<HttpResult<List<UserInfo>>>() {
                    @Override
                    public void onSuccess(HttpResult<List<UserInfo>> response) {
                        listener.onSuccess(response.getData());
                    }

                    @Override
                    public void onError(Call call, Exception e) {
                        listener.onError();
                    }
                });
    }
}
