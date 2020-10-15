package com.song.xposed.provider;

final class PrefName {

    private final String prefName;
    private final boolean match;

    private PrefName(String prefName) {
        this(prefName, false);
    }

    private PrefName(String str, boolean match) {
        this.prefName = str;
        this.match = match;
    }

    static PrefName[] parse(String[] prefNames) {
        PrefName[] prefNameArray = new PrefName[prefNames.length];
        for (int i = 0; i < prefNames.length; i++) {
            prefNameArray[i] = new PrefName(prefNames[i]);
        }
        return prefNameArray;
    }

    String getPrefName() {
        return this.prefName;
    }

    boolean isMatch() {
        return this.match;
    }
}