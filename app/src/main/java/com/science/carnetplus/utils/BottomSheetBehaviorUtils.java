package com.science.carnetplus.utils;

import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.ViewParent;

/**
 * @author 幸运Science-陈土燊
 * @description 设置 BottomSheetBehavior 没有设置 PeekHeight 或者 PeekHeight为0 的时候.
 * 底部的BottomSheetBehavior 视图滑动不出来.
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/7
 */

public class BottomSheetBehaviorUtils {

    public static void updateOffsets(View view) {

        // Manually invalidate the view and parent to make sure we get drawn pre-M
        if (Build.VERSION.SDK_INT < 23) {
            tickleInvalidationFlag(view);
            final ViewParent vp = view.getParent();
            if (vp instanceof View) {
                tickleInvalidationFlag((View) vp);
            }
        }
    }

    private static void tickleInvalidationFlag(View view) {
        final float y = ViewCompat.getTranslationY(view);
        ViewCompat.setTranslationY(view, y + 1);
        ViewCompat.setTranslationY(view, y);
    }
}
