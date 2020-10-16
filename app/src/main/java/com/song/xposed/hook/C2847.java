package com.song.xposed.hook;

import de.robv.android.xposed.XC_MethodHook;

public class C2847 extends XC_MethodHook {

    private Object f8446 = null;

    public C2847(Object obj) {
        this.f8446 = obj;
    }

    @Override
    public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
        C2847.super.beforeHookedMethod(methodHookParam);
        if (this.f8446 instanceof Throwable) {
            methodHookParam.setThrowable((Throwable) this.f8446);
        } else {
            methodHookParam.setResult(this.f8446);
        }
    }
}