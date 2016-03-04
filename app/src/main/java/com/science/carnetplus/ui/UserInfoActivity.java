package com.science.carnetplus.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.science.carnetplus.R;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/4
 */

public class UserInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_user_info);
        setToolbar("我的");
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }
}
