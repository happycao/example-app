package com.cl.testapp.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cl.testapp.R;
import com.cl.testapp.ui.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * BottomSheet
 */
public class BottomSheetActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
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
        setContentView(R.layout.bottom_sheet_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setToolbar(mToolbar, "Bottom sheet", true);
        mRgRipple.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_circle:
                        break;
                    case R.id.rb_square:
                        break;
                    case R.id.rb_triangle:
                        break;
                    case R.id.rb_star:
                        break;
                    case R.id.rb_image:
                        break;
                }
            }
        });
    }


    @OnClick(R.id.tv_bottom_title)
    public void onClick() {

    }

    @OnCheckedChanged({R.id.enable_single_ripple, R.id.enable_stroke_ripple, R.id.enable_color_transition, R.id.enable_random_color, R.id.enable_random_position})
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.enable_color_transition:
                break;
            case R.id.enable_single_ripple:
                break;
            case R.id.enable_random_position:
                break;
            case R.id.enable_random_color:
                break;
            case R.id.enable_stroke_ripple:
                break;
        }
    }
}
