package com.science.carnetplus.util;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetDataCallback;
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

    public interface OnAVOSCallback {
        void getAvaterListener(byte[] avatarBytes);

        void getUserInfoListener(List<AVObject> userInfoList);

        void getCarListListener(List<AVObject> carList);
    }

    private volatile static AVOSUtils avosUtils;

    private AVOSUtils() {
    }

    public static AVOSUtils getInstance() {
        if (avosUtils == null) {
            synchronized (AVOSUtils.class) {
                if (avosUtils == null) {
                    avosUtils = new AVOSUtils();
                }
            }
        }
        return avosUtils;
    }

    /**
     * 注册
     */
    public void signUp(String mobilePhone, String password, String installationId,
                       SignUpCallback signUpCallback) {
        AVUser user = new AVUser();
        user.setUsername(mobilePhone);
        user.setPassword(password);
        user.setMobilePhoneNumber(mobilePhone);
        user.put("installationId", installationId);
        user.signUpInBackground(signUpCallback);
    }

    /**
     * 首次注册保存用户头像
     */
    public void upLoadUserAvatar(String username, String avatarUrl, SaveCallback saveCallback) {
        AVFile imageFile = null;

        try {
            imageFile = AVFile.withAbsoluteLocalPath("avatar.jpg", avatarUrl);
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
     * 重置头像
     */
    public void resetAvatar(String username, final String avatarUrl) {
        AVFile imageFile = null;
        try {
            imageFile = AVFile.withAbsoluteLocalPath("avatar.jpg", avatarUrl);
            imageFile.save();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AVException e) {
            e.printStackTrace();
        }

        final AVFile finalImageFile = imageFile;
        final AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list != null && list.size() != 0) {
                    final String objectId = list.get(list.size() - 1).getObjectId();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                AVObject gender = query.get(objectId);
                                gender.put("avatar", finalImageFile);
                                gender.save();
                            } catch (AVException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }

    /**
     * 获取用户头像
     */
    public void getUserAvatar(String username, final OnAVOSCallback callback) {
        AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (list != null && list.size() != 0) {
                    String objectId = list.get(list.size() - 1).getObjectId();
                    getAvatarFile(objectId, callback);
                }
            }
        });
    }

    /**
     * 得到头像文件
     *
     * @param objectId
     */
    private void getAvatarFile(final String objectId, final OnAVOSCallback callback) {

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
                imageFile.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, AVException e) {
                        callback.getAvaterListener(bytes);
                    }
                });

            }

        }).start();
    }

    /**
     * 更新用户信息:昵称
     *
     * @param username
     * @param nickname
     */
    public void updateUserInfo(String username, final String nickname, final String describe,
                               final String sex, final String birth, final String hometown) {
        final AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(final List<AVObject> list, AVException e) {
                if (list != null && list.size() != 0) {
                    final String objectId = list.get(list.size() - 1).getObjectId();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            AVObject avObject = null;
                            try {
                                avObject = query.get(objectId);
                                avObject.put(CommonDefine.NICKNAME, nickname);
                                avObject.put(CommonDefine.DESCRIBE, describe);
                                avObject.put(CommonDefine.SEX, sex);
                                avObject.put(CommonDefine.BIRTH, birth);
                                avObject.put(CommonDefine.HOMETOWN, hometown);
                                avObject.save();
                            } catch (AVException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
    }

    /**
     * 得到用户信息
     *
     * @param username
     */
    public void getUserInfo(String username, final OnAVOSCallback callback) {
        AVQuery<AVObject> query = new AVQuery<AVObject>("UserInfo");
        query.whereEqualTo("username", username);
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    callback.getUserInfoListener(avObjects);
                }
            }
        });
    }

    /**
     * 保存车辆信息
     *
     * @param carNumber
     * @param carOilNumber
     * @param carBrand
     * @param carType
     * @param carColor
     * @param saveCallback
     */
    public void saveCarInfo(String username, String carNumber, String carOilNumber, String carBrand,
                            String carType, String carColor, SaveCallback saveCallback) {
        AVObject avObject = new AVObject("CarInfo");
        avObject.put("username", username);
        avObject.put("carNumber", carNumber);
        avObject.put("carOilNumber", carOilNumber);
        avObject.put("carBrand", carBrand);
        avObject.put("carType", carType);
        avObject.put("carColor", carColor);
        avObject.saveInBackground(saveCallback);
    }

    /**
     * 得到车辆信息
     */
    public void getCarList(String username, final OnAVOSCallback callback) {
        AVQuery<AVObject> query = new AVQuery<AVObject>("CarInfo");
        query.whereEqualTo("username", username);
        // 根据 createdAt 字段降序显示数据
        query.orderByDescending("createdAt");
        query.findInBackground(new FindCallback<AVObject>() {
            public void done(List<AVObject> avObjects, AVException e) {
                if (e == null) {
                    callback.getCarListListener(avObjects);
                }
            }
        });
    }

    // 退出登录
    public void logout() {
        AVUser.logOut();
    }
}
