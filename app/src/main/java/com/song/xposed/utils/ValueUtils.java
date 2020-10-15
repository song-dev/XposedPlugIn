package com.song.xposed.utils;

import java.util.HashSet;
import java.util.Set;

public class ValueUtils {

    private static Integer castInt(Boolean bool) {
        if (bool == null) {
            return null;
        }
        return bool ? 1 : 0;
    }

    public static Object castObject(Object value, int type) {
        if (type != 0) {
            switch (type) {
                case 1:
                    return (String) value;
                case 2:
                    return parseSet((String) value);
                case 3:
                    return (Integer) value;
                case 4:
                    return (Long) value;
                case 5:
                    return (Float) value;
                case 6:
                    try {
                        return castBool(value);
                    } catch (ClassCastException e) {
                        throw new IllegalArgumentException("Expected type " + type + ", got " + value.getClass(), e);
                    }
                default:
                    throw new IllegalArgumentException("Unknown type: " + type);
            }
        } else if (value == null) {
            return null;
        } else {
            throw new IllegalArgumentException("Expected null, got non-null value");
        }
    }

    public static String castString(Set<String> set) {
        if (set == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String replace : set) {
            sb.append(replace.replace("\\", "\\\\").replace(";", "\\;"));
            sb.append(';');
        }
        return sb.toString();
    }

    public static Set<String> castSet(Object obj) {
        return (Set<String>) obj;
    }

    public static Set<String> parseSet(String str) {
        if (str == null) {
            return null;
        }
        HashSet<String> hashSet = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < str.length()) {
            char charAt = str.charAt(i);
            if (charAt == '\\') {
                i++;
                charAt = str.charAt(i);
            } else if (charAt == ';') {
                hashSet.add(sb.toString());
                sb.setLength(0);
                i++;
            }
            sb.append(charAt);
            i++;
        }
        if (sb.length() != 0) {
            hashSet.add(sb.toString());
        }
        return hashSet;
    }

    public static int parseType(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof String) {
            return 1;
        }
        if (obj instanceof Set) {
            return 2;
        }
        if (obj instanceof Integer) {
            return 3;
        }
        if (obj instanceof Long) {
            return 4;
        }
        if (obj instanceof Float) {
            return 5;
        }
        if (obj instanceof Boolean) {
            return 6;
        }
        throw new AssertionError("Unknown preference type: " + obj.getClass());
    }

    public static Object castObject(Object obj) {
        return obj instanceof Boolean ? castInt((Boolean) obj) : obj instanceof Set ? castString(castSet(obj)) : obj;
    }

    private static Boolean castBool(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        return (Integer) obj != 0;
    }
}