package com.song.xposed;


import com.song.xposed.hook.others.GeetestHook;
import com.song.xposed.utils.Constants;
import com.song.xposed.utils.LogUtils;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by chensongsong on 2020/10/16.
 */
public class MainHook implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam loadPackageParam) throws Throwable {
        XposedBridge.log(Constants.TAG + " Loaded app: " + loadPackageParam.packageName);
        try {
//            new DeviceHook().handleLoadPackage(loadPackageParam);
            new GeetestHook(loadPackageParam);
        } catch (Exception e) {
            LogUtils.e("MainHook.handleLoadPackage: " + e.getMessage());
        }
    }
}
