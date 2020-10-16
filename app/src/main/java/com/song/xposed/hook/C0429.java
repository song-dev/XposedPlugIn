package com.song.xposed.hook;

import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;

/* renamed from: android.support.v7.app.ʼיˉ  reason: contains not printable characters */
public class C0429 {
    /* renamed from: ˉ  reason: contains not printable characters */
    public static Class<?> m2602(String str, ClassLoader classLoader) {
        return XposedHelpers.findClass(str, classLoader);
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    public static Set<XC_MethodHook.Unhook> m2603(Class<?> cls, String str, XC_MethodHook xC_MethodHook) {
        try {
            return XposedBridge.hookAllMethods(cls, str, xC_MethodHook);
        } catch (Exception unused) {
            return null;
        }
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    public static Set<XC_MethodHook.Unhook> m2604(ClassLoader classLoader, String str, String str2, XC_MethodHook xC_MethodHook) {
        return m2603(m2602(str, classLoader), str2, xC_MethodHook);
    }
}