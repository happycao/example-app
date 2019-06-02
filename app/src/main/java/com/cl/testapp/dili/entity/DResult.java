package com.cl.testapp.dili.entity;

/**
 * author : happyc
 * e-mail : bafs.jy@live.com
 * time   : 2019/05/28
 * desc   : dili返回结果
 * version: 1.0
 */
public class DResult<T> {

    private int code;
    private String message;
    private T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
