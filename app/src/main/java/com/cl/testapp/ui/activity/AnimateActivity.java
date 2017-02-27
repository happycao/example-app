package com.cl.testapp.ui.activity;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cl.testapp.R;
import com.cl.testapp.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AnimateActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_animate)
    ImageView mIvAnimate;
    ViewGroup mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animate);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setToolbar(mToolbar, "元素共享动画测试", true);
        Bundle bundle = getIntent().getExtras();
        String imgUrl = bundle.getString("animate");
        Glide.with(this)
                .load(imgUrl)
                .asBitmap()
                .into(mIvAnimate);
    }

    private void getConcactFromDB() {
        Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                //模拟从数据库中获取数据
                ArrayList<String> list = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    list.add("user name:" + i);
                }
                //模拟耗时操作
                SystemClock.sleep(5000);

                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<String>>() {
                    @Override
                    public void call(List<String> list) {
                        Log.d("testapp", "更新界面：" + list.size());
                    }
                });
    }

    private void startTimerTask() {
        Observable.timer(2, TimeUnit.SECONDS).subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Log.d("testapp", "start execute task:" + Thread.currentThread().getName());
            }
        });
    }

}
