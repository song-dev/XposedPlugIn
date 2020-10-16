package com.song.xposed.hook;

/* renamed from: android.support.v7.app.ʾˆˏ  reason: contains not printable characters */
public class C0618 {
    /* renamed from: ˉ  reason: contains not printable characters */
    public static String m3373(String str, String str2, String str3) {
        return m3374(str, str2, str3, -1);
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    public static String m3374(String str, String str2, String str3, int i) {
        return m3375(str, str2, str3, i, false);
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    private static String m3375(String str, String str2, String str3, int i, boolean z) {
        String str4;
        if (m3376(str) || m3376(str2) || str3 == null || i == 0) {
            return str;
        }
        if (z) {
            str4 = str.toLowerCase();
            str2 = str2.toLowerCase();
        } else {
            str4 = str;
        }
        int i2 = 0;
        int indexOf = str4.indexOf(str2, 0);
        if (indexOf == -1) {
            return str;
        }
        int length = str2.length();
        int length2 = str3.length() - length;
        if (length2 < 0) {
            length2 = 0;
        }
        int i3 = 64;
        if (i < 0) {
            i3 = 16;
        } else if (i <= 64) {
            i3 = i;
        }
        StringBuilder sb = new StringBuilder(str.length() + (length2 * i3));
        while (indexOf != -1) {
            sb.append(str, i2, indexOf);
            sb.append(str3);
            i2 = indexOf + length;
            i--;
            if (i == 0) {
                break;
            }
            indexOf = str4.indexOf(str2, i2);
        }
        sb.append(str, i2, str.length());
        return sb.toString();
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    public static boolean m3376(CharSequence charSequence) {
        return charSequence == null || charSequence.length() == 0;
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    public static boolean m3377(CharSequence charSequence, CharSequence charSequence2) {
        if (charSequence == charSequence2) {
            return true;
        }
        if (charSequence == null || charSequence2 == null || charSequence.length() != charSequence2.length()) {
            return false;
        }
        if ((charSequence instanceof String) && (charSequence2 instanceof String)) {
            return charSequence.equals(charSequence2);
        }
        return C1736.m7915(charSequence, false, 0, charSequence2, 0, charSequence.length());
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    public static boolean m3378(CharSequence charSequence, char... cArr) {
        if (!m3376(charSequence) && !C1346.m6440(cArr)) {
            int length = charSequence.length();
            int length2 = cArr.length;
            int i = length - 1;
            int i2 = length2 - 1;
            for (int i3 = 0; i3 < length; i3++) {
                char charAt = charSequence.charAt(i3);
                for (int i4 = 0; i4 < length2; i4++) {
                    if (cArr[i4] == charAt) {
                        if (!Character.isHighSurrogate(charAt) || i4 == i2) {
                            return true;
                        }
                        if (i3 < i && cArr[i4 + 1] == charSequence.charAt(i3 + 1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /* renamed from: ˏ  reason: contains not printable characters */
    public static boolean m3379(CharSequence charSequence) {
        if (m3376(charSequence)) {
            return false;
        }
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isDigit(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /* renamed from: ˏ  reason: contains not printable characters */
    public static boolean m3380(CharSequence charSequence, char... cArr) {
        if (!(charSequence == null || cArr == null)) {
            int length = charSequence.length();
            int i = length - 1;
            int length2 = cArr.length;
            int i2 = length2 - 1;
            for (int i3 = 0; i3 < length; i3++) {
                char charAt = charSequence.charAt(i3);
                for (int i4 = 0; i4 < length2; i4++) {
                    if (cArr[i4] == charAt) {
                        if (!Character.isHighSurrogate(charAt) || i4 == i2) {
                            return false;
                        }
                        if (i3 < i && cArr[i4 + 1] == charSequence.charAt(i3 + 1)) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /* renamed from: ˑ  reason: contains not printable characters */
    public static boolean m3381(CharSequence charSequence) {
        if (charSequence == null || m3376(charSequence)) {
            return false;
        }
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isLowerCase(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /* renamed from: ᵢ  reason: contains not printable characters */
    public static boolean m3382(CharSequence charSequence) {
        if (charSequence == null || m3376(charSequence)) {
            return false;
        }
        int length = charSequence.length();
        for (int i = 0; i < length; i++) {
            if (!Character.isUpperCase(charSequence.charAt(i))) {
                return false;
            }
        }
        return true;
    }
}