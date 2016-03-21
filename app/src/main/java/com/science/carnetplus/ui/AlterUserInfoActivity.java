package com.science.carnetplus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.science.carnetplus.R;
import com.science.carnetplus.utils.AVOSUtils;
import com.science.carnetplus.utils.CommonDefine;
import com.science.carnetplus.utils.CommonUtils;
import com.science.carnetplus.utils.FileUtil;

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
    private CircleImageView mImgUserAvartar;
    private RelativeLayout mLayoutSex, mLayoutBirth, mLayoutHometown;
    private EditText mEditNickname, mEditUserDescrible;
    private TextView mTextSex, mTextBirth, mTextHomeTown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_alter_user_info);
        setToolbar(getString(R.string.alter_user_info));

        mRootLayout = (CoordinatorLayout) findViewById(R.id.root_layout);
        mImgUserAvartar = (CircleImageView) findViewById(R.id.img_circle_user_avatar);
        mLayoutSex = (RelativeLayout) findViewById(R.id.layout_sex);
        mLayoutBirth = (RelativeLayout) findViewById(R.id.layout_birth);
        mLayoutHometown = (RelativeLayout) findViewById(R.id.layout_hometown);
        mEditNickname = (EditText) findViewById(R.id.edit_nickname);
        mEditUserDescrible = (EditText) findViewById(R.id.edit_user_describe);
        mTextSex = (TextView) findViewById(R.id.text_sex);
        mTextBirth = (TextView) findViewById(R.id.text_birth);
        mTextHomeTown = (TextView) findViewById(R.id.text_hometown);
        CommonUtils.materialRipple(mLayoutSex, "#585858");
        CommonUtils.materialRipple(mLayoutBirth, "#585858");
        CommonUtils.materialRipple(mLayoutHometown, "#585858");
        mAVOSUtils = AVOSUtils.getInstance();
    }

    @Override
    public void initData() {
        mImgUserAvartar.setImageBitmap(FileUtil.getAvatar(FileUtil.getAvatarFilePath(AlterUserInfoActivity.this)));
        mEditNickname.setText(getIntent().getStringExtra(CommonDefine.NICKNAME));
        mEditUserDescrible.setText(getIntent().getStringExtra(CommonDefine.DESCRIBE));
        mTextSex.setText(getIntent().getStringExtra(CommonDefine.SEX));
        mTextBirth.setText(getIntent().getStringExtra(CommonDefine.BIRTH));
        mTextHomeTown.setText(getIntent().getStringExtra(CommonDefine.HOMETOWN));
    }

    @Override
    public void initListener() {
        mImgUserAvartar.setOnClickListener(this);
        mLayoutSex.setOnClickListener(this);
        mLayoutBirth.setOnClickListener(this);
        mLayoutHometown.setOnClickListener(this);
        // 点击屏幕隐藏软键盘
        hideKeyBoard(mRootLayout, mEditNickname);
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

                break;

            case R.id.text_sex:

                break;

            case R.id.text_birth:

                break;

            case R.id.text_hometown:

                break;
        }
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
        String userDescribe = mEditUserDescrible.getText().toString();
        String sex = mTextSex.getText().toString();
        String birth = mTextBirth.getText().toString();
        String hometown = mTextHomeTown.getText().toString();

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
