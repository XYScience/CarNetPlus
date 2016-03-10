package com.science.carnetplus.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/3/9
 */

public class FileUtil {

    /**
     * 将Bitmap 图片保存到本地路径，并返回路径
     *
     * @param c
     * @param fileName 文件名称
     * @param bitmap   图片
     * @return
     */
    public static String saveAvatarFile(Context c, String fileName, Bitmap bitmap) {
        return saveAvatarFile(c, "", fileName, bitmap);
    }

    public static String saveAvatarFile(Context c, String filePath, String fileName, Bitmap bitmap) {
        byte[] bytes = bitmapToBytes(bitmap);
        return saveAvatarFile(c, filePath, fileName, bytes);
    }

    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static String saveAvatarFile(Context c, String strFilePath, String fileName, byte[] bytes) {
        String fileFullPath = "";
        FileOutputStream fos = null;
        try {
            if (strFilePath == null || strFilePath.trim().length() == 0) {
                strFilePath = Environment.getExternalStorageDirectory() + CommonDefine.AVATAR_FILE_DIR;
            }
            File file = new File(strFilePath); // 创建存放头像文件夹
            if (!file.exists()) {
                file.mkdirs();
            }
            File fullFile = new File(strFilePath, fileName);
            fos = new FileOutputStream(fullFile);
            fos.write(bytes);
            fileFullPath = fullFile.getPath();
            saveAvatarFilePath(fileFullPath, c);
        } catch (Exception e) {
            fileFullPath = "";
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    fileFullPath = "";
                }
            }
        }
        return fileFullPath;
    }

    /**
     * 裁剪图片
     *
     * @param uri
     * @param activity
     */
    public static void startPhotoZoom(Uri uri, Activity activity) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, CommonDefine.IMAGE_UNSPECIFIED);
        // crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        activity.startActivityForResult(intent, CommonDefine.CROP_REQUEST_CODE);
    }

    /**
     * 创建一个以当前时间为名称的文件,保存路径放在根目录下
     * Constructs a new file using the specified path.
     *
     * @return 文件uri
     */
    public static Uri getCameraStorageUri() {
        File tempFile = new File(Environment.getExternalStorageDirectory() + "/" + CommonDefine.AVATAR_FILE_NAME);
        return Uri.fromFile(tempFile);
    }

    // 使用系统当前日期加以调整作为照片的名称
    public static String getPictureFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".png";
    }

    /**
     * 压缩图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap compressBitmap(Bitmap bitmap) {
        Bitmap bm = null;
        bm = bitmap;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, stream);// (0 - 100)压缩文件
        return bm;
    }

    /**
     * 保存头像地址
     *
     * @param path
     * @param context
     */
    public static void saveAvatarFilePath(String path, Context context) {
        SharedPreferences sp = context.getSharedPreferences(CommonDefine.AVATAR_FILE, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CommonDefine.AVATAR_FILE_PATH, path);
        editor.commit();
    }

    /**
     * 得到头像地址
     *
     * @param context
     * @return
     */
    public static String getAvatarFilePath(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CommonDefine.AVATAR_FILE, context.MODE_PRIVATE);
        String avatarFilePath = sp.getString(CommonDefine.AVATAR_FILE_PATH, "");
        return avatarFilePath;
    }

    /**
     * 得到头像Bitmap
     *
     * @param path
     * @return 判断是否为空!
     */
    public static Bitmap getAvatar(String path) {
        File file = new File(path);
        if (file.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(path);
            return bitmap;
        }
        return null;
    }
}
