package com.cl.testapp;

import android.app.Application;

import com.cl.testapp.db.DBManager;
import com.cl.testapp.util.IconFont;
import com.cl.testapp.util.SPUtil;
import com.mikepenz.iconics.Iconics;

/**
 * Application
 * Created by Administrator on 2016-09-18.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        Iconics.init(getBaseContext());
        Iconics.registerFont(new IconFont());
        DBManager.getInstance(getApplicationContext());
        SPUtil.newInstance().init(this);
    }

}
