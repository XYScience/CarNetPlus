package com.science.carnetplus.utils;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

import java.io.IOException;
import java.util.List;

/**
 * @author 幸运Science-陈土燊
 * @description leancloud云服务管理类
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/6
 */

public class AVOSUtils {

    private interface OnGetAvatarListener {
        void getAvaterListener(String avatarUrl);
    }

    private OnGetAvatarListener mOnGetAvatarListener = null;

    public void setOnGetAvatarListener(OnGetAvatarListener onGetAvatarListener) {
        this.mOnGetAvatarListener = onGetAvatarListener;
    }

    private static AVOSUtils avosUtils;

    private AVOSUtils() {

    }

    public static synchronized AVOSUtils getInstance() {
        if (avosUtils == null) {
            avosUtils = new AVOSUtils();
        }
        return avosUtils;
    }

    /**
     * 注册
     */
    public static void signUp(String username, String password, String email, String installationId,
                              SignUpCallback signUpCallback) {

        AVUser user = new AVUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.put("installationId", installationId);
        user.signUpInBackground(signUpCallback);
    }

    /**
     * 保存用户头像
     */
    public static void upLoadUserAvatar(String username, String avatarUrl, SaveCallback saveCallback) {
        AVFile imageFile = null;

        try {
            imageFile = AVFile.withAbsoluteLocalPath(username
                    + "_avatar.jpg", avatarUrl);
            imageFile.save();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AVException e) {
            e.printStackTrace();
        }

        AVObject userInformation = new AVObject("UserInfo");
        userInformation.put("username", username);
        userInformation.put("avatar", imageFile);
        userInformation.saveInBackground(saveCallback);
    }

    /**
     * 获取用户头像
     */
    public void getUserAvatar(String username) {
        AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list != null && list.size() != 0) {
                    String objectId = list.get(list.size() - 1).getObjectId();
                    getAvatarFile(objectId);
                }
            }
        });
    }

    /**
     * 得到头像文件
     *
     * @param objectId
     */
    private void getAvatarFile(final String objectId) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
                AVObject gender = null;
                try {
                    gender = query.get(objectId);
                } catch (AVException e) {
                    e.printStackTrace();
                }
                // Retrieving the file
                AVFile imageFile = (AVFile) gender.get("avatar");

                if (mOnGetAvatarListener != null) {
                    mOnGetAvatarListener.getAvaterListener(imageFile.getUrl());
                }
            }

        }).start();
    }


    // 退出登录
    public static void logout() {
        AVUser.logOut();
    }
}
