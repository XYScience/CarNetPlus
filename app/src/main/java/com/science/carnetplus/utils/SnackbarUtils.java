package com.science.carnetplus.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * @author 幸运Science-陈土燊
 * @description Snackbar工具类
 * @school University of South China
 * @company wiwide.com
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2015/10/12
 */
public class SnackbarUtils {

    public static void showSnackbar(View view, String msg) {

        Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        snackbar.show();
    }

    public static void showSnackbar(View view, String msg, int length) {

        Snackbar snackbar = Snackbar.make(view, msg, length);
        snackbar.show();
    }
}
