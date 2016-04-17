package com.science.carnetplus.widget;

import android.view.View;

/**
 * @author 幸运Science-陈土燊
 * @description 防止Button的频繁点击, 多次执行点击事件
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/4/16
 */
public abstract class OnSingleClickListener implements View.OnClickListener {

    private static long lastTime;

    public abstract void onSingleClick(View v);

    @Override
    public void onClick(View v) {

        if (isDoubleClick()) {
            return;
        }
        onSingleClick(v);
    }

    private boolean isDoubleClick() {
        boolean flag = false;
        long time = System.currentTimeMillis() - lastTime;
        if (time > 500) {
            return true;
        }
        lastTime = System.currentTimeMillis();
        return flag;
    }
}
