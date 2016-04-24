package com.science.carnetplus.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.science.carnetplus.R;
import com.science.carnetplus.util.AVOSUtils;
import com.science.carnetplus.util.CommonDefine;
import com.science.carnetplus.util.CommonUtils;
import com.science.carnetplus.util.ImageUtils;
import com.science.carnetplus.util.StatusBarCompat;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author 幸运Science-陈土燊
 * @description 用户信息
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/4
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private AVOSUtils mAVOSUtils;
    private FrameLayout mLayoutMy;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private CircleImageView mImgUserAvatar;
    private TextView mTextAccount;
    private TextView mTextUserDescribe;
    private TextView mTextNickname;
    private TextView mTextSex;
    private TextView mTextBirth;
    private TextView mTextHometown;
    private FloatingActionButton mFab;
    private LinearLayout mListLayout;
    private RelativeLayout mLayoutCars;
    private RelativeLayout mLayoutQuitAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(UserInfoActivity.this, Color.TRANSPARENT, false);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_user_info);
        mToolbar = setToolbar(getIntent().getStringExtra(CommonDefine.NICKNAME));
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) mToolbar.getLayoutParams();
            lp.height = (int) (CommonUtils.getStatusBarHeight(this) +
                    getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
            mToolbar.setLayoutParams(lp);
        }

        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mLayoutMy = (FrameLayout) findViewById(R.id.my_fl);
        mImgUserAvatar = (CircleImageView) findViewById(R.id.img_circle_user_avatar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mTextUserDescribe = (TextView) findViewById(R.id.text_user_describe);
        mTextAccount = (TextView) findViewById(R.id.text_account);
        mTextNickname = (TextView) findViewById(R.id.text_nickname);
        mTextSex = (TextView) findViewById(R.id.text_sex);
        mTextBirth = (TextView) findViewById(R.id.text_birth);
        mTextHometown = (TextView) findViewById(R.id.text_hometown);
        mListLayout = (LinearLayout) findViewById(R.id.list_layout);
        mLayoutCars = (RelativeLayout) findViewById(R.id.layout_cars);
        mLayoutQuitAccount = (RelativeLayout) findViewById(R.id.layout_quit_account);
        CommonUtils.materialRipple(mLayoutCars, "#585858");
        CommonUtils.materialRipple(mLayoutQuitAccount, "#585858");
        mAVOSUtils = AVOSUtils.getInstance();
    }

    @Override
    public void initData() {
        mTextAccount.setText(AVUser.getCurrentUser().getUsername().toString());
        mTextUserDescribe.setText(getIntent().getStringExtra(CommonDefine.DESCRIBE));
        mTextNickname.setText(getIntent().getStringExtra(CommonDefine.NICKNAME));
        mTextSex.setText(getIntent().getStringExtra(CommonDefine.SEX));
        mTextBirth.setText(getIntent().getStringExtra(CommonDefine.BIRTH));
        mTextHometown.setText(getIntent().getStringExtra(CommonDefine.HOMETOWN));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImgUserAvatar.setImageBitmap(ImageUtils.getAvatar(ImageUtils.getAvatarFilePath(UserInfoActivity.this)));
    }

    @Override
    public void initListener() {
        mLayoutCars.setOnClickListener(this);
        mLayoutQuitAccount.setOnClickListener(this);
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
                ViewCompat.setAlpha(mLayoutMy, 1 - percentage);
                ViewCompat.setTranslationY(mListLayout, -(percentage * (mImgUserAvatar.getHeight() / 2)));
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, AlterUserInfoActivity.class);
                intent.putExtra(CommonDefine.DESCRIBE, mTextUserDescribe.getText().toString());
                intent.putExtra(CommonDefine.NICKNAME, mTextNickname.getText().toString());
                intent.putExtra(CommonDefine.SEX, mTextSex.getText().toString());
                intent.putExtra(CommonDefine.BIRTH, mTextBirth.getText().toString());
                intent.putExtra(CommonDefine.HOMETOWN, mTextHometown.getText().toString());
                startActivityForResult(intent, CommonDefine.INTENT_REQUSET);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_cars:
                Intent intent = new Intent(UserInfoActivity.this, CarsListActivity.class);
                startActivity(intent);
                break;
            case R.id.layout_quit_account:
                logout();
                break;
        }
    }

    /**
     * 退出登录
     */
    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UserInfoActivity.this);
        builder.setTitle(getString(R.string.tip));
        builder.setMessage(getString(R.string.logout_ensure));
        builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAVOSUtils.logout();
                Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == CommonDefine.INTENT_REQUSET && resultCode == RESULT_OK) {
            mTextUserDescribe.setText(data.getStringExtra(CommonDefine.DESCRIBE));
            mTextNickname.setText(data.getStringExtra(CommonDefine.NICKNAME));
            mTextSex.setText(data.getStringExtra(CommonDefine.SEX));
            mTextBirth.setText(data.getStringExtra(CommonDefine.BIRTH));
            mTextHometown.setText(data.getStringExtra(CommonDefine.HOMETOWN));
            mCollapsingToolbarLayout.setTitle(data.getStringExtra(CommonDefine.NICKNAME));
        }
    }

    @Override
    public void onBackPressed() {
        responseMainActivity();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                responseMainActivity();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void responseMainActivity() {
        Intent intent = new Intent();
        intent.putExtra(CommonDefine.DESCRIBE, mTextUserDescribe.getText().toString());
        intent.putExtra(CommonDefine.NICKNAME, mTextNickname.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
