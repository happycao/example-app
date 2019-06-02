package com.cl.testapp.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cl.testapp.R;
import com.cl.testapp.db.DBManager;
import com.cl.testapp.model.User;
import com.cl.testapp.ui.base.BaseActivity;
import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func2;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

    public static final int REQUEST_LOGIN = 101;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.et_username)
    AppCompatEditText mEtUsername;
    @BindView(R.id.et_password)
    AppCompatEditText mEtPassword;
    @BindView(R.id.btn_submit)
    Button mBtnSubmit;
    @BindView(R.id.et_search)
    AppCompatEditText mEtSearch;
    @BindView(R.id.log_info)
    TextView mLogInfo;

    private int num = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setToolbar(mToolbar, "登录和RxJava使用", true);

        /**
         * 响应式，输入用户名密码才能点确定
         */
        Observable<CharSequence> usernameOb = RxTextView.textChanges(mEtUsername);
        Observable<CharSequence> pwdOb = RxTextView.textChanges(mEtPassword);
        Observable.combineLatest(usernameOb, pwdOb, new Func2<CharSequence, CharSequence, Boolean>() {

            @Override
            public Boolean call(CharSequence username, CharSequence pwd) {
                return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(pwd);
            }
        }).subscribe(new Action1<Boolean>() {

            @Override
            public void call(Boolean isLogin) {
                mBtnSubmit.setEnabled(isLogin);
            }
        });

        /**
         * 点击且防止被多次点击
         */
        RxView.clicks(mBtnSubmit).throttleFirst(1L, TimeUnit.SECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Log.i("xl", "click");
                String name = mEtUsername.getText().toString().trim();
                int age = 19;
                try {
                    age = Integer.parseInt(mEtPassword.getText().toString().trim());
                } catch (NumberFormatException e) {
                    mLogInfo.append("psd:" + mEtPassword.getText().toString().trim() + "\n");
                }
                String info = mEtSearch.getText().toString().trim();
                User user = new User(name, age, 0, info);
                try {
                    DBManager.getInstance(LoginActivity.this).insertUser(user);
                    mLogInfo.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                    mLogInfo.setText("创建失败\n");
                }
                List<User> userList = DBManager.getInstance(LoginActivity.this).getUserList();
                num = userList.size();
                for (User u : userList) {
                    mLogInfo.append("User:id=" + u.getId() + "，name=" + u.getName() + "，age=" + u.getAge() + ",info=" + u.getInfo() + "\n");
                }
            }
        });

        mEtSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {
                    Log.i("xl", "手指弹起时执行确认功能");
                    mLogInfo.append("Search:" + mEtSearch.getText().toString().trim() + "\n");
                    return true;
                }
                return false;
            }
        });
    }

}
