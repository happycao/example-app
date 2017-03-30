package com.cl.testapp.util;

import android.webkit.JavascriptInterface;

/**
 * js
 * Created by Administrator on 2017-03-14.
 */

public class JsObject {

    @JavascriptInterface
    public void callJavaMethod() {
        System.out.println("被调用");
    }
}
