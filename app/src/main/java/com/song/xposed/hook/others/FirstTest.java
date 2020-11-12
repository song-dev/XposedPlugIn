package com.song.xposed.hook.others;

import android.content.ContentResolver;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class FirstTest implements IXposedHookLoadPackage {

    private static final String TAG = "FirstTest";


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XposedBridge.log("Loaded app: " + lpparam.packageName);
        Log.e(TAG, "MainHook Loaded app: " + lpparam.packageName);

        // hook mobjni 进程
        if (!"com.youjuyouqu.testjg".equals(lpparam.packageName)) {
            return;
        }

        Log.e(TAG, "handleLoadPackage: init hook mobjni");

        try {
            XposedHelpers.findAndHookMethod("android.provider.Settings$Secure", lpparam.classLoader, "getString",
                    ContentResolver.class, String.class, XC_MethodReplacement.returnConstant("testandroidid"));
        } catch (Exception e) {
        }

        try {
            XposedHelpers.findAndHookMethod("android.telephony.TelephonyManager", lpparam.classLoader, "getDeviceId",
                    XC_MethodReplacement.returnConstant("test"));
        } catch (Exception e) {
        }


    }
}
