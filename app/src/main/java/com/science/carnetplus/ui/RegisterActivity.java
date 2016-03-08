package com.science.carnetplus.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.science.carnetplus.R;
import com.science.carnetplus.utils.BottomSheetBehaviorUtils;
import com.science.carnetplus.utils.CommonUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author 幸运Science-陈土燊
 * @description 注册界面
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/6
 */

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_register);

        initView();
        initListener();
    }

    private void initView() {
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
        mTextGallery = (TextView) findViewById(R.id.text_camera);
        mTextCancel = (TextView) findViewById(R.id.text_cancel);

        mCommonUtils = CommonUtils.getInstance(RegisterActivity.this);
        mCommonUtils.materialRipple(mBtnLogin);
        mCommonUtils.materialRipple(mBtnGetVerify);
    }

    private void initListener() {
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
                break;

            case R.id.text_camera:
                break;

            case R.id.text_gallery:
                break;

            case R.id.text_cancel:
                mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
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
