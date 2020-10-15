package com.song.xposed.provider;

public final class XposedPreferencesProvider extends PreferencesProvider {
    public XposedPreferencesProvider() {
        super("com.song.xposed.provider.XposedPreferencesProvider", new String[]{"xposed"});
    }
}