package com.cl.testapp.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cl.testapp.R;
import com.cl.testapp.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AnimateActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.iv_animate)
    ImageView mIvAnimate;
    @BindView(R.id.input_info)
    EditText mInputInfo;
    @BindView(R.id.btn_topic)
    Button mBtnTopic;
    @BindView(R.id.btn_at)
    Button mBtnAt;
    @BindView(R.id.btn_moe)
    Button mBtnMoe;
    @BindView(R.id.log_info)
    TextView mLogInfo;

    private StringBuilder stringBuilder = new StringBuilder();

    private List<String> mActionList = new ArrayList<>();
    private List<ForegroundColorSpan> mColorSpans = new ArrayList<>();
    private boolean isInAfter = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animate_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setToolbar(mToolbar, "元素共享动画测试", true);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String imgUrl = bundle.getString("animate");
            Glide.with(this)
                    .load(imgUrl)
                    .asBitmap()
                    .into(mIvAnimate);
        }

        mInputInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) return;
                // 查找话题和@
                String content = s.toString();
                mActionList.clear();
                mActionList.addAll(findAction(content));

                // 首先移除之前设置的colorSpan
                Editable editable = mInputInfo.getText();
                for (ForegroundColorSpan mColorSpan : mColorSpans) {
                    editable.removeSpan(mColorSpan);
                }
                mColorSpans.clear();
                // 设置前景色colorSpan
                int findPos = 0;
                for (String topic : mActionList) {
                    findPos = content.indexOf(topic, findPos);
                    if (findPos != -1) {
                        ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.rgb(3, 169, 244));
                        editable.setSpan(colorSpan, findPos, findPos = findPos + topic.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        mColorSpans.add(colorSpan);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mInputInfo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN && isInAfter) {

                    int selectionStart = mInputInfo.getSelectionStart();
                    int selectionEnd = mInputInfo.getSelectionEnd();
                    // 如果光标起始和结束在同一位置,说明是选中效果,直接返回 false 交给系统执行删除动作
                    if (selectionStart != selectionEnd) {
                        return false;
                    }

                    Editable editable = mInputInfo.getText();
                    String content = editable.toString();
                    int lastPos = 0;
                    // 遍历判断光标的位置
                    for (String action : mActionList) {
                        lastPos = content.indexOf(action, lastPos);
                        if (lastPos != -1) {
                            if (selectionStart != 0 && selectionStart >= lastPos && selectionStart <= (lastPos + action.length())) {
                                //选中话题
                                mInputInfo.setSelection(lastPos, lastPos + action.length());
                                return true;
                            }
                        }
                        lastPos += action.length();
                    }
                }
                return false;
            }
        });
    }

    @OnClick({R.id.btn_topic, R.id.btn_at, R.id.btn_moe})
    public void onClick(View view) {
        Editable text = mInputInfo.getText();
        stringBuilder = new StringBuilder();
        stringBuilder.append(text);
        switch (view.getId()) {
            case R.id.btn_topic:
                stringBuilder.append("#你看看你#");
                break;
            case R.id.btn_at:
                stringBuilder.append("@你说你啊 ");
                break;
            case R.id.btn_moe:
                stringBuilder.append("(❤ω❤)");
                break;
        }
        setText();
    }

    private void setText() {
        mInputInfo.setText(stringBuilder.toString());
        mInputInfo.setSelection(stringBuilder.length());
    }

    // 正则表达式,一定要和服务器以及 iOS 端统一
    private static final String TOPIC = "#([^#]+?)#";
    private static final String AT = "@[\\w\\p{InCJKUnifiedIdeographs}-]{1,26} ";
    private static final String ALL = "(" + AT + ")" + "|" + "(" + TOPIC + ")";

    private static ArrayList<String> findAction(String s) {

        Pattern p = Pattern.compile(ALL);
        Matcher m = p.matcher(s);

        ArrayList<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        return list;
    }

}
