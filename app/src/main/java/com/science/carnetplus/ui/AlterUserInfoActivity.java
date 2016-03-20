package com.science.carnetplus.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.science.carnetplus.R;
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

    private CircleImageView mImgUserAvartar;
    private RelativeLayout mLayoutSex, mLayoutBirth, mLayoutHometown;
    private EditText mEditNickname, mEditUserDescriable;
    private TextView mTextSex, mTextBirth, mTextHomeTown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_alter_user_info);
        setToolbar(getString(R.string.alter_user_info));

        mImgUserAvartar = (CircleImageView) findViewById(R.id.img_circle_user_avatar);
        mLayoutSex = (RelativeLayout) findViewById(R.id.layout_sex);
        mLayoutBirth = (RelativeLayout) findViewById(R.id.layout_birth);
        mLayoutHometown = (RelativeLayout) findViewById(R.id.layout_hometown);
        mEditNickname = (EditText) findViewById(R.id.edit_nickname);
        mEditUserDescriable = (EditText) findViewById(R.id.edit_user_describe);
        mTextSex = (TextView) findViewById(R.id.text_sex);
        mTextBirth = (TextView) findViewById(R.id.text_birth);
        mTextHomeTown = (TextView) findViewById(R.id.text_hometown);
        CommonUtils.materialRipple(mLayoutSex, "#585858");
        CommonUtils.materialRipple(mLayoutBirth, "#585858");
        CommonUtils.materialRipple(mLayoutHometown, "#585858");
    }

    @Override
    public void initData() {
        mImgUserAvartar.setImageBitmap(FileUtil.getAvatar(FileUtil.getAvatarFilePath(AlterUserInfoActivity.this)));
    }

    @Override
    public void initListener() {
        mImgUserAvartar.setOnClickListener(this);
        mLayoutSex.setOnClickListener(this);
        mLayoutBirth.setOnClickListener(this);
        mLayoutHometown.setOnClickListener(this);
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

                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
