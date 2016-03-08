package com.science.carnetplus.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.balysv.materialripple.MaterialRippleLayout;

/**
 * @author 幸运Science-陈土燊
 * @description 通用工具类
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/5
 */

public class CommonUtils {

    private static Context mContext;
    private static CommonUtils commonUtils;

    private CommonUtils(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static synchronized CommonUtils getInstance(Context context) {
        if (commonUtils == null) {
            commonUtils = new CommonUtils(context);
        }
        return commonUtils;
    }

    /**
     * 获得屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;

        return screenWidth;
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        int screenHeight = dm.heightPixels;

        return screenHeight;
    }

    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取当前屏幕截图，包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, 0, width, height);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 获取当前屏幕截图，不包含状态栏
     *
     * @param activity
     * @return
     */
    public static Bitmap snapShotWithoutStatusBar(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bmp = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = getScreenWidth(activity);
        int height = getScreenHeight(activity);
        Bitmap bp = null;
        bp = Bitmap.createBitmap(bmp, 0, statusBarHeight, width, height
                - statusBarHeight);
        view.destroyDrawingCache();
        return bp;

    }

    /**
     * 隐藏软键盘
     *
     * @param v
     */
    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 打开软键盘
     *
     * @param v
     */
    public static void showKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(v, 0);
    }

    /**
     * 判断是否有外网连接（普通方法不能判断外网的网络是否连接，比如连接上局域网）
     */
    public static boolean isNetworkUseful() {

        boolean flag = false;
        try {
            String ip = "www.baidu.com";// ping 的地址，可以换成任何一种可靠的外网
            Process p = Runtime.getRuntime().exec("ping -c 2 -w 100 " + ip);// ping网址3次
            StringBuffer stringBuffer = new StringBuffer();
            // ping的状态
            int status = p.waitFor();
            if (status == 0) {
                flag = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            return flag;
        }
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings",
                "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 获取网络状态，wifi,wap,2g,3g.
     *
     * @return 是否是wifi 网络
     */
    public static boolean isWifiNet() {
        ConnectivityManager manager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isWifi = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            String type = networkInfo.getTypeName();
            isWifi = type.equalsIgnoreCase("WIFI");
        }
        return isWifi;
    }

    /**
     * 5.+水波纹特效
     *
     * @param view  加载动画的view
     * @param color 加载的颜色
     */
    public static void materialRipple(View view, String color) {
        MaterialRippleLayout.on(view).rippleColor(Color.parseColor(color)).
                rippleAlpha(0.2f).rippleHover(true).rippleOverlay(true).create();
    }

    /**
     * 5.+水波纹特效
     *
     * @param view 加载动画的view
     */
    public static void materialRipple(View view) {
        MaterialRippleLayout.on(view).rippleColor(Color.parseColor("#585858")).
                rippleAlpha(0.2f).rippleHover(true).rippleOverlay(true).create();
    }

}