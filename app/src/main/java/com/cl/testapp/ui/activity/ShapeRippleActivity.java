package com.cl.testapp.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cl.testapp.R;
import com.cl.testapp.ui.base.BaseActivity;
import com.rodolfonavalon.shaperipplelibrary.ShapeRipple;
import com.rodolfonavalon.shaperipplelibrary.model.Circle;
import com.rodolfonavalon.shaperipplelibrary.model.Image;
import com.rodolfonavalon.shaperipplelibrary.model.Square;
import com.rodolfonavalon.shaperipplelibrary.model.Star;
import com.rodolfonavalon.shaperipplelibrary.model.Triangle;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import static com.cl.testapp.R.id.ripple;

/**
 * 波纹扩散
 */
public class ShapeRippleActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(ripple)
    ShapeRipple mRipple;
    @BindView(R.id.bottom_sheet)
    LinearLayout mBottomSheet;
    @BindView(R.id.tv_bottom_title)
    TextView mTvBottomTitle;
    @BindView(R.id.enable_single_ripple)
    AppCompatCheckBox mEnableSingleRipple;
    @BindView(R.id.enable_stroke_ripple)
    AppCompatCheckBox mEnableStrokeRipple;
    @BindView(R.id.enable_color_transition)
    AppCompatCheckBox mEnableColorTransition;
    @BindView(R.id.enable_random_color)
    AppCompatCheckBox mEnableRandomColor;
    @BindView(R.id.enable_random_position)
    AppCompatCheckBox mEnableRandomPosition;
    @BindView(R.id.rg_ripple)
    RadioGroup mRgRipple;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shape_ripple);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setToolbar(mToolbar, "波纹扩散与Bottom sheet", true);
        initShapeRipple();
        mRgRipple.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_circle:
                        mRipple.setRippleShape(new Circle());
                        break;
                    case R.id.rb_square:
                        mRipple.setRippleShape(new Square());
                        break;
                    case R.id.rb_triangle:
                        mRipple.setRippleShape(new Triangle());
                        break;
                    case R.id.rb_star:
                        mRipple.setRippleShape(new Star());
                        break;
                    case R.id.rb_image:
                        mRipple.setRippleShape(new Image(R.drawable.love));
                        break;
                }
            }
        });
    }

    private void initShapeRipple() {
        //Circle Square Triangle Star
        mRipple.setRippleShape(new Image(R.drawable.love));//图形
        mRipple.setRippleDuration(2300);//持续时间
        mRipple.setRippleInterval(1.5f);//时间间隔
        mRipple.setEnableRandomPosition(true);//随机位置
        mRipple.setEnableColorTransition(true);//颜色透明
        mRipple.setEnableSingleRipple(false);//单一的
        mRipple.setEnableRandomColor(false);//随机颜色
        mRipple.setEnableStrokeStyle(false);//中空
        mRipple.setVisibility(View.GONE);
    }

    private void setColor() {
        mRipple.setRippleColor(Color.parseColor("#ff0000"));//颜色
        mRipple.setRippleFromColor(Color.parseColor("#dd0000"));
    }

    private void hide() {
        mRipple.setVisibility(View.GONE);
    }

    private void show() {
        mRipple.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.tv_bottom_title)
    public void onClick() {
        if (mRipple.isShown()) {
            hide();
        } else {
            show();
        }
    }

    @OnCheckedChanged({R.id.enable_single_ripple, R.id.enable_stroke_ripple, R.id.enable_color_transition, R.id.enable_random_color, R.id.enable_random_position})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.enable_color_transition:
                mRipple.setEnableColorTransition(isChecked);
                break;
            case R.id.enable_single_ripple:
                mRipple.setEnableSingleRipple(isChecked);
                break;
            case R.id.enable_random_position:
                mRipple.setEnableRandomPosition(isChecked);
                break;
            case R.id.enable_random_color:
                mRipple.setEnableRandomColor(isChecked);
                break;
            case R.id.enable_stroke_ripple:
                mRipple.setEnableStrokeStyle(isChecked);
                break;
        }
    }
}
