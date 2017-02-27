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
import com.cl.testapp.ui.base.BaseActivity;
import com.cl.testapp.weixin.WXUtil;
import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 支付宝微信支付，微信登录
 */
public class PayActivity extends BaseActivity {

    private static final String TAG = "xl";

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
        setContentView(R.layout.activity_pay);
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
        //TODO 根据不同参数可以做不同的逻辑判断
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
                doAliPay();
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
     * 支付宝支付
     */
    private void doAliPay() {
        //在服务器生成订单信息
        String payInfo = "";
        //客户端调起支付宝支付
        Alipay alipay = new Alipay(PayActivity.this, payInfo, new Alipay.AlipayResultCallBack() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "支付成功");
            }

            @Override
            public void onDealing() {
                Log.d(TAG, "支付中");
            }

            @Override
            public void onError(int error_code) {
                Log.d(TAG, "支付错误" + error_code);
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
        OkHttpUtils.get().url(url).build().execute(new com.zhy.http.okhttp.callback.StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                Log.i(TAG, response);
                WXUtil wxUtil = WXUtil.getInstance(PayActivity.this);
                wxUtil.doPay(response, new WXUtil.WXPayResultCallBack() {
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
                    //TODO 获取到昵称、性别，头像后进行app用户注册
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
