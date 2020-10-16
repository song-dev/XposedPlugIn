package com.song.xposed;


import android.util.Log;

import com.song.xposed.hook.C1147;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by chensongsong on 2020/10/16.
 */
public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedBridge.log("Loaded app: " + loadPackageParam.packageName);
        try {
            new C1147().m5330(loadPackageParam);
        } catch (Exception e) {
            Log.e("XposedEntry", "AppEnv Error: " + e.getLocalizedMessage(), e);
        }

    }
}
