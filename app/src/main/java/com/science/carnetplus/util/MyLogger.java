package com.science.carnetplus.util;

import android.util.Log;

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
     * 原始打印方法
     *
     * @param msg 信息
     */
    private static void takeLogE(String className, String methodName, String msg) {
        if (IS_DEBUG) {
            //为了看起来养眼,对类名前缀的长度进行统一
            if (className.length() > 24) {
                className = className.substring(0, 24) + "...";
            }
            if (methodName.length() > 24) {
                methodName = methodName.substring(0, 24) + "...:";
            }
            Log.e(className, methodName + msg);
        }
    }

    public static void e(Object msg) {
        String className = getClassName();
        String methodLine = getMethodLine();
        takeLogE(className, methodLine, String.valueOf(msg));
    }

    /**
     * 打印的时候带上类名
     *
     * @return 类名前缀
     */
    public static String getClassName() {
        if (!IS_DEBUG) {
            return null;
        }
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        String className = traceElement.getFileName();
        //去除文件名中的后缀
        if (className.contains(".java")) {
            className = className.substring(0, className.length() - 5);
        }
        return className + ">>>>>>>>>>>>>>>>>>>>>>>>";
    }

    public static String getMethodName() {
        if (!IS_DEBUG) {
            return null;
        }
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        String methodName = traceElement.getMethodName();
        return methodName + ">>>>>>>>>>>>>>>>>>>>>>>>";
    }

    public static String getMethodLine() {
        if (!IS_DEBUG) {
            return null;
        }
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[2];
        String methodName = traceElement.getMethodName();
        int methodLine = traceElement.getLineNumber();
        return methodName + "-" + methodLine + ">>>>>>>>>>>>>>>>>>>>>>>>";
    }
}
