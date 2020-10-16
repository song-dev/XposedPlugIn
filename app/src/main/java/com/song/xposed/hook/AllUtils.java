package com.song.xposed.hook;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public final class AllUtils {

    private static final ConcurrentMap<String, List<Locale>> f4989 = new ConcurrentHashMap<>();

    private static final ConcurrentMap<String, List<Locale>> f4990 = new ConcurrentHashMap<>();

    public static Locale getLocale(String language) {
        if (language == null) {
            return null;
        }
        if (language.isEmpty()) {
            return new Locale("", "");
        }
        if (!language.contains("#")) {
            int length = language.length();
            if (length < 2) {
                throw new IllegalArgumentException("Invalid locale format: " + language);
            } else if (language.charAt(0) != '_') {
                return m7150(language);
            } else {
                if (length >= 3) {
                    char charAt = language.charAt(1);
                    char charAt2 = language.charAt(2);
                    if (!Character.isUpperCase(charAt) || !Character.isUpperCase(charAt2)) {
                        throw new IllegalArgumentException("Invalid locale format: " + language);
                    } else if (length == 3) {
                        return new Locale("", language.substring(1, 3));
                    } else {
                        if (length < 5) {
                            throw new IllegalArgumentException("Invalid locale format: " + language);
                        } else if (language.charAt(3) == '_') {
                            return new Locale("", language.substring(1, 3), language.substring(4));
                        } else {
                            throw new IllegalArgumentException("Invalid locale format: " + language);
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Invalid locale format: " + language);
                }
            }
        } else {
            throw new IllegalArgumentException("Invalid locale format: " + language);
        }
    }

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

    private static boolean m7151(String str) {
        return StringUtils.isLowerString(str) && (str.length() == 2 || str.length() == 3);
    }

    private static boolean m7152(String str) {
        return StringUtils.isDigit(str) && str.length() == 3;
    }

    private static boolean m7153(String str) {
        return StringUtils.isUpperString(str) && str.length() == 2;
    }
}