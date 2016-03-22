package com.science.carnetplus;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.science.carnetplus.fragments.CarIllegallyFragment;
import com.science.carnetplus.fragments.CarMaintainFragment;
import com.science.carnetplus.fragments.MainFragment;
import com.science.carnetplus.fragments.MusicFragment;
import com.science.carnetplus.fragments.OrdersFragment;
import com.science.carnetplus.ui.BaseActivity;
import com.science.carnetplus.ui.UserInfoActivity;
import com.science.carnetplus.utils.AVOSUtils;
import com.science.carnetplus.utils.CommonDefine;
import com.science.carnetplus.utils.FileUtil;
import com.science.carnetplus.utils.StatusBarCompat;
import com.science.carnetplus.utils.ToastUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    private View mHeaderView;
    private CircleImageView mImgAvatar;
    private ImageView mImgMusicControl;
    private TextView mTextNickname;
    private TextView mTextUserDescribe;
    private FragmentManager mFragmentManager;
    private MainFragment mFragmentMain;
    private MusicFragment mFragmentMusic;
    private CarMaintainFragment mFragmentCarMaintain;
    private OrdersFragment mFragmentOrders;
    private CarIllegallyFragment mFragmentCarIllegally;
    private String sex, birth, hometown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarCompat.compat(MainActivity.this, Color.TRANSPARENT, false);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ViewGroup contentLayout = (ViewGroup) findViewById(android.R.id.content);
            contentLayout.getChildAt(0).setFitsSystemWindows(false);
        }

        mToolbar = setToolbar(getString(R.string.app_name));
        mFragmentManager = getFragmentManager();

        initDrawerLayout();
        initNavigationView();
        showFragment(CommonDefine.FRAGMENT_MAIN);
    }

    private void initDrawerLayout() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // android support library 23.1.0+
        mHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        mImgAvatar = (CircleImageView) mHeaderView.findViewById(R.id.image_avatar);
        mTextNickname = (TextView) mHeaderView.findViewById(R.id.text_nickname);
        mImgMusicControl = (ImageView) mHeaderView.findViewById(R.id.img_music_control);
        mTextUserDescribe = (TextView) mHeaderView.findViewById(R.id.text_user_describe);
        mTextUserDescribe.setSelected(true);
        mTextUserDescribe.requestFocus();
    }

    @Override
    public void initData() {
//        GlideUtils.getInstance(MainActivity.this).setImage(FileUtil.getAvatarFilePath(MainActivity.this), mImgAvatar);
        AVOSUtils.getInstance().getUserInfo(AVUser.getCurrentUser().getUsername().toString(), new AVOSUtils.OnAVOSCallback() {
            @Override
            public void getAvaterListener(byte[] avatarBytes) {

            }

            @Override
            public void getUserInfoListener(List<AVObject> userInfoList) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = userInfoList;
                mHandler.sendMessage(msg);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mImgAvatar.setImageBitmap(FileUtil.getAvatar(FileUtil.getAvatarFilePath(MainActivity.this)));
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    updateUserInfo((List<AVObject>) msg.obj);
                    break;
            }
        }
    };

    private void updateUserInfo(List<AVObject> list) {
        if (list != null && list.size() != 0) {
            mTextUserDescribe.setText(list.get(list.size() - 1).getString(CommonDefine.DESCRIBE));
            mTextNickname.setText(list.get(list.size() - 1).getString(CommonDefine.NICKNAME));
            sex = list.get(list.size() - 1).getString(CommonDefine.SEX);
            birth = list.get(list.size() - 1).getString(CommonDefine.BIRTH);
            hometown = list.get(list.size() - 1).getString(CommonDefine.HOMETOWN);
        }
    }

    @Override
    public void initListener() {
        mImgAvatar.setOnClickListener(this);
        mTextNickname.setOnClickListener(this);
        mImgMusicControl.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_avatar:
                intentUserInfo();
                break;
            case R.id.text_nickname:
                intentUserInfo();
                break;
        }
    }

    private void intentUserInfo() {
        Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
        intent.putExtra(CommonDefine.DESCRIBE, mTextUserDescribe.getText().toString());
        intent.putExtra(CommonDefine.NICKNAME, mTextNickname.getText().toString());
        intent.putExtra(CommonDefine.SEX, sex);
        intent.putExtra(CommonDefine.BIRTH, birth);
        intent.putExtra(CommonDefine.HOMETOWN, hometown);
        startActivityForResult(intent, CommonDefine.INTENT_REQUSET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //requestCode标示请求的标示   resultCode表示有数据
        if (requestCode == CommonDefine.INTENT_REQUSET && resultCode == RESULT_OK) {
            mTextUserDescribe.setText(data.getStringExtra(CommonDefine.DESCRIBE));
            mTextNickname.setText(data.getStringExtra(CommonDefine.NICKNAME));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START) || mFragmentMain.mFABToolbarLayout.isToolbar()) {
            drawer.closeDrawer(GravityCompat.START);
            mFragmentMain.mFABToolbarLayout.hide();
        } else {
            doExitApp();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_list) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_nav_main) {
            showFragment(CommonDefine.FRAGMENT_MAIN);
        } else if (id == R.id.nav_music) {
            showFragment(CommonDefine.FRAGMENT_MUSIC);
        } else if (id == R.id.nav_orders) {
            showFragment(CommonDefine.FRAGMENT_ORDERS);
        } else if (id == R.id.nav_car_maintain) {
            showFragment(CommonDefine.FRAGMENT_CAR_MAINTAIN);
        } else if (id == R.id.nav_car_illegally) {
            showFragment(CommonDefine.FRAGMENT_CAR_ILLEGALLY);
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_exit_app) {
            finish();
        }
        mToolbar.setTitle(item.getTitle());
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void showFragment(int index) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        hideFragment(ft);
        switch (index) {
            case CommonDefine.FRAGMENT_MAIN:
                if (mFragmentMain == null) {
                    mFragmentMain = new MainFragment();
                    ft.add(R.id.content, mFragmentMain);
                } else {
                    ft.show(mFragmentMain);
                }
                break;
            case CommonDefine.FRAGMENT_MUSIC:
                if (mFragmentMusic == null) {
                    mFragmentMusic = new MusicFragment();
                    ft.add(R.id.content, mFragmentMusic);
                } else {
                    ft.show(mFragmentMusic);
                }
                break;
            case CommonDefine.FRAGMENT_CAR_MAINTAIN:
                if (mFragmentCarMaintain == null) {
                    mFragmentCarMaintain = new CarMaintainFragment();
                    ft.add(R.id.content, mFragmentCarMaintain);
                } else {
                    ft.show(mFragmentCarMaintain);
                }
                break;
            case CommonDefine.FRAGMENT_ORDERS:
                if (mFragmentOrders == null) {
                    mFragmentOrders = new OrdersFragment();
                    ft.add(R.id.content, mFragmentOrders);
                } else {
                    ft.show(mFragmentOrders);
                }
                break;
            case CommonDefine.FRAGMENT_CAR_ILLEGALLY:
                if (mFragmentCarIllegally == null) {
                    mFragmentCarIllegally = new CarIllegallyFragment();
                    ft.add(R.id.content, mFragmentCarIllegally);
                } else {
                    ft.show(mFragmentCarIllegally);
                }
                break;
        }
        ft.commit();
    }

    private void hideFragment(FragmentTransaction ft) {
        if (mFragmentMain != null)
            ft.hide(mFragmentMain);
        if (mFragmentMusic != null)
            ft.hide(mFragmentMusic);
        if (mFragmentCarMaintain != null)
            ft.hide(mFragmentCarMaintain);
        if (mFragmentOrders != null)
            ft.hide(mFragmentOrders);
        if (mFragmentCarIllegally != null)
            ft.hide(mFragmentCarIllegally);
    }

    private long exitTime = 0;

    private void doExitApp() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.showMessage(this, getString(R.string.double_click_quit));
            exitTime = System.currentTimeMillis();
        } else {
            AppManager.getAppManager().AppExit(this);
        }
    }
}
