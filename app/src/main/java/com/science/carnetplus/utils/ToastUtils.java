package com.science.carnetplus.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author 幸运Science-陈土燊
 * @description toast工具类
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/5
 */

public class ToastUtils {

    private static Toast toast = null;

    public static void showMessage(final Context act, final String msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    public static void showMessage(final Context act, final int msg) {
        showMessage(act, msg, Toast.LENGTH_SHORT);
    }

    public static void showMessage(final Context act, final String msg, final int len) {
        if (toast == null) {
            toast = Toast.makeText(act, msg, len);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void showMessage(final Context act, final int msg, final int len) {
        if (toast == null) {
            toast = Toast.makeText(act, msg, len);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }

    public static void hideToast() {
        if (toast != null) {
            toast.cancel();
        }
    }
}
