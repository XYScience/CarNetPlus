package com.science.carnetplus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.science.carnetplus.MainActivity;
import com.science.carnetplus.R;
import com.science.carnetplus.utils.CommonUtils;

/**
 * @author 幸运Science-陈土燊
 * @description 登录界面
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/6
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private CoordinatorLayout mCoordinatorLayout;
    private RelativeLayout mContentLayout;
    private EditText mEditAccount;
    private EditText mEditPassword;
    private Button mBtnLogin;
    private TextView mTextForgetPassword;
    private TextView mTextRegister;
    private CommonUtils mCommonUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_login);

        initView();
        initListener();
    }

    private void initView() {
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mContentLayout = (RelativeLayout) findViewById(R.id.content_layout);
        mEditAccount = (EditText) findViewById(R.id.edit_account);
        mEditPassword = (EditText) findViewById(R.id.edit_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mTextForgetPassword = (TextView) findViewById(R.id.text_forget_password);
        mTextRegister = (TextView) findViewById(R.id.text_register);

        mCommonUtils = CommonUtils.getInstance(LoginActivity.this);
        mCommonUtils.materialRipple(mBtnLogin);
    }

    private void initListener() {
        mBtnLogin.setOnClickListener(this);
        mTextForgetPassword.setOnClickListener(this);
        mTextRegister.setOnClickListener(this);

        // 点击屏幕隐藏软键盘
        mContentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mContentLayout.setFocusable(true);
                mContentLayout.setFocusableInTouchMode(true);
                mContentLayout.requestFocus();
                mCommonUtils.hideKeyboard(mEditAccount);
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                Intent intentMain = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentMain);
                break;

            case R.id.text_forget_password:
                Intent intentForgetPsw = new Intent(LoginActivity.this, ForgetPswActivity.class);
                startActivity(intentForgetPsw);
                break;

            case R.id.text_register:
                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

}
