package com.cl.testapp.model;

/**
 * 首页跳转
 * Created by Administrator on 2017-02-24.
 */

public class GoBean {

    private String title;
    private String imgUrl;

    public GoBean(String title, String imgUrl, Class<?> cls) {
        this.title = title;
        this.imgUrl = imgUrl;
        this.cls = cls;
    }

    private Class<?> cls;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }
}
