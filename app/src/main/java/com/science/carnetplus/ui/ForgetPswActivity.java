package com.science.carnetplus.ui;

import android.os.Bundle;
import android.view.MenuItem;

import com.science.carnetplus.R;

/**
 * @author 幸运Science-陈土燊
 * @description 忘记密码
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/8
 */

public class ForgetPswActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_forget_psw);
        setToolbar(getString(R.string.forget_psw));
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
