package com.song.xposed.hook;

/* renamed from: android.support.v7.app.יˑʽ  reason: contains not printable characters */
public class C1736 {
    /* renamed from: ˉ  reason: contains not printable characters */
    static boolean m7915(CharSequence charSequence, boolean z, int i, CharSequence charSequence2, int i2, int i3) {
        if (!(charSequence instanceof String) || !(charSequence2 instanceof String)) {
            int length = charSequence.length() - i;
            int length2 = charSequence2.length() - i2;
            if (i < 0 || i2 < 0 || i3 < 0 || length < i3 || length2 < i3) {
                return false;
            }
            while (true) {
                int i4 = i3 - 1;
                if (i3 <= 0) {
                    return true;
                }
                int i5 = i + 1;
                char charAt = charSequence.charAt(i);
                int i6 = i2 + 1;
                char charAt2 = charSequence2.charAt(i2);
                if (charAt != charAt2) {
                    if (!z) {
                        return false;
                    }
                    if (!(Character.toUpperCase(charAt) == Character.toUpperCase(charAt2) || Character.toLowerCase(charAt) == Character.toLowerCase(charAt2))) {
                        return false;
                    }
                }
                i = i5;
                i3 = i4;
                i2 = i6;
            }
        } else {
            return ((String) charSequence).regionMatches(z, i, (String) charSequence2, i2, i3);
        }
    }
}