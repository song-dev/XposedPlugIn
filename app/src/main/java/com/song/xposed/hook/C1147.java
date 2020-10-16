package com.song.xposed.hook;

import android.app.Application;

import java.util.Arrays;
import java.util.List;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/* renamed from: android.support.v7.app.ˊˈˑ  reason: contains not printable characters */
public class C1147 {

    private static final List<String> FILTER = Arrays.asList("android", "me.weishu.exp", "de.robv.android.xposed.installer");

    /* renamed from: ˉ  reason: contains not printable characters */
    public void m5330(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        if (loadPackageParam.packageName.equalsIgnoreCase("com.sollyu.xposed.hook.model")) {
            C0429.m2604(loadPackageParam.classLoader, "com.sollyu.kotlin.appenv.Application", "isXposedWorked", new C2847(true));
            return;
        }
        if (!FILTER.contains(loadPackageParam.packageName)) {
            C0429.m2603(Application.class, "onCreate", new DeviceHook(loadPackageParam));
        }
    }
}