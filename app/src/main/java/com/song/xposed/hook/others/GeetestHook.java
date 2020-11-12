package com.song.xposed.hook.others;

import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 极光一键登录 hook 代码
 * Created by chensongsong on 2019/11/5.
 */
public class GeetestHook {

    private static final String TAG = "GeetestHook";
    private static final String PACKAGE_NAME = "com.example.geeguarddemo";
    private static final String PACKAGE_NAME_V2 = "com.geetest.deepknow.demo";
    private static final String PACKAGE_NAME_SE = "com.example.geetestthr";
    private static final String PACKAGE_NAME_SE_ZXYH = "com.geetest.sensebot.demo";
    private static final String PACKAGE_NAME_SE_ZXYH_2 = "com.geetest.sensebot.demo2";

    public GeetestHook(XC_LoadPackage.LoadPackageParam lpparam) {

        if (PACKAGE_NAME.equals(lpparam.packageName)
                || PACKAGE_NAME_V2.equals(lpparam.packageName)
                || PACKAGE_NAME_SE.equals(lpparam.packageName)
                || PACKAGE_NAME_SE_ZXYH.equals(lpparam.packageName)
                || PACKAGE_NAME_SE_ZXYH_2.equals(lpparam.packageName)) {
            Log.i(TAG, "PackageName: " + lpparam.packageName);
            encryptHook(lpparam);
            jsonHook(lpparam);
        }
    }

    private void jsonHook(XC_LoadPackage.LoadPackageParam lpparam) {

        XposedHelpers.findAndHookMethod("org.json.JSONObject", lpparam.classLoader, "toString", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String result = (String) param.getResult();
                printLongString(result, "JSONObject.toString result: ");
                super.afterHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookMethod("org.json.JSONObject", lpparam.classLoader, "toString", int.class, new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String result = (String) param.getResult();
//                Log.e(TAG, "JSONObject.toString result: " + result);
                printLongString(result, "JSONObject.toString result: ");
                super.afterHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookConstructor("org.json.JSONObject", lpparam.classLoader, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String result = (String) param.args[0];
//                Log.e(TAG, "jsonHook new JSONObject param: " + result);
                printLongString(result, "jsonHook new JSONObject param: ");
                super.beforeHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookConstructor("org.json.JSONObject", lpparam.classLoader, Map.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Map arg = (Map) param.args[0];
                Log.e(TAG, "jsonHook new JSONObject param: " + arg.toString());
                printLongString(arg.toString(), "jsonHook new JSONObject param: ");
                super.beforeHookedMethod(param);
            }
        });

    }

    /**
     * 加密 Hook
     *
     * @param lpparam
     */
    private void encryptHook(XC_LoadPackage.LoadPackageParam lpparam) {

        Class clazz = XposedHelpers.findClass(Cipher.class.getName(), null);
        Method m = XposedHelpers.findMethodExact(clazz, "getInstance", String.class);
        m.setAccessible(true);
        XposedBridge.hookMethod(m, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String content = (String) param.args[0];
                Log.e(TAG, "Cipher.getInstance param: " + content);
                super.beforeHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookMethod("javax.crypto.Cipher", lpparam.classLoader, "doFinal", byte[].class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                byte[] content = (byte[]) param.args[0];
                String unzip = unzip(content);
                if (unzip == null) {
                    String data = new String(content, "utf-8");
                    printLongString(data, "Cipher.doFinal param origin: ");
                } else {
                    printLongString(unzip, "Cipher.doFinal param zip: ");
                }
                super.beforeHookedMethod(param);
            }

        });

        XposedHelpers.findAndHookMethod("javax.crypto.Cipher", lpparam.classLoader, "doFinal", byte[].class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                byte[] content = (byte[]) param.args[0];
                String data = new String(content, "utf-8");
                Log.e(TAG, "Cipher.doFinal param third: " + data);
                super.beforeHookedMethod(param);
            }

        });

    }

    /**
     * hook hash 函数
     *
     * @param lpparam
     */
    private void hashHook(XC_LoadPackage.LoadPackageParam lpparam) {

        XposedHelpers.findAndHookMethod("java.security.MessageDigest", lpparam.classLoader, "getInstance", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                String content = (String) param.args[0];
                Log.e(TAG, "MessageDigest.getInstance(String algorithm) param: " + content);
                super.beforeHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookMethod("java.security.MessageDigest", lpparam.classLoader, "update", byte[].class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                byte[] content = (byte[]) param.args[0];
                Log.e(TAG, "MessageDigest.update(byte[] input, int offset, int len) param: " + new String(content, "utf-8"));
                super.beforeHookedMethod(param);
            }
        });

    }

    private final static int PRINT_SIZE = 3800;

    /**
     * 打印超长字符串
     *
     * @param data
     */
    private void printLongString(String data, String tag) {
        int len = data.length();
        if (len > PRINT_SIZE) {
            int n = 0;
            while ((len - n) > PRINT_SIZE) {
                String s = data.substring(n, n + PRINT_SIZE);
                Log.e(TAG, tag + s);
                n += PRINT_SIZE;
            }
            Log.e(TAG, tag + data.substring(n));
        } else {
            Log.e(TAG, tag + data);
        }
    }

    private boolean isZIP(byte[] arr) {
        return arr.length >= 2 && arr[0] == 31 && arr[1] == 139;
    }

    private String unzip(byte[] content) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ByteArrayInputStream bais = new ByteArrayInputStream(content);
            GZIPInputStream unGzip;

            unGzip = new GZIPInputStream(bais);
            byte[] buffer = new byte[512];
            int n;
            while ((n = unGzip.read(buffer)) >= 0) {
                baos.write(buffer, 0, n);
            }

            byte[] array = baos.toByteArray();
            return new String(array, "utf-8");
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return null;
    }

}
