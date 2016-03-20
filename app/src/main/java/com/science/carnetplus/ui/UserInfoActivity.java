package com.science.carnetplus.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.science.carnetplus.R;
import com.science.carnetplus.utils.AVOSUtils;
import com.science.carnetplus.utils.CommonUtils;
import com.science.carnetplus.utils.FileUtil;
import com.science.carnetplus.utils.StatusBarCompat;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * @author 幸运Science-陈土燊
 * @description 用户信息
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/4
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {

    private FrameLayout mLayoutMy;
    private AppBarLayout mAppBarLayout;
    private CircleImageView mImgUserAvatar;
    private TextView mTextUserDescribe;
    private FloatingActionButton mFab;
    private LinearLayout mListLayout;
    private RelativeLayout mLayoutAccount;
    private RelativeLayout mLayoutNickname;
    private RelativeLayout mLayoutSex;
    private RelativeLayout mLayoutBirth;
    private RelativeLayout mLayoutHometown;
    private RelativeLayout mLayoutEmail;
    private RelativeLayout mLayoutCars;
    private RelativeLayout mLayoutQuitAccount;
    private TextView mTextAccount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(UserInfoActivity.this, Color.TRANSPARENT, false);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = setToolbar(AVUser.getCurrentUser().getUsername().toString());
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            CollapsingToolbarLayout.LayoutParams lp = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
            lp.height = (int) (CommonUtils.getStatusBarHeight(this) +
                    getResources().getDimension(R.dimen.abc_action_bar_default_height_material));
            toolbar.setLayoutParams(lp);
        }

        mAppBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        mLayoutMy = (FrameLayout) findViewById(R.id.my_fl);
        mImgUserAvatar = (CircleImageView) findViewById(R.id.img_circle_user_avatar);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mTextUserDescribe = (TextView) findViewById(R.id.text_user_describe);
        mTextAccount = (TextView) findViewById(R.id.text_account);
        mListLayout = (LinearLayout) findViewById(R.id.list_layout);
        mLayoutAccount = (RelativeLayout) findViewById(R.id.layout_account);
        mLayoutNickname = (RelativeLayout) findViewById(R.id.layout_nickname);
        mLayoutSex = (RelativeLayout) findViewById(R.id.layout_sex);
        mLayoutBirth = (RelativeLayout) findViewById(R.id.layout_birth);
        mLayoutHometown = (RelativeLayout) findViewById(R.id.layout_hometown);
        mLayoutEmail = (RelativeLayout) findViewById(R.id.layout_email);
        mLayoutCars = (RelativeLayout) findViewById(R.id.layout_cars);
        mLayoutQuitAccount = (RelativeLayout) findViewById(R.id.layout_quit_account);
        CommonUtils.materialRipple(mLayoutEmail, "#585858");
        CommonUtils.materialRipple(mLayoutCars, "#585858");
        CommonUtils.materialRipple(mLayoutQuitAccount, "#585858");
    }

    @Override
    public void initData() {
        mImgUserAvatar.setImageBitmap(FileUtil.getAvatar(FileUtil.getAvatarFilePath(UserInfoActivity.this)));
        mTextAccount.setText(AVUser.getCurrentUser().getUsername().toString());
    }

    @Override
    public void initListener() {
        mLayoutEmail.setOnClickListener(this);
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
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_email:
                break;
            case R.id.layout_cars:
                break;
            case R.id.layout_quit_account:
                AVOSUtils.logout();
                Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
