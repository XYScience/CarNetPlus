package com.science.carnetplus.util;

import android.util.Log;

import java.text.SimpleDateFormat;

/**
 * @author 幸运Science-陈土燊
 * @description 养眼log~~
 * @school University of South China
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/4/2
 */

public class MyLogger {

    public static boolean IS_DEBUG = true;

    /**
     * 最终的打印方法
     * 打印内容：tag:>>>>>>>>>>;
     * msg:方法名(类名.java：行数): 要打印的信息
     *
     * @param msg 信息
     */
    private static void takeLogE(String methodLine, String msg) {
        Log.e(">>>>>>>>>>", methodLine + msg);
    }

    public static void e(Object msg) {
        if (IS_DEBUG) {
            String methodLine = getMethodAndLine();
            takeLogE(methodLine, String.valueOf(msg));
        }
    }

    /**
     * 打印方法的类名
     *
     * @return 类名前缀
     */
    public static String getClassName() {
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        String className = traceElement.getFileName();
        //去除文件名中的后缀
        if (className.contains(".java")) {
            className = className.substring(0, className.length() - 5);
        }
        return className;
    }

    /**
     * 打印的方法+类名+所在行数(显示超链)
     */
    private static String getMethodAndLine() {
        String result = "";
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        result += traceElement.getMethodName();
        result += "(" + traceElement.getFileName();
        result += ":" + traceElement.getLineNumber() + "): ";
        return result;
    }

    /**
     * 打印分割线,主要用于程序启动或销毁时在log中做标记
     *
     * @param msg 信息
     */
    public static void line(String msg) {
        if (IS_DEBUG) {
            takeLogE("---====================================---\n" +
                    "\t我是" + msg + "的分割线-" + getCurrentTime() +
                    "\n---====================================---", "(●'◡'●)");
        }
    }

    public static String getCurrentTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(System.currentTimeMillis());
    }
}
