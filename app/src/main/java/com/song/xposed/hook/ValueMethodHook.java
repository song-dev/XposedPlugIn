package com.song.xposed.hook;

import de.robv.android.xposed.XC_MethodHook;

public class ValueMethodHook extends XC_MethodHook {

    private Object value = null;

    public ValueMethodHook(Object value) {
        this.value = value;
    }

    @Override
    public void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
        super.beforeHookedMethod(param);
        if (this.value instanceof Throwable) {
            param.setThrowable((Throwable) this.value);
        } else {
            param.setResult(this.value);
        }
    }
}