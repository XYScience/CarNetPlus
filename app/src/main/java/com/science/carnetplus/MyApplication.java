package com.science.carnetplus;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/4
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        AVOSCloud.initialize(this, "xRMhJH081RiDNuG3WYsAMGwq-gzGzoHsz", "IfwcLPmeqpyEvhcJWSg0lW2M");
    }
}
