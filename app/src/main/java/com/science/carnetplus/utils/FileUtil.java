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
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
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
            saveAvatarFilePath(fileFullPath, c);
            fileFullPath = fullFile.getPath();
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
     * 质量压缩图片，第一次压缩后若大于100kb。则继续压缩
     *
     * @return
     */
    public static Bitmap compressImageSec(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 100) {    //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 5;//每次都减少5
        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return image;
    }

    /**
     * 比例压缩图片
     *
     * @return
     */
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, stream);// (0 - 100)压缩文件
        return image;

        //比例压缩图片
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
//            baos.reset();//重置baos即清空baos
//            image.compress(Bitmap.CompressFormat.JPEG, 80, baos);//这里压缩80%，把压缩后的数据存放到baos中
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//        BitmapFactory.Options newOpts = new BitmapFactory.Options();
//        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
//        newOpts.inJustDecodeBounds = true;
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
//        newOpts.inJustDecodeBounds = false;
//        int w = newOpts.outWidth;
//        int h = newOpts.outHeight;
//        float hh = 1920f;//这里设置高度为1920f
//        float ww = 1080f;//这里设置宽度为1080f
//        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
//        int be = 1;//be=1表示不缩放
//        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
//            be = (int) (newOpts.outWidth / ww);
//        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
//            be = (int) (newOpts.outHeight / hh);
//        }
//        if (be <= 0)
//            be = 1;
//        newOpts.inSampleSize = be;//设置缩放比例
//        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
//        isBm = new ByteArrayInputStream(baos.toByteArray());
//        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
//        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
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
