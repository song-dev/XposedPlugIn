package com.song.xposed.hook;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

public final class XposedBridgeUtils {

    public static Class<?> findClass(String className, ClassLoader classLoader) {
        return XposedHelpers.findClass(className, classLoader);
    }

    public static Set<XC_MethodHook.Unhook> hookAllMethods(Class<?> hookClass, String methodName, XC_MethodHook callback) {
        try {
            return XposedBridge.hookAllMethods(hookClass, methodName, callback);
        } catch (Exception unused) {
            return null;
        }
    }

    public static Set<XC_MethodHook.Unhook> hookAllMethods(ClassLoader classLoader, String className, String methodName, XC_MethodHook callback) {
        return hookAllMethods(findClass(className, classLoader), methodName, callback);
    }
}