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

    private static Snackbar mSnackbar;
    private static SnackbarUtils snackbarUtils;

    private SnackbarUtils() {

    }

    public static synchronized SnackbarUtils getInstance() {
        if (snackbarUtils == null) {
            snackbarUtils = new SnackbarUtils();
        }
        return snackbarUtils;
    }

    public static void showSnackbar(View view, String msg) {

        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
        } else {
            mSnackbar.setText(msg);
        }
        mSnackbar.show();
    }

    public static void showSnackbar(View view, String msg, int length) {

        if (mSnackbar == null) {
            mSnackbar = Snackbar.make(view, msg, length);
        } else {
            mSnackbar.setText(msg);
        }
        mSnackbar.show();
    }

    public static void hideSnackbar() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }
}
