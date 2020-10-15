package com.song.xposed.provider;

import java.util.HashSet;
import java.util.Set;

/* renamed from: android.support.v7.app.ˊٴ  reason: contains not printable characters */
class C1188 {
    /* renamed from: ˉ  reason: contains not printable characters */
    private static Integer m5473(Boolean bool) {
        if (bool == null) {
            return null;
        }
        return Integer.valueOf(bool.booleanValue() ? 1 : 0);
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    public static Object m5474(Object obj, int i) {
        if (i != 0) {
            switch (i) {
                case 1:
                    return (String) obj;
                case 2:
                    return m5477((String) obj);
                case 3:
                    return (Integer) obj;
                case 4:
                    return (Long) obj;
                case 5:
                    return (Float) obj;
                case 6:
                    try {
                        return m5480(obj);
                    } catch (ClassCastException e) {
                        throw new IllegalArgumentException("Expected type " + i + ", got " + obj.getClass(), e);
                    }
                default:
                    throw new IllegalArgumentException("Unknown type: " + i);
            }
        } else if (obj == null) {
            return null;
        } else {
            throw new IllegalArgumentException("Expected null, got non-null value");
        }
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    public static String m5475(Set<String> set) {
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

    /* renamed from: ˉ  reason: contains not printable characters */
    public static Set<String> m5476(Object obj) {
        return (Set) obj;
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    public static Set<String> m5477(String str) {
        if (str == null) {
            return null;
        }
        HashSet hashSet = new HashSet();
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

    /* renamed from: ˏ  reason: contains not printable characters */
    public static int m5478(Object obj) {
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

    /* renamed from: ˑ  reason: contains not printable characters */
    public static Object m5479(Object obj) {
        return obj instanceof Boolean ? m5473((Boolean) obj) : obj instanceof Set ? m5475(m5476(obj)) : obj;
    }

    /* renamed from: ᵢ  reason: contains not printable characters */
    private static Boolean m5480(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Boolean) {
            return (Boolean) obj;
        }
        return Boolean.valueOf(((Integer) obj).intValue() != 0);
    }
}