package com.science.carnetplus.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.science.carnetplus.MainActivity;
import com.science.carnetplus.R;
import com.science.carnetplus.utils.AVOSUtils;
import com.science.carnetplus.utils.CommonDefine;
import com.science.carnetplus.utils.CommonUtils;
import com.science.carnetplus.utils.FileUtil;
import com.science.carnetplus.utils.SnackbarUtils;
import com.science.carnetplus.widget.materialProgress.LoadingView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author 幸运Science-陈土燊
 * @description 登录界面
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/6
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, AVOSUtils.OnGetAvatarListener {

    private CoordinatorLayout mCoordinatorSnackbar;
    private RelativeLayout mContentLayout;
    private CircleImageView mImgAvatar;
    private EditText mEditAccount;
    private EditText mEditPassword;
    private Button mBtnLogin;
    private TextView mTextForgetPassword;
    private TextView mTextRegister;
    private AVOSUtils mAVOSUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        if (AVUser.getCurrentUser() != null) {
            Intent intentRegister = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intentRegister);
            finish();
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_login);
        mCoordinatorSnackbar = (CoordinatorLayout) findViewById(R.id.coordinator_snackbar);
        mContentLayout = (RelativeLayout) findViewById(R.id.content_layout);
        mImgAvatar = (CircleImageView) findViewById(R.id.img_circle_user_avatar);
        mEditAccount = (EditText) findViewById(R.id.edit_account);
        mEditPassword = (EditText) findViewById(R.id.edit_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mTextForgetPassword = (TextView) findViewById(R.id.text_forget_password);
        mTextRegister = (TextView) findViewById(R.id.text_register);

        mAVOSUtils = AVOSUtils.getInstance();
        mAVOSUtils.setOnGetAvatarListener(this);
        CommonUtils.materialRipple(mBtnLogin);
    }

    @Override
    public void initData() {
        //显示已保存的用户名和密码
        SharedPreferences sharedPreferences = getSharedPreferences(CommonDefine.LOGIN, MODE_PRIVATE);
        mEditAccount.setText(sharedPreferences.getString(CommonDefine.MOBILE_PHONE, null));
        String account = mEditAccount.getText().toString().trim();
        if (!TextUtils.isEmpty(account)) {
            mAVOSUtils.getUserAvatar(account);
        }
    }

    @Override
    public void initListener() {
        mBtnLogin.setOnClickListener(this);
        mTextForgetPassword.setOnClickListener(this);
        mTextRegister.setOnClickListener(this);
        // 点击屏幕隐藏软键盘
        hideKeyBoard(mContentLayout, mEditAccount);

        mEditAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (TextUtils.isEmpty(s)) {
                    mImgAvatar.setImageResource(R.mipmap.car_net_plus);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                mAVOSUtils.getUserAvatar(mEditAccount.getText().toString().trim());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                login();
                break;

            case R.id.text_forget_password:
                Intent intentForgetPsw = new Intent(LoginActivity.this, ForgetPswActivity.class);
                intentForgetPsw.putExtra(CommonDefine.MOBILE_PHONE, mEditAccount.getText().toString().trim());
                startActivity(intentForgetPsw);
                break;

            case R.id.text_register:
                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    @Override
    public void getAvaterListener(byte[] avatarUrl) {
        Message msg = new Message();
        msg.what = 1;
        msg.obj = avatarUrl;
        mHandlerLoad.sendMessage(msg);
    }

    // 子线程Handler刷新UI界面
    private Handler mHandlerLoad = new Handler() {

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    byte[] avatarBytes = (byte[]) msg.obj;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(avatarBytes, 0, avatarBytes.length);
                    mImgAvatar.setImageBitmap(bitmap);
                    // 保存图片到本地
                    FileUtil.saveAvatarFile(LoginActivity.this, CommonDefine.AVATAR_FILE_NAME, bitmap);
            }
        }
    };

    private void login() {
        String account = mEditAccount.getText().toString().trim();
        String password = mEditPassword.getText().toString().trim();
        // 保存账号和密码
        SharedPreferences sharedPreferences = getSharedPreferences(CommonDefine.LOGIN, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(CommonDefine.MOBILE_PHONE, account);
        editor.putString(CommonDefine.PASSWORD, password);
        editor.commit();
        if (!TextUtils.isEmpty(account)) {
            if (!TextUtils.isEmpty(password)) {
                loginAVOS(account, password);
            } else {
                SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.error_register_password_null));
            }
        } else {
            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.error_mobile_phone));
        }
    }

    private void loginAVOS(String account, String password) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle(getString(R.string.tip));
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.load_progress, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.show();
        final LoadingView loadingView = (LoadingView) view.findViewById(R.id.loading_view);
        final TextView textLoadingTip = (TextView) view.findViewById(R.id.text_loading_tip);
        // 手机号+密码登录
        AVUser.loginByMobilePhoneNumberInBackground(account, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, final AVException e) {
                if (e == null) {
                    textLoadingTip.setText(getString(R.string.login_success));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(mainIntent);
                            LoginActivity.this.finish();
                        }
                    }, 1000);
                } else {
                    Log.e(">>>>>>>>>>>", ">>>>>>>>>>>loginByMobilePhoneNumber" + e.toString());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingView.stopAnimation();
                            dialog.dismiss();
                            switch (e.getCode()) {
                                case 1:
                                    SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.error_login_limited));
                                    break;
                                case 211: // INVALID_PHONE_NUMBER
                                    SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.could_not_find_user));
                                    break;

                                case 124: // TIMEOUT
                                    SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.time_out));
                                    break;

                                case 210:
                                    SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.username_password_mismatch));
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
}
