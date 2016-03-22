package com.science.carnetplus.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.science.carnetplus.R;
import com.science.carnetplus.utils.AVOSUtils;
import com.science.carnetplus.utils.BottomSheetBehaviorUtils;
import com.science.carnetplus.utils.CommonDefine;
import com.science.carnetplus.utils.CommonUtils;
import com.science.carnetplus.utils.FileUtil;
import com.science.carnetplus.utils.SnackbarUtils;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author 幸运Science-陈土燊
 * @description 更新用户信息
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/20
 */

public class AlterUserInfoActivity extends BaseActivity implements View.OnClickListener {

    private AVOSUtils mAVOSUtils;
    private CoordinatorLayout mRootLayout;
    private RelativeLayout mLayoutContent;
    private CircleImageView mImgUserAvatar;
    private RelativeLayout mLayoutSex, mLayoutBirth, mLayoutHometown;
    private EditText mEditNickname, mEditUserDescribe;
    private TextView mTextSex, mTextBirth, mTextHomeTown;
    private CoordinatorLayout mCoordinatorBottom, mCoordinatorSnackBar;
    private View mDarkenLayout;
    private BottomSheetBehavior mSheetBehavior;
    private View mBottomSheet;
    private TextView mTextCamera, mTextGallery, mTextCancel;
    private String mStrAvatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_alter_user_info);
        setToolbar(getString(R.string.alter_user_info));

        mRootLayout = (CoordinatorLayout) findViewById(R.id.root_layout);
        mLayoutContent = (RelativeLayout) findViewById(R.id.content_layout);
        mCoordinatorSnackBar = (CoordinatorLayout) findViewById(R.id.coordinator_snackBar);
        mImgUserAvatar = (CircleImageView) findViewById(R.id.img_circle_user_avatar);
        mLayoutSex = (RelativeLayout) findViewById(R.id.layout_sex);
        mLayoutBirth = (RelativeLayout) findViewById(R.id.layout_birth);
        mLayoutHometown = (RelativeLayout) findViewById(R.id.layout_hometown);
        mEditNickname = (EditText) findViewById(R.id.edit_nickname);
        mEditUserDescribe = (EditText) findViewById(R.id.edit_user_describe);
        mTextSex = (TextView) findViewById(R.id.text_sex);
        mTextBirth = (TextView) findViewById(R.id.text_birth);
        mTextHomeTown = (TextView) findViewById(R.id.text_hometown);
        mCoordinatorBottom = (CoordinatorLayout) findViewById(R.id.coordinator_bottom);
        mDarkenLayout = findViewById(R.id.darken_layout);
        mBottomSheet = findViewById(R.id.design_bottom_sheet);
        mSheetBehavior = BottomSheetBehavior.from(mBottomSheet);
        mTextCamera = (TextView) findViewById(R.id.text_camera);
        mTextGallery = (TextView) findViewById(R.id.text_gallery);
        mTextCancel = (TextView) findViewById(R.id.text_cancel);
        CommonUtils.materialRipple(mLayoutSex, "#585858");
        CommonUtils.materialRipple(mLayoutBirth, "#585858");
        CommonUtils.materialRipple(mLayoutHometown, "#585858");
        mAVOSUtils = AVOSUtils.getInstance();
    }

    @Override
    public void initData() {
        mImgUserAvatar.setImageBitmap(FileUtil.getAvatar(FileUtil.getAvatarFilePath(AlterUserInfoActivity.this)));
        mEditNickname.setText(getIntent().getStringExtra(CommonDefine.NICKNAME));
        mEditUserDescribe.setText(getIntent().getStringExtra(CommonDefine.DESCRIBE));
        mTextSex.setText(getIntent().getStringExtra(CommonDefine.SEX));
        mTextBirth.setText(getIntent().getStringExtra(CommonDefine.BIRTH));
        mTextHomeTown.setText(getIntent().getStringExtra(CommonDefine.HOMETOWN));
    }

    @Override
    public void initListener() {
        mImgUserAvatar.setOnClickListener(this);
        mLayoutSex.setOnClickListener(this);
        mLayoutBirth.setOnClickListener(this);
        mLayoutHometown.setOnClickListener(this);
        mTextCamera.setOnClickListener(this);
        mTextGallery.setOnClickListener(this);
        mTextCancel.setOnClickListener(this);
        // 点击屏幕隐藏软键盘
        hideKeyBoard(mLayoutContent, mEditNickname);

        // BottomSheet展开时，点击暗色屏幕收起BottomSheet
        mDarkenLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        // 取消滑动BottomSheet以外的地方而拖出BottomSheet
        mRootLayout.setOnTouchListener(new View.OnTouchListener() {
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.done, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_circle_user_avatar:
                CommonUtils.hideKeyboard(mEditNickname, AlterUserInfoActivity.this);
                mSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;

            case R.id.text_sex:

                break;

            case R.id.text_birth:

                break;

            case R.id.text_hometown:

                break;

            case R.id.text_camera:
                initPermission(0, getString(R.string.request_camera_write_permission),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                break;

            case R.id.text_gallery:
                initPermission(1, getString(R.string.request_write_permission),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;

            case R.id.text_cancel:
                mSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                break;
        }
    }

    private void initPermission(final int type, String permissionTip, String... permission) {
        performCodeWithPermission(permissionTip, new PermissionCallback() {
            @Override
            public void hasPermission() {
                if (type == 0) {
                    getAvatarFormCamera();
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
    private void getAvatarFormCamera() {
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

            FileUtil.startPhotoZoom(FileUtil.getCameraStorageUri(), AlterUserInfoActivity.this);
        }
        // 图库
        else if (requestCode == CommonDefine.GALLERY_REQUEST_CODE) {
            if (data == null) {
                return;
            } else {
                FileUtil.startPhotoZoom(data.getData(), AlterUserInfoActivity.this);
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

    private Bitmap cropBitmap;

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
        cropBitmap = extras.getParcelable("data"); // 裁剪得到的bitmap
        cropBitmap = FileUtil.compressImageSec(FileUtil.compressImage(cropBitmap)); // 压缩后得到的bitmap
        mImgUserAvatar.setImageBitmap(cropBitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.done:
                updateUserInfo();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateUserInfo() {
        String nickname = mEditNickname.getText().toString();
        String userDescribe = mEditUserDescribe.getText().toString();
        String sex = mTextSex.getText().toString();
        String birth = mTextBirth.getText().toString();
        String hometown = mTextHomeTown.getText().toString();

        // 保存头像图标，并返回头像地址url
        mStrAvatarUrl = FileUtil.saveAvatarFile(AlterUserInfoActivity.this, CommonDefine.AVATAR_FILE_NAME, cropBitmap);
        mAVOSUtils.resetAvatar(AVUser.getCurrentUser().getUsername(), mStrAvatarUrl);
        mAVOSUtils.updateUserInfo(AVUser.getCurrentUser().getUsername(), nickname, userDescribe, sex, birth, hometown);
        Intent intent = new Intent();
        intent.putExtra(CommonDefine.DESCRIBE, userDescribe);
        intent.putExtra(CommonDefine.NICKNAME, nickname);
        intent.putExtra(CommonDefine.SEX, sex);
        intent.putExtra(CommonDefine.BIRTH, birth);
        intent.putExtra(CommonDefine.HOMETOWN, hometown);
        setResult(RESULT_OK, intent);
        finish();
    }
}
