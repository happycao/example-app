package com.cl.testapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.cl.testapp.R;
import com.cl.testapp.ui.base.BaseActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        init();
    }

    private void init() {
        startActivity(new Intent(this, MainActivity.class));
        this.finish();
    }
}
