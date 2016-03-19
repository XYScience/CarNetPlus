package com.science.carnetplus;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.science.carnetplus.ui.BaseActivity;
import com.science.carnetplus.ui.LoginActivity;
import com.science.carnetplus.utils.AVOSUtils;
import com.science.carnetplus.utils.CommonUtils;
import com.science.carnetplus.utils.FileUtil;
import com.science.carnetplus.utils.StatusBarCompat;
import com.science.carnetplus.utils.ToastUtils;
import com.science.carnetplus.widget.FABToolbar.FABToolbarLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private Toolbar mToolbar;
    private View mHeaderView;
    private FABToolbarLayout mFABToolbarLayout;
    private FloatingActionButton mFABToolbarButton;
    private CircleImageView mImgAvatar;
    private ImageView mImgMusicControl;
    private TextView mTextUsername;
    private TextView mTextUserDescribe;
    private TextView mTextAddOil, mTextCar4s, mTextCarWash, mTextCarPark;

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

        initDrawerLayout();
        initNavigationView();
        initFab();
    }

    private void initDrawerLayout() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    private void initNavigationView() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // android support library 23.1.0+
        mHeaderView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        mImgAvatar = (CircleImageView) mHeaderView.findViewById(R.id.image_avatar);
        mImgMusicControl = (ImageView) mHeaderView.findViewById(R.id.img_music_control);
        mTextUsername = (TextView) mHeaderView.findViewById(R.id.text_username);
        mTextUserDescribe = (TextView) mHeaderView.findViewById(R.id.text_user_describe);
        mTextUserDescribe.setSelected(true);
        mTextUserDescribe.requestFocus();
    }

    private void initFab() {
        mFABToolbarLayout = (FABToolbarLayout) findViewById(R.id.fabtoolbar);
        mFABToolbarButton = (FloatingActionButton) findViewById(R.id.fabtoolbar_fab);
        mTextAddOil = (TextView) findViewById(R.id.text_add_oil);
        mTextCar4s = (TextView) findViewById(R.id.text_car_4s);
        mTextCarWash = (TextView) findViewById(R.id.text_car_wash);
        mTextCarPark = (TextView) findViewById(R.id.text_car_park);
        CommonUtils.materialRipple(mTextAddOil, "#ffffff", 0.3f);
        CommonUtils.materialRipple(mTextCar4s, "#ffffff", 0.3f);
        CommonUtils.materialRipple(mTextCarWash, "#ffffff", 0.3f);
        CommonUtils.materialRipple(mTextCarPark, "#ffffff", 0.3f);
    }

    @Override
    public void initData() {
//        GlideUtils.getInstance(MainActivity.this).setImage(FileUtil.getAvatarFilePath(MainActivity.this), mImgAvatar);
        mImgAvatar.setImageBitmap(FileUtil.getAvatar(FileUtil.getAvatarFilePath(MainActivity.this)));
        mTextUsername.setText(AVUser.getCurrentUser().getUsername());
//        mTextUserDescribe.setText(AVUser.getCurrentUser().getUsername());
    }

    @Override
    public void initListener() {
        mTextAddOil.setOnClickListener(this);
        mTextCar4s.setOnClickListener(this);
        mTextCarWash.setOnClickListener(this);
        mTextCarPark.setOnClickListener(this);
        mImgMusicControl.setOnClickListener(this);

        mFABToolbarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFABToolbarLayout.show();
            }
        });
        mFABToolbarButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mFABToolbarLayout.isFab()) {
                    Toast.makeText(MainActivity.this, "long click!", Toast.LENGTH_SHORT).show();
                }
                return true; // true为不加短按,false为加入短按
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_add_oil:
                break;
            case R.id.text_car_4s:
                break;
            case R.id.text_car_wash:
                break;
            case R.id.text_car_park:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START) || mFABToolbarLayout.isToolbar()) {
            drawer.closeDrawer(GravityCompat.START);
            mFABToolbarLayout.hide();
        } else {
            doExitApp();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mFABToolbarLayout.show();
        }
        return super.onKeyDown(keyCode, event);
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

        if (id == R.id.nav_my) {
            // Handle the camera action
        } else if (id == R.id.nav_music) {

        } else if (id == R.id.nav_orders) {

        } else if (id == R.id.nav_car) {

        } else if (id == R.id.nav_car_illegally) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_about) {

        } else if (id == R.id.nav_exit_app) {
            AVOSUtils.logout();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
