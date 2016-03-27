package com.science.carnetplus.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.science.carnetplus.R;
import com.science.carnetplus.util.CommonDefine;
import com.science.carnetplus.util.CommonUtils;
import com.science.carnetplus.util.SnackbarUtils;
import com.science.carnetplus.widget.materialProgress.LoadingView;

/**
 * @author 幸运Science-陈土燊
 * @description 忘记密码
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/8
 */

public class ForgetPswActivity extends BaseActivity implements View.OnClickListener {

    private CoordinatorLayout mRootLayout;
    private CoordinatorLayout mCoordinatorSnackbar;
    private EditText mEditMobilePhone;
    private EditText mEditVerify;
    private EditText mEditNewPassword;
    private Button mBtnGetVerifyCode;
    private Button mBtnSure;
    private String mobilePhone, verifyCode, newPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_forget_psw);
        setToolbar(getString(R.string.forget_psw));

        mRootLayout = (CoordinatorLayout) findViewById(R.id.root_layout);
        mCoordinatorSnackbar = (CoordinatorLayout) findViewById(R.id.coordinator_snackbar);
        mEditMobilePhone = (EditText) findViewById(R.id.edit_mobile_phone);
        mEditVerify = (EditText) findViewById(R.id.edit_verify_code);
        mEditNewPassword = (EditText) findViewById(R.id.edit_new_password);
        mBtnGetVerifyCode = (Button) findViewById(R.id.btn_get_verify);
        mBtnSure = (Button) findViewById(R.id.btn_sure);

        CommonUtils.materialRipple(mBtnGetVerifyCode);
        CommonUtils.materialRipple(mBtnSure, "#ffffff");
    }

    @Override
    public void initData() {
        mEditMobilePhone.setText(getIntent().getStringExtra(CommonDefine.MOBILE_PHONE));
    }

    @Override
    public void initListener() {
        mBtnGetVerifyCode.setOnClickListener(ForgetPswActivity.this);
        mBtnSure.setOnClickListener(ForgetPswActivity.this);
        // 点击屏幕隐藏软键盘
        hideKeyBoard(mRootLayout, mEditMobilePhone);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_verify:
                mobilePhone = mEditMobilePhone.getText().toString().trim();
                verifyEdit(0);
                break;

            case R.id.btn_sure:
                mobilePhone = mEditMobilePhone.getText().toString().trim();
                verifyCode = mEditVerify.getText().toString().trim();
                newPassword = mEditNewPassword.getText().toString().trim();
                verifyEdit(1);
                break;
        }
    }

    private void verifyEdit(int type) {
        CommonUtils.hideKeyboard(mEditMobilePhone, ForgetPswActivity.this);
        if (CommonUtils.isMobilePhone(mobilePhone)) {
            if (type == 0) {
                // 点击"验证码"按钮
                requestVerify();
            } else {
                if (!TextUtils.isEmpty(verifyCode)) {
                    if (!TextUtils.isEmpty(newPassword)) {
                        if (CommonUtils.passwordVerify(newPassword)) {
                            resetPassword(); // 确认重置密码
                        } else {
                            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.error_register_password));
                        }
                    } else {
                        SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.error_register_password_null));
                    }
                } else {
                    SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.error_register_verify_address_null));
                }
            }

        } else {
            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.error_mobile_phone));
        }
    }

    /**
     * 发送验证码
     */
    private void requestVerify() {
        CommonUtils.hideKeyboard(mEditMobilePhone, ForgetPswActivity.this);
        AVUser.requestPasswordResetBySmsCodeInBackground(mobilePhone, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    //发送成功了
                    SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.please_receive_verify_code));
                } else {
                    Log.e("ForgetActivity>>>", ">>>>>>>>>>requestVerify:" + e.toString());
                    switch (e.getCode()) {
                        case 124: // TIMEOUT
                            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.time_out));
                            break;
                        case 213: // INVALID_PHONE_NUMBER
                            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.could_not_find_user));
                            break;
                        case 601:
                            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.verify_code_limited_5));
                            break;
                        default:
                            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.network_not_connected));
                            break;
                    }
                }
            }
        });
    }

    /**
     * 确定重置密码
     */
    private void resetPassword() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ForgetPswActivity.this);
        builder.setTitle(getString(R.string.tip));
        View view = LayoutInflater.from(ForgetPswActivity.this).inflate(R.layout.load_progress, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.show();
        final LoadingView loadingView = (LoadingView) view.findViewById(R.id.loading_view);
        final TextView textLoadingTip = (TextView) view.findViewById(R.id.text_loading_tip);

        AVUser.resetPasswordBySmsCodeInBackground(verifyCode, mobilePhone, new UpdatePasswordCallback() {
            @Override
            public void done(final AVException e) {
                if (e == null) {
                    //密码更改成功了！
                    // 保存账号和密码
                    SharedPreferences sharedPreferences = getSharedPreferences(CommonDefine.LOGIN, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(CommonDefine.MOBILE_PHONE, mobilePhone);
                    editor.putString(CommonDefine.PASSWORD, newPassword);
                    editor.commit();
                    textLoadingTip.setText(getString(R.string.reset_password_success));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent mainIntent = new Intent(ForgetPswActivity.this, LoginActivity.class);
                            startActivity(mainIntent);
                            ForgetPswActivity.this.finish();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingView.stopAnimation();
                            dialog.dismiss();
                            Log.e("ForgetPswActivity>>>", ">>>>>>>>>>resetPassword:" + e.toString());
                            switch (e.getCode()) {
                                case 603: //无效的短信验证码
                                    SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.please_get_verify_code_again));
                                    break;
                                case 124: // TIMEOUT
                                    SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.time_out));
                                    break;
                                case 211: // INVALID_PHONE_NUMBER
                                    SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.could_not_find_user));
                                    break;
                                default:
                                    SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.network_not_connected));
                                    break;
                            }
                        }
                    }, 1000);
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
