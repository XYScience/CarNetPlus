package com.science.carnetplus.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.science.carnetplus.AppManager;
import com.science.carnetplus.R;
import com.science.carnetplus.utils.StatusBarCompat;

/**
 * @author 幸运Science-陈土燊
 * @description 基类
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/8/27
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);

        initView();
        // 状态栏着色 4.4之后加入windowTranslucentStatus的属性之后，也就是我们可以用到状态栏的区域了
        StatusBarCompat.compat(this);
        initData();
        initListener();
    }

    public Toolbar setToolbar(String title) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(title); // 标题的文字需在setSupportActionBar之前，不然会无效;getSupportActionBar().setTitle("标题");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 显示返回键
        return toolbar;
    }

    public abstract void initView();

    public abstract void initData();

    public abstract void initListener();
}
