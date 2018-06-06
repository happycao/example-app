package com.cl.testapp.ui.activity;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.LayoutInflaterCompat;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cl.testapp.R;
import com.cl.testapp.ui.base.BaseActivity;
import com.cl.testapp.util.IconFont;
import com.cl.testapp.util.Util;
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
        setContentView(R.layout.activity_icon);
        ButterKnife.bind(this);
        setToolbar(mToolbar, "Icon演示及地区选择", true);
        initDrawable();
    }

    private void initDrawable() {
        Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        drawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(drawable, ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
        drawable.setBounds(0, 0, Util.dip2px(this, 24), Util.dip2px(this, 24));//边距，长宽
        mTvDrColor.setCompoundDrawables(drawable, null, null, null);

        // SpannableString
        // 文本颜色
        String str = "#话题# 我的表情是这样的 ::like @你好，明天  查看链接>> ";
        SpannableString spannableString = new SpannableString(str);
        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.parseColor("#0099EE"));
        // 话题
        int topicStart = str.indexOf("#");
        int topicEnd = str.indexOf("#", topicStart + 1) + 1;
        spannableString.setSpan(colorSpan, topicStart, topicEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        // 表情
        int imgStart = str.indexOf("::");
        int imgEnd = str.indexOf(" ", imgStart + 1);
        drawable.setBounds(0, 0, 42, 42);
        ImageSpan imageSpan = new ImageSpan(drawable);
        spannableString.setSpan(imageSpan, imgStart, imgEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        // 点击事件
        int atStart = str.indexOf("@");
        int atEnd = str.indexOf(" ", atStart + 1) + 1;
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Util.toastShow(IconActivity.this, "click");
            }
        }, atStart, atEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        // 超链接
        int linkStart = str.indexOf("查看链接>>");
        int linkEnd = str.indexOf(" ", linkStart + 1) + 1;
        URLSpan urlSpan = new URLSpan("http://47.100.245.128");
        spannableString.setSpan(urlSpan, linkStart, linkEnd, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        mTvDrColor.setMovementMethod(LinkMovementMethod.getInstance());
        mTvDrColor.setHighlightColor(Color.parseColor("#36969696"));

        mTvDrColor.setText(spannableString);

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

    private void setOnTouch() {
        mTvDrColor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        IconicsDrawable icon = new IconicsDrawable(v.getContext()).icon(IconFont.Icon.xl_coffee).sizeDp(144).paddingDp(8).backgroundColor(Color.parseColor("#DDFFFFFF")).roundedCornersDp(12);
                        ImageView imageView = new ImageView(v.getContext());
                        imageView.setImageDrawable(icon);
                        int size = Util.dip2px(v.getContext(), 144);
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
