package com.song.xposed.provider;

public class C2640 {

    private final String f7840;
    private final boolean f7841;

    public C2640(String str) {
        this(str, false);
    }

    public C2640(String str, boolean z) {
        this.f7840 = str;
        this.f7841 = z;
    }

    public static C2640[] m12073(String[] strArr) {
        C2640[] r0 = new C2640[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            r0[i] = new C2640(strArr[i]);
        }
        return r0;
    }

    public String m12074() {
        return this.f7840;
    }

    public boolean m12075() {
        return this.f7841;
    }
}