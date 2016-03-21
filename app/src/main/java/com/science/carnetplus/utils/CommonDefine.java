package com.science.carnetplus.utils;

import android.graphics.Color;

/**
 * @author 幸运Science-陈土燊
 * @description 通用常量相关
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/9
 */

public class CommonDefine {

    public static final int FRAGMENT_MAIN = 0;
    public static final int FRAGMENT_MUSIC = 1;
    public static final int FRAGMENT_CAR_MAINTAIN = 2;
    public static final int FRAGMENT_ORDERS = 3;
    public static final int FRAGMENT_CAR_ILLEGALLY = 4;

    public static final int CAMERA_REQUEST_CODE = 1;// 拍照
    public static final int GALLERY_REQUEST_CODE = 2; // 缩放
    public static final int CROP_REQUEST_CODE = 3;// 结果
    public static final String IMAGE_UNSPECIFIED = "image/*"; // 手机里的图片类型
    public static final String AVATAR_FILE_NAME = "avatar.jpg";// 头像文件名称
    public static final String AVATAR_FILE_DIR = "/CarNetPlus/avatar/";// 头像文件夹
    public static final String AVATAR_FILE = "avatar_file"; // SharedPreferences 头像
    public static final String AVATAR_FILE_PATH = "avatar_file_path"; //  SharedPreferences key:头像路径

    // requestCode
    public static final int REQUEST_PERMISSION_CODE = 88; // 请求权限
    public static final int KITKAT_STATUS_COLOR = Color.parseColor("#30000000"); // 请求权限
    public static final String LOGIN = "login"; // SharedPreferences
    public static final String MOBILE_PHONE = "mobile_phone"; // SharedPreferences key
    public static final String PASSWORD = "password"; // SharedPreferences key

    // 用户信息字段
    public static final String DESCRIBE = "describe";
    public static final String NICKNAME = "nickname";
    public static final String SEX = "sex";
    public static final String BIRTH = "birth";
    public static final String HOMETOWN = "hometown";

    public static final int INTENT_REQUSET = 1; // ForResult的意图
}
