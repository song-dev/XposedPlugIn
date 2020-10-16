package com.song.xposed;

import android.app.Application;

/**
 * Created by chensongsong on 2020/8/11.
 */
public class MainApplication extends Application implements Thread.UncaughtExceptionHandler {

    private static MainApplication instance = null;

    @Override
    public void onCreate() {
        super.onCreate();
        MainApplication.instance = this;
    }

    public synchronized static MainApplication getInstance() {
        return instance;
    }

    /**
     * @return 检查XPOSED是否工作
     */
    public boolean isXposedWork() {
        return false;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
