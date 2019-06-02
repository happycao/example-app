package com.cl.testapp.okhttp;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * author : Bafs
 * e-mail : bafs.jy@live.com
 * time   : 2018/04/29
 * desc   : post请求
 * version: 1.0
 */
public class PostRequest {

    private static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    private String mUrl;
    private Gson mGson;
    private Handler mDelivery;
    private OkHttpClient mOkHttpClient;

    private LinkedHashMap<String, String> headers = new LinkedHashMap<>();
    private LinkedHashMap<String, String> params = new LinkedHashMap<>();
    private LinkedHashMap<String, List<String>> urlParams = new LinkedHashMap<>();
    private boolean isSpliceUrl;
    private boolean isJsonRequest;
    private String mJson;


    public PostRequest() {
        OkUtil okUtil = OkUtil.newInstance();
        mDelivery = okUtil.getDelivery();
        mOkHttpClient = okUtil.getOkHttpClient();

        // json date format
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                try {
                    return format.parse(json.getAsJsonPrimitive().getAsString());
                } catch (ParseException e) {
                    return null;
                }
            }
        });
        mGson = gsonBuilder.create();
    }

    /** 设置url */
    public PostRequest url(@NonNull String url) {
        this.mUrl = url;
        return this;
    }

    /** 添加请求头 */
    public PostRequest addHeader(@NonNull String key, String value) {
        if (value == null) return this;
        this.headers.put(key, value);
        return this;
    }

    /** 添加参数 */
    public PostRequest addParam(@NonNull String key, Integer value) {
        if (value == null) return this;
        this.params.put(key, String.valueOf(value));
        return this;
    }

    /** 添加参数 */
    public PostRequest addParam(@NonNull String key, String value) {
        if (value == null) return this;
        this.params.put(key, value);
        return this;
    }

    /** 添加数组参数 */
    public PostRequest addUrlParams(@NonNull String key, List<String> values) {
        if (values == null || values.size() == 0) return this;
        this.isSpliceUrl = true;
        this.urlParams.put(key, values);
        return this;
    }

    /** 提交json数据 */
    public PostRequest postJson(@NonNull String json) {
        this.isJsonRequest = true;
        this.mJson = json;
        return this;
    }

    private Headers getHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
        if (headers != null && !headers.isEmpty()) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                headerBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        return headerBuilder.build();
    }

    private RequestBody getFormBody() {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }
        }
        return builder.build();
    }

    private RequestBody getJsonBody () {
        return RequestBody.create(JSON, mJson);
    }

    private Request getRequest() {
        Request.Builder builder = new Request.Builder();
        builder.headers(getHeaders());
        if (isSpliceUrl) mUrl = createUrlFromParams(mUrl, urlParams);
        builder.url(mUrl);
        if (isJsonRequest) {
            builder.post(getJsonBody());
        } else {
            builder.post(getFormBody());
        }
        return builder.build();
    }

    /** 异步请求 */
    public void execute(@NonNull final ResultCallback callback) {
        mOkHttpClient.newCall(getRequest()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setOnError(call, e, callback);
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {
                try {
                    ResponseBody body = response.body();
                    if (body != null) {
                        setOnSuccess(body.charStream(), callback);
                    } else {
                        setOnError(call, new Exception("body is null"), callback);
                    }
                } catch (Exception e) {
                    setOnError(call, e, callback);
                }

            }
        });
    }

    /** 设置请求成功回调 */
    private void setOnSuccess(final Reader reader, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(mGson.fromJson(reader, callback.getType()));
            }
        });
    }

    /** 设置请求失败回调 */
    private void setOnError(final Call call, final Exception e, final ResultCallback callback) {
        mDelivery.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(call, e);
            }
        });
    }

    private String createUrlFromParams(String url, Map<String, List<String>> params) {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(url);
            if (url.indexOf('&') > 0 || url.indexOf('?') > 0) sb.append("&");
            else sb.append("?");
            for (Map.Entry<String, List<String>> urlParams : params.entrySet()) {
                List<String> urlValues = urlParams.getValue();
                for (String value : urlValues) {
                    //对参数进行 utf-8 编码,防止头信息传中文
                    String urlValue = URLEncoder.encode(value, "UTF-8");
                    sb.append(urlParams.getKey()).append("=").append(urlValue).append("&");
                }
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }
}
