package com.song.xposed.hook.others;

import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Method;
import java.util.Map;

import javax.crypto.Cipher;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * 极光一键登录 hook 代码
 * Created by chensongsong on 2019/11/5.
 */
public class JiGuangHook {

    private static final String TAG = "JiGuangHook";
    //    private static final String PACKAGE_NAME = "com.github.gavin.smid";
    private static final String PACKAGE_NAME = "com.youjuyouqu.testjg";

    public JiGuangHook(XC_LoadPackage.LoadPackageParam lpparam) {

        if (PACKAGE_NAME.equals(lpparam.packageName)) {
            encryptHook(lpparam);
            hashHook(lpparam);
            systemSettingHook(lpparam);
            fileHook(lpparam);
            jsonHook(lpparam);

            
        }

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
                String data = new String(content, "utf-8");
                Log.e(TAG, "Cipher.doFinal param: " + data);
//                String data_base64 = Base64.encode(content);
//                Log.e(TAG, "Cipher.doFinal param base64: " + data_base64);
                super.beforeHookedMethod(param);
            }

        });

        XposedHelpers.findAndHookMethod("javax.crypto.Cipher", lpparam.classLoader, "doFinal", byte[].class, int.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                byte[] content = (byte[]) param.args[0];
                String data = new String(content, "utf-8");
                Log.e(TAG, "Cipher.doFinal param: " + data);
//                String data_base64 = Base64.encode(content,Base64.CRLF);
//                Log.e(TAG, "Cipher.doFinal param base64: " + data_base64);

//                String smid = "1D1CDC:D27911:BF151Bfeq#%dc87#989(^)78909-=89+1vfiqocxq58*@#~09$";
//                if (smid.equals(data)) {
//                    param.args[0] = "1D1CDC:D27912:BF151Bfeq#%dc87#989(^)78909-=89+1vfiqocxq58*@#~09$".getBytes();
//                }

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

    /**
     * 系统设置
     *
     * @param lpparam
     */
    private void systemSettingHook(XC_LoadPackage.LoadPackageParam lpparam) {

        XposedHelpers.findAndHookMethod("android.content.ContextWrapper", lpparam.classLoader, "getContentResolver", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.e(TAG, "Context.getContentResolver is invoked.");
                super.beforeHookedMethod(param);
            }
        });

        try {

            Class secureCls = XposedHelpers.findClass(Settings.Secure.class.getName(), null);
            Method secureGetstring = XposedHelpers.findMethodExact(secureCls, "getString", ContentResolver.class, String.class);
            secureGetstring.setAccessible(true);
            XposedBridge.hookMethod(secureGetstring, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    String content = (String) param.args[1];
                    Log.e(TAG, "Settings.Secure.getString param: " + content);
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    String result = (String) param.getResult();
                    Log.e(TAG, "Settings.Secure.getString result: " + result);
                    super.afterHookedMethod(param);
                }
            });

            Class systemCls = XposedHelpers.findClass(Settings.System.class.getName(), null);
            Method systemGetString = XposedHelpers.findMethodExact(systemCls, "getString", ContentResolver.class, String.class);
            XposedBridge.hookMethod(systemGetString, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    String content = (String) param.args[1];
                    Log.e(TAG, "Settings.System.getString param: " + content);
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    String result = (String) param.getResult();
                    Log.e(TAG, "Settings.System.getString result: " + result);
                    super.afterHookedMethod(param);
                }

            });

            Method systemPutString = XposedHelpers.findMethodExact(systemCls, "putString", ContentResolver.class, String.class, String.class);
            XposedBridge.hookMethod(systemPutString, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                    String p1 = (String) param.args[1];
                    String p2 = (String) param.args[2];
                    Log.e(TAG, "Settings.System.putString param: " + p1 + "#" + p2);
                    super.beforeHookedMethod(param);
                }

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    Object result = param.getResult();
                    Log.e(TAG, "Settings.System.putString result: " + result);
                    super.afterHookedMethod(param);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void fileHook(XC_LoadPackage.LoadPackageParam lpparam) {

        XposedHelpers.findAndHookMethod("java.io.File", lpparam.classLoader, "getName", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String result = (String) param.getResult();
                Log.e(TAG, "File.getName result: " + result);
                super.afterHookedMethod(param);
            }
        });

        XposedHelpers.findAndHookConstructor("java.io.File", lpparam.classLoader, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String arg = (String) param.args[0];
                Log.e(TAG, "fileHook new File param: " + arg);
                super.beforeHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookConstructor("java.io.File", lpparam.classLoader, String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String arg = (String) param.args[0];
                int arg1 = (int) param.args[1];
                Log.e(TAG, "fileHook new File param: " + arg + "#" + arg1);
                super.beforeHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookConstructor("java.io.File", lpparam.classLoader, String.class, File.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String arg = (String) param.args[0];
                File arg1 = (File) param.args[1];
                Log.e(TAG, "fileHook new File param: " + arg + "#" + arg1.getAbsolutePath());
                super.beforeHookedMethod(param);
            }
        });
        XposedHelpers.findAndHookConstructor("java.io.File", lpparam.classLoader, File.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                File arg = (File) param.args[0];
                String arg1 = (String) param.args[1];
                Log.e(TAG, "fileHook new File param: " + arg.getAbsolutePath() + "#" + arg1);
                super.beforeHookedMethod(param);


            }
        });
        XposedHelpers.findAndHookConstructor("java.io.File", lpparam.classLoader, String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                String arg = (String) param.args[0];
                String arg1 = (String) param.args[1];
                Log.e(TAG, "fileHook new File param: " + arg + "#" + arg1);
                super.beforeHookedMethod(param);
            }
        });
    }

    private void jsonHook(XC_LoadPackage.LoadPackageParam lpparam) {

        XposedHelpers.findAndHookMethod("org.json.JSONObject", lpparam.classLoader, "toString", new XC_MethodHook() {

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                String result = (String) param.getResult();
                printLongString(result, "JSONObject.toString result: ");

                // 篡改第二次请求数据
                // mac,imei,androidid
                if (result.startsWith("{\"acc\":{")) {

                    String temp = result;

                    // 篡改 mac
                    temp = temp.replace("d4bbc8527b21", "d4cbc8727c32");
                    temp = temp.replace("ec:c2:fd:06:75:fb", "df:c3:fd:06:75:42");
                    // 篡改 imei
                    temp = temp.replace("867478046879276", "867578089879244");
                    temp = temp.replace("85601415363864770017", "85301445363864770038");
                    temp = temp.replace("018767445483615", "018705445489654");
                    // 篡改 android_id
                    temp = temp.replace("13dcab62cd8523a1", "13dcab72cd9523b3");

//                    temp = temp.replace("988751ca8e09f518984ff7b0bc98753a", "988751ca8e09f518984ff7b0bc98753b");
                    temp = temp.replace("201909111614071a82756ef7b6821065b4c98b2f6987c2015665e83b088004", "20190911181619e3f1be5b11451df0214d4ef1ee5a1d3b01f1801e392bea38");
//                    temp = temp.replace("5907320832", "5907320858");
//                    temp = temp.replace("31257739264", "31257739268");

                    temp = temp.replace("\\/data\\/user\\/0\\/io.va.exposed\\/virtual\\/data\\/user\\/0\\/com.song.testcrackdeviceid\\/files", "/data/user/0/com.song.testcrackdeviceid/files");

                    param.setResult(temp);
                }

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
//        XposedHelpers.findAndHookMethod("org.json.JSONObject", lpparam.classLoader, "put", String.class, Object.class, new XC_MethodHook() {
//
//            @Override
//            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                JSONObject jsonObject = (JSONObject) param.getResult();
//                Log.e(TAG, "JSONObject.put result: " + jsonObject.toString());
//                super.afterHookedMethod(param);
//            }
//        });

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
                String s = data.substring(n, PRINT_SIZE);
                Log.e(TAG, tag + s);
                n += PRINT_SIZE;
            }
            Log.e(TAG, tag + data.substring(n));
        } else {
            Log.e(TAG, tag + data);
        }

    }


}
