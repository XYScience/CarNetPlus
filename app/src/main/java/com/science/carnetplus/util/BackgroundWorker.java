package com.science.carnetplus.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author 幸运Science-陈土燊
 * @description
 * @email chentushen.science@gmail.com,274240671@qq.com
 * @data 2016/4/17
 */
public class BackgroundWorker {

    private ExecutorService mThreadPool;
    private volatile static BackgroundWorker sBackgroundWorker;

    public static BackgroundWorker getInstance() {
        if (sBackgroundWorker == null) {
            synchronized (BackgroundWorker.class) {
                if (sBackgroundWorker == null) {
                    sBackgroundWorker = new BackgroundWorker();
                }
            }
        }
        return sBackgroundWorker;
    }

    private BackgroundWorker() {
        mThreadPool = Executors.newCachedThreadPool();
    }

    public Future submitTask(Runnable task) {
        return mThreadPool.submit(task);
    }
}
