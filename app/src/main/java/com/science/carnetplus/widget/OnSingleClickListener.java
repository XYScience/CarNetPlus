package com.science.carnetplus.widget;

import android.os.SystemClock;
import android.view.View;

/**
 * @author 幸运Science-陈土燊
 * @description 防止Button的频繁点击, 多次执行点击事件
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/4/16
 */
public abstract class OnSingleClickListener implements View.OnClickListener {

    /**
     * 最短click事件的时间间隔
     */
    private static final long MIN_CLICK_INTERVAL = 600;
    /**
     * 上次click的时间
     */
    private long mLastClickTime;

    /**
     * click响应函数
     *
     * @param v
     * The view that was clicked.
     */
    public abstract void onSingleClick(View v);

    @Override
    public final void onClick(View view){
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClickTime;
        // 有可能2次连击，也有可能3连击，保证mLastClickTime记录的总是上次click的时间
        mLastClickTime = currentClickTime;

        if (elapsedTime <= MIN_CLICK_INTERVAL)
            return;
        onSingleClick(view);
    }
}
