package com.cl.testapp.okhttp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * author : Bafs
 * e-mail : bafs.jy@live.com
 * time   : 2018/04/29
 * desc   : ok http util
 * version: 1.0
 */
public class OkUtil {

    private static final long DEFAULT_MILLISECONDS = 50000;      //默认的超时时间

    @SuppressLint("StaticFieldLeak")
    private static OkUtil mInstance;

    private Application context;
    private Handler mDelivery;
    private OkHttpClient okHttpClient;

    private OkUtil() {
        mDelivery = new Handler(Looper.getMainLooper());

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggerInterceptor loggingInterceptor = new HttpLoggerInterceptor(getClass().getName());
        builder.addInterceptor(loggingInterceptor);

        builder.readTimeout(OkUtil.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.writeTimeout(OkUtil.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        builder.connectTimeout(OkUtil.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        okHttpClient = builder.build();
    }

    public static OkUtil newInstance() {
        if (mInstance == null) {
            synchronized (OkUtil.class) {
                if (mInstance == null) {
                    mInstance = new OkUtil();
                }
            }
        }
        return mInstance;
    }

    /**
     * 初始化
     */
    public OkUtil init(Application app) {
        context = app;
        return this;
    }

    /**
     * get请求
     */
    public static GetRequest get() {
        return new GetRequest();
    }

    /**
     * post请求
     */
    public static PostRequest post() {
        return new PostRequest();
    }

    public Handler getDelivery() {
        return mDelivery;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    /**
     * 取消所有请求请求
     */
    public void cancelAll() {
        for (Call call : getOkHttpClient().dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getOkHttpClient().dispatcher().runningCalls()) {
            call.cancel();
        }
    }
}
