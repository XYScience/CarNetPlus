package com.science.carnetplus.util;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.science.carnetplus.R;


/**
 * @author 幸运Science-陈土燊
 * @description
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/4
 */
public class StatusBarCompat {
    private static final int INVALID_VAL = -1;

    public static void compat(Activity activity, int statusColor, boolean kk_status) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor == INVALID_VAL) {
                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.colorPrimaryDark));
            } else {
                activity.getWindow().setStatusBarColor(statusColor);
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            if (kk_status) {
                ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
                View statusBarView = contentView.getChildAt(0);
                //改变颜色时避免重复添加statusBarView
                if (statusBarView != null && statusBarView.getMeasuredHeight() == CommonUtils.getStatusBarHeight(activity)) {
                    statusBarView.setBackgroundColor(CommonDefine.KITKAT_STATUS_COLOR);
                    return;
                }
                statusBarView = new View(activity);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        CommonUtils.getStatusBarHeight(activity));
                statusBarView.setBackgroundColor(CommonDefine.KITKAT_STATUS_COLOR);
                contentView.addView(statusBarView, lp);
            }
        }

    }

    public static void compat(Activity activity) {
        compat(activity, INVALID_VAL, true);
    }

}
