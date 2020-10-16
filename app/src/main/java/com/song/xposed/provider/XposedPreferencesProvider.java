package com.song.xposed.provider;

import com.song.xposed.utils.Constants;

public final class XposedPreferencesProvider extends PreferencesProvider {
    public XposedPreferencesProvider() {
        super(Constants.AUTHORITIES, new String[]{Constants.PREFName});
    }
}