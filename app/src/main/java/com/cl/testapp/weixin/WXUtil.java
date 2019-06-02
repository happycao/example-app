package com.cl.testapp.weixin;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.cl.testapp.okhttp.OkUtil;
import com.cl.testapp.okhttp.ResultCallback;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;

/**
 * 微信登录支付
 * Created by tsy on 16/6/1.
 */
public class WXUtil {

    private static String TAG = "xl";

    private static WXUtil mWXUtil;
    private IWXAPI mWXApi;
    private static WXLoginResultCallBack mLoginCallBack;
    private WXPayResultCallBack mPayCallback;

    private static final String WX_AppId = "wxa66075612b1fc1f3";
    private static final String WX_AppSecret = "微信appsecret";

    private static final int ERROR_AUTH_DENIED = 5;//用户拒绝登录授权
    private static final int ERROR_NO_TOKEN = 6;//登录获取token失败
    private static final int ERROR_NO_USER_INFO = 7;//获取用户信息失败

    /**
     * 登录回调接口
     */
    public interface WXLoginResultCallBack {
        void onSuccess(String response); //登录成功
        void onError(int error_code);   //登录失败
        void onCancel();    //登录取消
    }

    private static final int NO_OR_LOW_WX = 1;   //未安装微信或微信版本过低
    private static final int ERROR_PAY_PARAM = 2;  //支付参数错误
    private static final int ERROR_PAY = 3;  //支付失败

    /**
     * 支付回调接口
     */
    public interface WXPayResultCallBack {
        void onSuccess(); //支付成功
        void onError(int error_code);   //支付失败
        void onCancel();    //支付取消
    }

    private WXUtil(Context context) {
        mWXApi = WXAPIFactory.createWXAPI(context, WX_AppId);
        mWXApi.registerApp(WX_AppId);
    }

    public static WXUtil getInstance(Context context) {
        if (mWXUtil == null) {
            mWXUtil = new WXUtil(context);
        }
        return mWXUtil;
    }

    public IWXAPI getWXApi() {
        return mWXApi;
    }

    /**
     * 发起微信登录
     */
    public void doLogin(WXLoginResultCallBack callback) {
        mLoginCallBack = callback;
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "wechat_sdk_demo_test";
        mWXApi.sendReq(req);
    }

    //登录回调响应
    public void onLoginResp(BaseResp baseResp) {
        if (mLoginCallBack == null) {
            return;
        }
        if (baseResp.errCode == 0) {
            //成功并获取token
            getWXToken(((SendAuth.Resp) baseResp).code);
        } else if (baseResp.errCode == -4) {
            //错误
            mLoginCallBack.onError(ERROR_AUTH_DENIED);
            mLoginCallBack = null;
        } else if (baseResp.errCode == -2) {
            //取消
            mLoginCallBack.onCancel();
            mLoginCallBack = null;
        }
    }

    /**
     * 获取微信access_token
     *
     * @param code 第一步获取的code参数
     * @code 返回示例：{ "access_token":"ACCESS_TOKEN", "expires_in":7200, "refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE","unionid":"o6_bmasdasdsad6_2sgVt7hMZOPfL" }
     */
    public static void getWXToken(String code) {
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + WX_AppId + "&secret=" + WX_AppSecret + "&code=" + code + "&grant_type=authorization_code";
        OkUtil.get().url(url).execute(new ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onResponse: " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String accessToken = jsonObject.getString("access_token");
                    getWXUserInfo(accessToken);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Call call, Exception e) {
                mLoginCallBack.onError(ERROR_NO_TOKEN);
                mLoginCallBack = null;
            }
        });
    }

    /**
     * 刷新access_token，一般情况用不到
     *
     * @param refreshToken 填写通过access_token获取到的refresh_token参数
     * @code 返回示例：{ "access_token":"ACCESS_TOKEN", "expires_in":7200,"refresh_token":"REFRESH_TOKEN", "openid":"OPENID", "scope":"SCOPE" }
     */
    public void refreshWXToken(String refreshToken) {
        String url = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + WXUtil.WX_AppId + "&grant_type=refresh_token&refresh_token=" + refreshToken;
        OkUtil.get().url(url).execute(new ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onResponse: " + response);
            }

            @Override
            public void onError(Call call, Exception e) {
            }
        });
    }

    /**
     * 获取微信登录的用户信息
     *
     * @param accessToken 通过code获得的access_token参数
     * @code 返回示例：{"openid":"OPENID","nickname":"昵称","sex":1,"language":"zh_CN","city":"","province":"","country":"CN","headimgurl":"HEADIMGURL","privilege":[],"unionid":"oZ8SGwnEYH_A__Zq3rm0MFx6_AJ8"}
     */
    public static void getWXUserInfo(String accessToken) {
        String url = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + WX_AppId;
        OkUtil.get().url(url).execute(new ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "onResponse: " + response);
                mLoginCallBack.onSuccess(response);
                mLoginCallBack = null;
            }

            @Override
            public void onError(Call call, Exception e) {
                mLoginCallBack.onError(ERROR_NO_USER_INFO);
                mLoginCallBack = null;
            }
        });
    }

    /**
     * 发起微信支付
     */
    public void doPay(String pay_param, WXPayResultCallBack callback) {
        mPayCallback = callback;

        if (!check()) {
            if (mPayCallback != null) {
                mPayCallback.onError(NO_OR_LOW_WX);
            }
            return;
        }
        JSONObject param = null;
        try {
            param = new JSONObject(pay_param);
        } catch (JSONException e) {
            e.printStackTrace();
            if (mPayCallback != null) {
                mPayCallback.onError(ERROR_PAY_PARAM);
            }
            return;
        }
        if (TextUtils.isEmpty(param.optString("appid")) || TextUtils.isEmpty(param.optString("partnerid")) || TextUtils.isEmpty(param.optString("prepayid")) || TextUtils.isEmpty(param.optString("package")) || TextUtils.isEmpty(param.optString("noncestr")) || TextUtils.isEmpty(param.optString("timestamp")) || TextUtils.isEmpty(param.optString("sign"))) {
            if (mPayCallback != null) {
                mPayCallback.onError(ERROR_PAY_PARAM);
            }
            return;
        }
        PayReq req = new PayReq();
        req.appId = param.optString("appid");
        req.partnerId = param.optString("partnerid");
        req.prepayId = param.optString("prepayid");
        req.packageValue = param.optString("package");
        req.nonceStr = param.optString("noncestr");
        req.timeStamp = param.optString("timestamp");
        req.sign = param.optString("sign");
        mWXApi.sendReq(req);
    }

    //支付回调响应
    public void onPayResp(int error_code) {
        if (mPayCallback == null) {
            return;
        }
        if (error_code == 0) {   //成功
            mPayCallback.onSuccess();
        } else if (error_code == -1) {   //错误
            mPayCallback.onError(ERROR_PAY);
        } else if (error_code == -2) {   //取消
            mPayCallback.onCancel();
        }
        mPayCallback = null;
    }

    //检测是否支持微信支付
    private boolean check() {
        return mWXApi.isWXAppInstalled() && mWXApi.getWXAppSupportAPI() >= Build.EMOJI_SUPPORTED_SDK_INT;
    }
}
