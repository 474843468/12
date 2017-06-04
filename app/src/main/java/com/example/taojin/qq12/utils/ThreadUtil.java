package com.example.taojin.qq12.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by taojin on 2016/9/8.17:29
 */
public class ThreadUtil {

    private static Executor sExecutor = Executors.newSingleThreadExecutor();
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public static  void runOnSubThread(Runnable runnable){
        sExecutor.execute(runnable);
    }

    public static  void runOnUiThread(Runnable runnable){
        sHandler.post(runnable);
    }
}
