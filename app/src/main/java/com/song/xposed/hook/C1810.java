package com.song.xposed.hook;

import android.util.Log;

import de.robv.android.xposed.XposedHelpers;

public class C1810 {
    /* renamed from: ˉ  reason: contains not printable characters */
    public static void m8133(Class<?> cls, String str, Object obj) {
        try {
            XposedHelpers.setStaticObjectField(cls, str, obj);
        } catch (Exception e) {
            Log.e("XposedHelpers", "setStaticObjectField: " + e.getLocalizedMessage(), e);
        }
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    public static void m8134(Object obj, String str, int i) {
        try {
            XposedHelpers.setIntField(obj, str, i);
        } catch (Exception e) {
            Log.e("XposedHelpers", "setStaticObjectField: " + e.getLocalizedMessage(), e);
        }
    }
}