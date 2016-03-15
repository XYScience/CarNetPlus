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
import android.text.TextUtils;
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
     * 判断网络是否连接
     *
     * @param mContext
     * @return
     */
    public static boolean isConnected() {

        ConnectivityManager connectivity = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (null != connectivity) {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
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
     * 验证手机格式
     */
    public static boolean isMobilePhone(String mobiles) {
        /*
        移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		联通：130、131、132、152、155、156、185、186
		电信：133、153、180、189、（1349卫通）
		总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		*/
        String telRegex = "[1][358]\\d{9}"; //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，
        // "\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return mobiles.matches(telRegex);
        }
    }

    /**
     * 密码校验
     * 由数字和字母组成，并且要同时含有数字和字母，且长度要在6-16位之间
     */
    public static boolean passwordVerify(String password) {
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,16}$";
        return password.matches(regex);
    }

    /**
     * 验证码校验
     * 判断验证码是否为 6 位纯数字，LeanCloud 统一的验证码均为 6 位纯数字。
     *
     * @param smsCode
     * @return
     */
    public static boolean isSMSCodeValid(String smsCode) {
        String regex = "^\\d{6}$";
        return smsCode.matches(regex);
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
