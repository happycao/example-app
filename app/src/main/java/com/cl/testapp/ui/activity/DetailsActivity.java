package com.cl.testapp.ui.activity;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.cl.testapp.R;
import com.cl.testapp.ui.adapter.DummyAdapter;
import com.cl.testapp.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.castorflex.android.verticalviewpager.VerticalViewPager;

/**
 * 图片主体色获取及垂直的ViewPager
 */
public class DetailsActivity extends BaseActivity {

    @BindView(R.id.vertical_viewpager)
    VerticalViewPager mViewPager;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setToolbar(mToolbar, "主体色与垂直ViewPager", true);
        final String imgUrl = "http://ww4.sinaimg.cn/large/610dc034jw1f8bc5c5n4nj20u00irgn8.jpg";
        // 使用Glide获取网络图片转为Bitmap
        Glide.with(DetailsActivity.this)
                .load(imgUrl)
                .asBitmap()
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        //通过Palette获取Bitmap主体色
                        Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {
                                Palette.Swatch swatch = palette.getLightVibrantSwatch();
                                if (swatch != null) {
                                    mToolbar.setBackgroundColor(swatch.getRgb());
                                    Log.i("xl", "swatch.getRgb() " + swatch.getRgb());
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        getWindow().setStatusBarColor(swatch.getRgb());
                                        //底部导航栏
                                        //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
                                    }
                                }
                            }
                        });

                    }
                });
        mViewPager.setAdapter(new DummyAdapter(getSupportFragmentManager()));
    }
}
