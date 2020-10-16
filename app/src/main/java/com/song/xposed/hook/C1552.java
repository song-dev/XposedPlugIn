package com.song.xposed.hook;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/* renamed from: android.support.v7.app.ˏᵢʿ  reason: contains not printable characters */
public class C1552 {

    /* renamed from: ˉ  reason: contains not printable characters */
    private static final ConcurrentMap<String, List<Locale>> f4989 = new ConcurrentHashMap();

    /* renamed from: ˏ  reason: contains not printable characters */
    private static final ConcurrentMap<String, List<Locale>> f4990 = new ConcurrentHashMap();

    /* renamed from: ˉ  reason: contains not printable characters */
    public static Locale m7149(String str) {
        if (str == null) {
            return null;
        }
        if (str.isEmpty()) {
            return new Locale("", "");
        }
        if (!str.contains("#")) {
            int length = str.length();
            if (length < 2) {
                throw new IllegalArgumentException("Invalid locale format: " + str);
            } else if (str.charAt(0) != '_') {
                return m7150(str);
            } else {
                if (length >= 3) {
                    char charAt = str.charAt(1);
                    char charAt2 = str.charAt(2);
                    if (!Character.isUpperCase(charAt) || !Character.isUpperCase(charAt2)) {
                        throw new IllegalArgumentException("Invalid locale format: " + str);
                    } else if (length == 3) {
                        return new Locale("", str.substring(1, 3));
                    } else {
                        if (length < 5) {
                            throw new IllegalArgumentException("Invalid locale format: " + str);
                        } else if (str.charAt(3) == '_') {
                            return new Locale("", str.substring(1, 3), str.substring(4));
                        } else {
                            throw new IllegalArgumentException("Invalid locale format: " + str);
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Invalid locale format: " + str);
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid locale format: " + str);
        }
    }

    /* renamed from: ˏ  reason: contains not printable characters */
    private static Locale m7150(String str) {
        if (m7151(str)) {
            return new Locale(str);
        }
        String[] split = str.split("_", -1);
        String str2 = split[0];
        if (split.length == 2) {
            String str3 = split[1];
            if ((m7151(str2) && m7153(str3)) || m7152(str3)) {
                return new Locale(str2, str3);
            }
        } else if (split.length == 3) {
            String str4 = split[1];
            String str5 = split[2];
            if (m7151(str2) && ((str4.isEmpty() || m7153(str4) || m7152(str4)) && !str5.isEmpty())) {
                return new Locale(str2, str4, str5);
            }
        }
        throw new IllegalArgumentException("Invalid locale format: " + str);
    }

    /* renamed from: ˑ  reason: contains not printable characters */
    private static boolean m7151(String str) {
        return C0618.m3381(str) && (str.length() == 2 || str.length() == 3);
    }

    /* renamed from: י  reason: contains not printable characters */
    private static boolean m7152(String str) {
        return C0618.m3379(str) && str.length() == 3;
    }

    /* renamed from: ᵢ  reason: contains not printable characters */
    private static boolean m7153(String str) {
        return C0618.m3382(str) && str.length() == 2;
    }
}