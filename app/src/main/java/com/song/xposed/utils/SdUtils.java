package com.song.xposed.utils;

import android.os.Environment;

/**
 * Created by chensongsong on 2020/10/15.
 */
public final class SdUtils {

    public static boolean isMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    public static String getDirPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

}
