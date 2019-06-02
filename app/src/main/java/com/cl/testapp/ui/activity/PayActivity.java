package com.cl.testapp.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cl.testapp.R;
import com.cl.testapp.alipay.Alipay;
import com.cl.testapp.model.HttpResult;
import com.cl.testapp.ui.base.BaseActivity;
import com.cl.testapp.weixin.WXUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 支付宝微信支付，微信登录
 */
public class PayActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.alipay)
    Button mAlipay;
    @BindView(R.id.wxpay)
    Button mWxpay;
    @BindView(R.id.wxlogin)
    Button mWxlogin;
    @BindView(R.id.log_info)
    TextView mLogInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_activity);
        ButterKnife.bind(this);
        init();
        showScheme();
    }

    /**
     * 通过scheme从网页调起APP
     * {@code 网页代码：<a href="{scheme}://{host}:{port}/{path}?{key}={value}">go</a>}
     * {@code Android端：在清单文件中注册，<activity><intent-filter></intent-filter></activity>}
     */
    private void showScheme() {
        Intent intent = getIntent();
        Uri uri = intent.getData();
        // TODO 根据不同参数可以做不同的逻辑判断
        if (intent.getScheme() == null) return;
        if (uri == null) return;
        Log.d(TAG, "scheme:" + intent.getScheme());
        Log.d(TAG, "scheme: " + uri.getScheme());
        Log.d(TAG, "host: " + uri.getHost());
        Log.d(TAG, "port: " + uri.getPort());
        Log.d(TAG, "path: " + uri.getPath());
        Log.d(TAG, "queryString: " + uri.getQuery());
        Log.d(TAG, "queryParameter: " + uri.getQueryParameter("key"));
    }

    private void init() {
        setToolbar(mToolbar, "支付和第三方登录", true);
    }

    @OnClick({R.id.alipay, R.id.wxpay, R.id.wxlogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.alipay:
                doHttpAliPay();
                break;
            case R.id.wxpay:
                doWXPay();
                break;
            case R.id.wxlogin:
                doWXLogin();
                break;
        }
    }

    /**
     * 获取服务器数据支付宝支付
     */
    private void doHttpAliPay() {
        //请求自己的服务器获取支付参数
        String url = "http://localhost/test/pay/alipayinfo";

        String json = "{\"id\":\"1804162038498745\"}";
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "支付信息获取失败");
                mLogInfo.post(new Runnable() {
                    @Override
                    public void run() {
                        mLogInfo.append("支付信息获取失败\n");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                String payInfo = null;
                if (!"".equals(json)) {
                    Type type = new TypeToken<HttpResult<String>>() {
                    }.getType();
                    HttpResult<String> resultData = new Gson().fromJson(json, type);
                    payInfo = resultData.getData();
                }
                if (payInfo != null && !"".equals(payInfo)) {
                    final String finalPayInfo = payInfo;
                    mAlipay.post(new Runnable() {
                        @Override
                        public void run() {
                            doAliPay(finalPayInfo);
                        }
                    });
                }
            }
        });
    }

    /**
     * 支付宝支付
     */
    private void doAliPay(String payInfo) {
        //在服务器生成订单信息
        //客户端调起支付宝支付
        Alipay alipay = new Alipay(PayActivity.this, payInfo, new Alipay.AlipayResultCallBack() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "支付成功");
                mLogInfo.append("支付宝支付成功\n");
            }

            @Override
            public void onDealing() {
                Log.d(TAG, "支付中");
                mLogInfo.append("支付宝支付中\n");
            }

            @Override
            public void onError(int error_code) {
                Log.d(TAG, "支付错误" + error_code);
                mLogInfo.append("支付宝支付错误" + error_code + "\n");
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "支付取消");
                mLogInfo.append("支付宝支付取消\n");
            }
        });
        alipay.doPay();
    }

    /**
     * 微信支付
     * 微信支付常见坑
     * 1.微信开放平台的包名和签名是否和本地的一致
     * 2.服务器能拿到prepare_id,还是返回-1，查看调起支付接口时的签名是否计算正确
     * 3.能调起支付，没有返回消息的，请查看自己项目包下是否有（wxapi.WXPayEntryActivity）
     * 4.本地调试时一定要使用正式签名文件进行调试，否则是调不起微信支付窗口的
     * 5.网络上遇到说微信缓存会影响返回-1的，目前没有遇到过
     */
    private void doWXPay() {
        //在服务器生成订单信息
        String response = "";
        WXUtil wxUtil = WXUtil.getInstance(PayActivity.this);
        wxUtil.doPay(response, new WXUtil.WXPayResultCallBack() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "支付成功");
            }

            @Override
            public void onError(int error_code) {
                Log.d(TAG, "支付失败" + error_code);
                mLogInfo.append("微信支付失败" + error_code + "\n");
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "支付取消");
            }
        });
    }

    /**
     * 获取服务器数据微信支付
     */
    private void doHttpWXPay() {
        //请求自己的服务器获取支付参数
        String url = "";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "支付信息获取失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body() != null ? response.body().string() : "{}";
                Log.i(TAG, json);
                WXUtil wxUtil = WXUtil.getInstance(PayActivity.this);
                wxUtil.doPay(json, new WXUtil.WXPayResultCallBack() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "支付成功");
                    }

                    @Override
                    public void onError(int error_code) {
                        Log.d(TAG, "支付失败" + error_code);
                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "支付取消");
                    }
                });
            }
        });
    }

    /**
     * 微信登录
     */
    private void doWXLogin() {
        WXUtil wxUtil = WXUtil.getInstance(PayActivity.this);
        wxUtil.doLogin(new WXUtil.WXLoginResultCallBack() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onSuccess: 登录成功 " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String nickname = jsonObject.getString("nickname");
                    String sex = jsonObject.getInt("sex") == 0 ? "女" : "男";
                    String headImgUrl = jsonObject.getString("headimgurl");
                    // TODO 获取到昵称、性别，头像后进行app用户注册
                    Log.d(TAG, "onResponse: " + nickname + "," + sex);
                    mLogInfo.append(nickname + "登录成功," + sex + "\n");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(int code) {
                Log.d(TAG, "onError: 登录失败");
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: 登录取消");
            }
        });
    }
}
