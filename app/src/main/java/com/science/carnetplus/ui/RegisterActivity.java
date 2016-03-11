package com.science.carnetplus.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.science.carnetplus.R;
import com.science.carnetplus.utils.BottomSheetBehaviorUtils;
import com.science.carnetplus.utils.CommonDefine;
import com.science.carnetplus.utils.CommonUtils;
import com.science.carnetplus.utils.FileUtil;
import com.science.carnetplus.utils.SnackbarUtils;
import com.science.carnetplus.utils.StatusBarCompat;
import com.science.carnetplus.utils.ToastUtils;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author 幸运Science-陈土燊
 * @description 注册界面
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/6
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private String mAvaterUrl;
    private boolean isTakeAvatar = false;

    private CoordinatorLayout mCoordinatorLayout;
    private RelativeLayout mContentLayout;
    private View mDarkenLayout;
    private CircleImageView mImgUserAvatar;
    private EditText mEditMobilePhone;
    private EditText mEditPassword;
    private EditText mEditVerify;
    private Button mBtnLogin;
    private Button mBtnGetVerify;
    private CommonUtils mCommonUtils;
    private BottomSheetBehavior mSheetBehavior;
    private View mBottomSheet;
    private TextView mTextCamera, mTextGallery, mTextCancel;
    private String mStrAvatarUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(RegisterActivity.this, 0);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_register);
        mCoordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        mContentLayout = (RelativeLayout) findViewById(R.id.root_layout);
        mDarkenLayout = findViewById(R.id.darken_layout);
        mImgUserAvatar = (CircleImageView) findViewById(R.id.img_circle_user_avatar);
        mEditMobilePhone = (EditText) findViewById(R.id.edit_mobile_phone);
        mEditPassword = (EditText) findViewById(R.id.edit_password);
        mEditVerify = (EditText) findViewById(R.id.edit_verify_code);
        mBtnGetVerify = (Button) findViewById(R.id.btn_get_verify);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBottomSheet = findViewById(R.id.design_bottom_sheet);
        mSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mTextCamera = (TextView) findViewById(R.id.text_camera);
        mTextGallery = (TextView) findViewById(R.id.text_gallery);
        mTextCancel = (TextView) findViewById(R.id.text_cancel);

        mCommonUtils = CommonUtils.getInstance(RegisterActivity.this);
//        mCommonUtils.materialRipple(mBtnLogin);
        mCommonUtils.materialRipple(mBtnGetVerify);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        mImgUserAvatar.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mBtnGetVerify.setOnClickListener(this);
        mTextCamera.setOnClickListener(this);
        mTextGallery.setOnClickListener(this);
        mTextCancel.setOnClickListener(this);

        // 点击屏幕隐藏软键盘
        mContentLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mContentLayout.setFocusable(true);
                mContentLayout.setFocusableInTouchMode(true);
                mContentLayout.requestFocus();
                mCommonUtils.hideKeyboard(mEditMobilePhone);
                return false;
            }
        });

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
                mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case R.id.btn_get_verify:
                break;

            case R.id.btn_login:
                SnackbarUtils.showSnackbar(v, "ssssss");
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
                    ToastUtils.showMessage(RegisterActivity.this, getString(R.string.must_allow_permission_start_camera));
                } else {
                    ToastUtils.showMessage(RegisterActivity.this, getString(R.string.must_allow_permission_read_gallery));
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
                Bundle extras = data.getExtras();
                if (extras == null) {
                    return;
                }
                Bitmap cropBitmap = extras.getParcelable("data"); // 裁剪得到的bitmap
                Bitmap avatarBitmap = FileUtil.comp(cropBitmap); // 压缩后得到的bitmap
                mImgUserAvatar.setImageBitmap(avatarBitmap);
                // 保存头像图标，并返回头像地址url
                mStrAvatarUrl = FileUtil.saveAvatarFile(RegisterActivity.this, CommonDefine.AVATAR_FILE_NAME, avatarBitmap);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPicToView(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 60, stream);
        byte[] bytes = stream.toByteArray();
        String img = new String(Base64.encodeToString(bytes, Base64.DEFAULT));
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
