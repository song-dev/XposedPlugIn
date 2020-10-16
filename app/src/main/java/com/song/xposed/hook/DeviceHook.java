package com.song.xposed.hook;

import android.app.Application;

import com.song.xposed.BuildConfig;

import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class DeviceHook {

    private static final List<String> FILTER = Arrays.asList("android", "me.weishu.exp", "de.robv.android.xposed.installer");

    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (loadPackageParam.packageName.equalsIgnoreCase(BuildConfig.APPLICATION_ID)) {
            XposedBridgeUtils.hookAllMethods(loadPackageParam.classLoader,
                    "com.song.xposed.MainApplication", "isXposedWorked", new ValueMethodHook(true));
            return;
        }
        if (!FILTER.contains(loadPackageParam.packageName)) {
            XposedBridgeUtils.hookAllMethods(Application.class, "onCreate", new DeviceMethodHook(loadPackageParam));
        }
    }
}