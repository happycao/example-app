package com.cl.testapp.ui.activity;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cl.testapp.R;
import com.cl.testapp.ui.base.BaseActivity;
import com.cl.testapp.util.IconFont;
import com.cl.testapp.util.Utils;
import com.cl.testapp.widget.areaselect.AreaSelectDialog;
import com.jakewharton.rxbinding.view.RxView;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.iconics.context.IconicsLayoutInflater;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

/**
 * 图标和地区选择
 */
public class IconActivity extends BaseActivity {

    @BindView(R.id.tv_dr_color)
    TextView mTvDrColor;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.tv_area_select)
    TextView mTvAreaSelect;

    private AreaSelectDialog.Builder builder;
    private PopupWindow popup;
    private String mProvince = "";
    private String mCity = "";
    private String mRegion = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LayoutInflaterCompat.setFactory(getLayoutInflater(), new IconicsLayoutInflater(getDelegate()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.icon_activity);
        ButterKnife.bind(this);
        setToolbar(mToolbar, "Icon演示及地区选择", true);
        initDrawable();
    }

    private void initDrawable() {
        Drawable drawable = getResources().getDrawable(R.drawable.love);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(drawable, ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        drawable.setBounds(0, 0, Utils.dp2px(24), Utils.dp2px(24));//边距，长宽
        mTvDrColor.setCompoundDrawables(drawable, null, drawable, null);
        setOnTouch();

        RxView.clicks(mTvAreaSelect)
                .throttleFirst(500, TimeUnit.MILLISECONDS) // 设置防抖间隔为 500ms
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        Log.i("xl", "click " + DateFormat.format("HH:mm:ss",System.currentTimeMillis()));
                        //这里有context的问题，http://stackoverflow.com/questions/31677552/the-activitys-layoutinflater-already-has-a-factory-installed-so-we-can-not-inst
                        if(builder == null) {
                            builder = new AreaSelectDialog.Builder(IconActivity.this)
                                    .setOnSelectListener(new AreaSelectDialog.Builder.OnSelectListener() {
                                        @Override
                                        public void OnSelect(String province, String city, String region) {
                                            mProvince = province;
                                            mCity = city;
                                            mRegion = region;
                                            mTvAreaSelect.setText(province + "" + city + "" + region);
                                        }
                                    });
                        }
                        builder.setSelectedArea(mProvince, mCity, mRegion).show();
                    }
                });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setOnTouch() {
        mTvDrColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        IconicsDrawable icon = new IconicsDrawable(v.getContext()).icon(IconFont.Icon.icon_homepage).sizeDp(144).paddingDp(8).backgroundColor(Color.parseColor("#DDFFFFFF")).roundedCornersDp(12);
                        ImageView imageView = new ImageView(v.getContext());
                        imageView.setImageDrawable(icon);
                        int size = Utils.dp2px(144);
                        popup = new PopupWindow(imageView, size, size);
                        popup.showAsDropDown(v);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (popup != null && popup.isShowing()) {
                            popup.dismiss();
                        }
                        break;
                }
                return false;
            }
        });
    }
}
