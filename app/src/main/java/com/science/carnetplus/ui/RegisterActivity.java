package com.science.carnetplus.ui;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SaveCallback;
import com.science.carnetplus.AppManager;
import com.science.carnetplus.MainActivity;
import com.science.carnetplus.R;
import com.science.carnetplus.utils.AVOSUtils;
import com.science.carnetplus.utils.BottomSheetBehaviorUtils;
import com.science.carnetplus.utils.CommonDefine;
import com.science.carnetplus.utils.CommonUtils;
import com.science.carnetplus.utils.FileUtil;
import com.science.carnetplus.utils.SnackbarUtils;
import com.science.carnetplus.widget.materialProgress.LoadingView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author 幸运Science-陈土燊
 * @description 注册界面
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/6
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private CoordinatorLayout mCoordinatorLayout, mCoordinatorSnackbar, mCoordinatorBottom;
    private RelativeLayout mContentLayout;
    private AVOSUtils mAVOSUtils;
    private View mDarkenLayout;
    private CircleImageView mImgUserAvatar;
    private EditText mEditMobilePhone;
    private EditText mEditPassword;
    private EditText mEditVerify;
    private Button mBtnRegister;
    private Button mBtnGetVerify;
    private BottomSheetBehavior mSheetBehavior;
    private View mBottomSheet;
    private TextView mTextCamera, mTextGallery, mTextCancel;
    private boolean isTakeAvatar = false;
    private String mStrAvatarUrl;
    private String mobilePhone, password, verify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_register);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mCoordinatorSnackbar = (CoordinatorLayout) findViewById(R.id.coordinator_snackbar);
        mCoordinatorBottom = (CoordinatorLayout) findViewById(R.id.coordinator_bottom);
        mContentLayout = (RelativeLayout) findViewById(R.id.content_layout);
        mDarkenLayout = findViewById(R.id.darken_layout);
        mImgUserAvatar = (CircleImageView) findViewById(R.id.img_circle_user_avatar);
        mEditMobilePhone = (EditText) findViewById(R.id.edit_mobile_phone);
        mEditPassword = (EditText) findViewById(R.id.edit_password);
        mEditVerify = (EditText) findViewById(R.id.edit_verify_code);
        mBtnGetVerify = (Button) findViewById(R.id.btn_get_verify);
        mBtnRegister = (Button) findViewById(R.id.btn_register);
        mBottomSheet = findViewById(R.id.design_bottom_sheet);
        mSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mTextCamera = (TextView) findViewById(R.id.text_camera);
        mTextGallery = (TextView) findViewById(R.id.text_gallery);
        mTextCancel = (TextView) findViewById(R.id.text_cancel);

        mAVOSUtils = AVOSUtils.getInstance();
        CommonUtils.materialRipple(mBtnRegister);
    }

    @Override
    public void initData() {
    }

    @Override
    public void initListener() {
        mImgUserAvatar.setOnClickListener(this);
        mBtnGetVerify.setOnClickListener(this);
        mBtnRegister.setOnClickListener(this);
        mTextCamera.setOnClickListener(this);
        mTextGallery.setOnClickListener(this);
        mTextCancel.setOnClickListener(this);
        // 点击屏幕隐藏软键盘
        hideKeyBoard(mContentLayout, mEditMobilePhone);

        // BottomSheet展开时，点击暗色屏幕收起BottomSheet
        mDarkenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        // 取消滑动BottomSheet以外的地方而拖出BottomSheet
        mCoordinatorLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (mSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                return true;
            }
        });

        mSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            boolean hasRequest;

            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED || newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mDarkenLayout.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

                // BottomSheet展开和收起屏幕变暗变亮
                mDarkenLayout.setVisibility(View.VISIBLE);
                ViewCompat.setAlpha(mDarkenLayout, slideOffset);

                if (!hasRequest && mSheetBehavior.getPeekHeight() == 0 && slideOffset > 0) {
                    hasRequest = true;
                    BottomSheetBehaviorUtils.updateOffsets(bottomSheet);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_circle_user_avatar:
                CommonUtils.hideKeyboard(mEditMobilePhone, RegisterActivity.this);
                mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case R.id.btn_get_verify:
                verifyEdit(0);
                break;

            case R.id.btn_register:
                verifyEdit(1);
                break;

            case R.id.text_camera:
                initPemission(0, getString(R.string.request_camera_write_permission),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                break;

            case R.id.text_gallery:
                initPemission(1, getString(R.string.request_write_permission),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;

            case R.id.text_cancel:
                mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
    }

    /**
     * 验证EditText书写正确性
     */
    private void verifyEdit(int type) {
        mobilePhone = mEditMobilePhone.getText().toString().trim();
        password = mEditPassword.getText().toString().trim();
        verify = mEditVerify.getText().toString().trim();
        if (CommonUtils.isMobilePhone(mobilePhone)) {
            if (!TextUtils.isEmpty(password)) {
                if (CommonUtils.passwordVerify(password)) {
                    // 获取验证码
                    if (type == 0) {
                        getVerify();
                    }
                    // 注册
                    else {
                        if (!TextUtils.isEmpty(verify)) {
                            if (CommonUtils.isSMSCodeValid(verify)) {
                                if (isTakeAvatar) {
                                    // 点击"注册"按钮
                                    register();
                                } else {
                                    SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.error_register_take_gender_null));
                                }
                            } else {
                                SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.verify_code_limited_6_number));
                            }
                        } else {
                            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.error_register_verify_address_null));
                        }
                    }
                } else {
                    SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.error_register_password));
                }
            } else {
                SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.error_register_password_null));
            }
        } else {
            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.error_mobile_phone));
        }
    }

    /**
     * 点击验证码按钮，获取验证码
     */
    private void getVerify() {
        AVOSCloud.requestSMSCodeInBackground(mobilePhone, new RequestMobileCodeCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    SnackbarUtils.showSnackbar(mCoordinatorLayout, getString(R.string.please_receive_verify_code));
                } else {
                    Log.e("RegisterActivity>>>>>>>", ">>>>>>>>>>>getVerify()" + e.toString());
                    switch (e.getCode()) {
                        case 124: // TIMEOUT
                            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.time_out));
                            break;
                        case 601:
                            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.double_get_verify_code));
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
     * 注册
     */
    private void register() {
        AVUser.signUpOrLoginByMobilePhoneInBackground(mobilePhone, verify, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    avUser.setPassword(password);
                    avUser.saveInBackground();
                    // 保存账号和密码
                    SharedPreferences sharedPreferences = getSharedPreferences(CommonDefine.LOGIN, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(CommonDefine.MOBILE_PHONE, mobilePhone);
                    editor.putString(CommonDefine.PASSWORD, password);
                    editor.commit();
                    saveInstallation(avUser);
                    upLoadUserAvatar();
                } else {
                    Log.e("RegisterActivity>>>>>>>", ">>>>>>>>>>>register()" + e.toString());
                    switch (e.getCode()) {
                        case 603: //无效的短信验证码
                            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.please_get_verify_code_again));
                            break;
                        case 124: // TIMEOUT
                            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.time_out));
                            break;
                        default:
                            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.network_not_connected));
                            break;
                    }
                }
            }
        });
    }

    private void saveInstallation(final AVUser avuser) {
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            public void done(AVException e) {
                if (e == null) {
                    // 关联 installationId 到用户表等操作……
                    avuser.put("installationId", AVInstallation.getCurrentInstallation().getInstallationId());
                } else {
                    // 保存失败，输出错误信息
                    Log.e("RegisterActivity>>>>>>>", ">>>>>>>>>>>saveInstallation()" + e.toString());
                }
            }
        });
    }

    /**
     * 上传头像
     */
    private void upLoadUserAvatar() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
        builder.setTitle(getString(R.string.tip));
        View view = LayoutInflater.from(RegisterActivity.this).inflate(R.layout.load_progress, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.show();
        final LoadingView loadingView = (LoadingView) view.findViewById(R.id.loading_view);
        final TextView textLoadingTip = (TextView) view.findViewById(R.id.text_loading_tip);

        final String username = AVUser.getCurrentUser().getUsername();
        mAVOSUtils.upLoadUserAvatar(username, mStrAvatarUrl, new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    textLoadingTip.setText(getString(R.string.register_success));
                    mAVOSUtils.updateUserInfo(username, username, "性别", "出生日期", "所在地区", getString(R.string.user_describe_details));
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            AppManager.getAppManager().finishActivity(LoginActivity.class);
                            finish();
                        }
                    }, 1000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingView.stopAnimation();
                            dialog.dismiss();
                            SnackbarUtils.showSnackbar(mCoordinatorSnackbar, getString(R.string.network_not_connected));
                        }
                    }, 1000);
                }
            }
        });
    }

    private void initPemission(final int type, String permissionTip, String... permission) {
        performCodeWithPermission(permissionTip, new PermissionCallback() {
            @Override
            public void hasPermission() {
                if (type == 0) {
                    getAvaterFormCamera();
                } else {
                    getAvatarFormGallery();
                }
            }

            @Override
            public void noPermission() {
                if (type == 0) {
                    SnackbarUtils.showSnackbar(mCoordinatorBottom, getString(R.string.must_allow_permission_start_camera));
                } else {
                    SnackbarUtils.showSnackbar(mCoordinatorBottom, getString(R.string.must_allow_permission_read_gallery));
                }
            }
        }, permission);
    }

    // 拍照获取头像
    private void getAvaterFormCamera() {
        Intent intentTakePhotos = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定调用相机拍照后照片的储存路径
        intentTakePhotos.putExtra(MediaStore.EXTRA_OUTPUT, FileUtil.getCameraStorageUri());
        startActivityForResult(intentTakePhotos, CommonDefine.CAMERA_REQUEST_CODE);
        mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    // 图库获取头像
    private void getAvatarFormGallery() {
        Intent intentGallery = new Intent(Intent.ACTION_GET_CONTENT);
        intentGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, CommonDefine.IMAGE_UNSPECIFIED);
        startActivityForResult(intentGallery, CommonDefine.GALLERY_REQUEST_CODE);
        mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // 拍照
        if (requestCode == CommonDefine.CAMERA_REQUEST_CODE) {

            FileUtil.startPhotoZoom(FileUtil.getCameraStorageUri(), RegisterActivity.this);
        }
        // 图库
        else if (requestCode == CommonDefine.GALLERY_REQUEST_CODE) {
            if (data == null) {
                return;
            } else {
                FileUtil.startPhotoZoom(data.getData(), RegisterActivity.this);
            }
        }
        // 裁剪
        else if (requestCode == CommonDefine.CROP_REQUEST_CODE) {
            if (data == null) {
                return;
            } else {
                saveAvatar(data);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 保存头像
     *
     * @param data
     */
    private void saveAvatar(Intent data) {

        Bundle extras = data.getExtras();
        if (extras == null) {
            return;
        }
        Bitmap cropBitmap = extras.getParcelable("data"); // 裁剪得到的bitmap
        cropBitmap = FileUtil.compressImageSec(FileUtil.compressImage(cropBitmap)); // 压缩后得到的bitmap
        mImgUserAvatar.setImageBitmap(cropBitmap);
        // 保存头像图标，并返回头像地址url
        mStrAvatarUrl = FileUtil.saveAvatarFile(RegisterActivity.this, CommonDefine.AVATAR_FILE_NAME, cropBitmap);
        isTakeAvatar = true;

        String splitStr[] = mStrAvatarUrl.split("0");
        SnackbarUtils.showSnackbar(mCoordinatorSnackbar,
                "头像保存在:" + splitStr[1].replaceAll(CommonDefine.AVATAR_FILE_NAME, ""));

//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bm.compress(Bitmap.CompressFormat.PNG, 60, stream);
//        byte[] bytes = stream.toByteArray();
//        String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
    }

    @Override
    public void onBackPressed() {
        if (mSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}
