package com.cl.testapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cl.testapp.R;
import com.cl.testapp.ui.base.BaseActivity;
import com.cl.testapp.util.Util;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AnimateActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_animate)
    ImageView mIvAnimate;

    @BindView(R.id.get_token)
    Button mGetToken;
    @BindView(R.id.get_upload_token)
    Button mGetUploadToken;
    @BindView(R.id.upload_img)
    Button mUploadImg;
    @BindView(R.id.log_info)
    TextView mLogInfo;
    @BindView(R.id.img)
    ImageView mImg;

    private String uid = "";
    private String token = "";
    private String uploadToken = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setToolbar(mToolbar, "元素共享动画测试", true);
        Bundle bundle = getIntent().getExtras();
        String imgUrl = bundle.getString("animate");
        Glide.with(this)
                .load(imgUrl)
                .asBitmap()
                .into(mIvAnimate);
    }

    private void login() {
        String url = "http://192.168.10.123:8008/s_user_auth/gain_user_token";
        String onlineUrl = "http://192.168.10.125:8080/fitness/s_user_auth/gain_user_token";
        OkHttpUtils.post()
                .url(url)
                .addParams("tel", "15570772073")
                .addParams("psw", "abcd1234")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Util.toastShow(AnimateActivity.this, e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Util.toastShow(AnimateActivity.this, response);
                        JsonParser parser = new JsonParser();
                        JsonObject object = parser.parse(response).getAsJsonObject();
                        if (object.get("code").getAsInt() == 200) {
                            JsonObject extra = object.getAsJsonObject("extra");
                            JsonObject data = extra.getAsJsonObject("data");
                            uid = data.get("uid").getAsString();
                            token = data.get("token").getAsString();
                            Util.toastShow(AnimateActivity.this, "登录成功");
                        }
                    }
                });
    }


    private void getUploadToken() {
        String url = "http://192.168.10.123:8008/s_user/gain_upload_token";
        String onlineUrl = "http://keepapp.yamon.com.cn/sports/s_user/gain_upload_token";
        OkHttpUtils.get()
                .url(url)
                .addHeader("yamon-com-uid", uid)
                .addHeader("yamon-com-token", token)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG, "onError: " + e.toString());
                        Util.toastShow(AnimateActivity.this, "错误");
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d(TAG, "onResponse: " + response);
                        JsonParser parser = new JsonParser();
                        JsonObject object = parser.parse(response).getAsJsonObject();
                        if (object.get("code").getAsInt() == 200) {
                            uploadToken = object.get("extra").getAsString();
                            Util.toastShow(AnimateActivity.this, "获取成功");
                        }
                    }
                });
    }

    private void uploadFile() {
        String url = "http://192.168.10.123:8008/s_upload/upload";
        String onlineUrl = "http://keepapp.yamon.com.cn/sports/s_upload/upload";

        File file = new File(Environment.getExternalStorageDirectory(), "20170323.jpg");
        if (!file.exists()) {
            Util.toastShow(this, "文件不存在，请修改文件路径");
            return;
        }

        OkHttpUtils.post()
                .url(url)
                .addHeader("yamon-com-uid", uid)
                .addHeader("yamon-com-token", token)
                .addHeader("yamon-com-upload-token", uploadToken)
                .addParams("phone", "15570772073")
                .addFile("img", "2017-03-20.jpg", file)
//                .addFile("img", "2017-03-21.jpg", file)
//                .addFile("img", "2017-03-22.jpg", file)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.d(TAG, "onError: " + e.toString());
                        Util.toastShow(AnimateActivity.this, e.toString());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Util.toastShow(AnimateActivity.this, response);
                    }
                });
    }

    @OnClick({R.id.get_token, R.id.get_upload_token, R.id.upload_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_token:
                login();
                break;
            case R.id.get_upload_token:
                getUploadToken();
                break;
            case R.id.upload_img:
                uploadFile();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            String filePath = data.getData().toString();
            mLogInfo.setText(filePath);
            Glide.with(this).load(filePath).into(mImg);
        }
    }

    private void okLoad() {
        File file = new File(Environment.getExternalStorageDirectory(), "20170322.jpg");
        if (!file.exists()) {
            Util.toastShow(this, "文件不存在，请修改文件路径");
            return;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("yamin-image-file", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));

        OkHttpClient client = new OkHttpClient();

        MultipartBody requestBody = builder.build();
        //构建请求
        Request request = new Request.Builder()
                .url("http:10.0.2.2:8008/s_upload/upload")//地址
                .post(requestBody)//添加请求体
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Util.toastShow(AnimateActivity.this, e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Util.toastShow(AnimateActivity.this, response.body().string());
            }
        });
    }

    /**
     * 打开相册
     */
    private void getPicture() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        this.startActivityForResult(intent, 0x01);

    }


}
