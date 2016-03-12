package com.science.carnetplus.ui;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.science.carnetplus.AppManager;
import com.science.carnetplus.R;
import com.science.carnetplus.utils.CommonDefine;
import com.science.carnetplus.utils.StatusBarCompat;

/**
 * @author 幸运Science-陈土燊
 * @description 基类
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/8/27
 */
public abstract class BaseActivity extends AppCompatActivity {

    private PermissionCallback mPermissionCallback;

    public interface PermissionCallback {
        void hasPermission();

        void noPermission();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);

        initView();
        initData();
        initListener();
    }

    public Toolbar setToolbar(String title) {
        // 状态栏着色 4.4之后加入windowTranslucentStatus的属性之后，也就是我们可以用到状态栏的区域了
        StatusBarCompat.compat(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title); // 标题的文字需在setSupportActionBar之前，不然会无效;getSupportActionBar().setTitle("标题");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 显示返回键
        return toolbar;
    }

    public void performCodeWithPermission(String permissionTip, PermissionCallback permissionCallback, String... permissions) {
        if (permissions == null || permissions.length == 0) {
            return;
        } else {
            mPermissionCallback = permissionCallback;
            if (checkPermissionGranted(permissions)) {
                // 有权限
                if (mPermissionCallback != null) {
                    mPermissionCallback.hasPermission();
                    mPermissionCallback = null;
                }
            } else {
                // 没有权限
                requestPermission(permissionTip, permissions);
            }
        }
    }

    private void requestPermission(final String permissionTip, final String[] permissions) {
        if (shouldShowRequestPermissionRationale(permissions)) {

            //如果用户之前拒绝过此权限，再提示一次准备授权相关权限
            new AlertDialog.Builder(this)
                    .setTitle(getString(R.string.tip))
                    .setMessage(permissionTip)
                    .setPositiveButton(R.string.allow, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(BaseActivity.this, permissions, CommonDefine.REQUEST_PERMISSION_CODE);
                        }
                    }).show();
        } else {
            ActivityCompat.requestPermissions(BaseActivity.this, permissions, CommonDefine.REQUEST_PERMISSION_CODE);
        }
    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        boolean flag = false;
        for (String p : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, p)) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 检测应用是否已经具有权限
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissionGranted(String[] permissions) {
        boolean flag = true;
        for (String p : permissions) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                flag = false; // 没有权限
                break;
            }
        }
        return flag;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == CommonDefine.REQUEST_PERMISSION_CODE) {
            if (verifyPermissions(grantResults)) {
                if (mPermissionCallback != null) {
                    mPermissionCallback.hasPermission();
                    mPermissionCallback = null;
                }
            } else {
                if (mPermissionCallback != null) {
                    mPermissionCallback.noPermission();
                    mPermissionCallback = null;
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    public boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();
}
