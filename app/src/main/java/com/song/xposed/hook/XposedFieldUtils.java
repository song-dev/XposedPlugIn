package com.song.xposed.hook;

import android.util.Log;

import de.robv.android.xposed.XposedHelpers;

public class XposedFieldUtils {

    public static void setStaticObjectField(Class<?> clazz, String fieldName, Object value) {
        try {
            XposedHelpers.setStaticObjectField(clazz, fieldName, value);
        } catch (Exception e) {
            Log.e("XposedHelpers", "setStaticObjectField: " + e.getLocalizedMessage(), e);
        }
    }

    public static void setIntField(Object obj, String fieldName, int value) {
        try {
            XposedHelpers.setIntField(obj, fieldName, value);
        } catch (Exception e) {
            Log.e("XposedHelpers", "setStaticObjectField: " + e.getLocalizedMessage(), e);
        }
    }
}