package com.song.xposed.hook;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.song.xposed.preferences.ProviderUtils;
import com.song.xposed.utils.Constants;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by chensongsong on 2020/10/15.
 */
public class DeviceMethodHook extends XC_MethodHook {

    public final HashMap<String, String> hashMap = new HashMap<>();
    private XC_LoadPackage.LoadPackageParam packageParam;

    public DeviceMethodHook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        this.packageParam = loadPackageParam;
    }

    @Override
    public void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
        super.afterHookedMethod(param);
        String value = new ProviderUtils((Context) param.thisObject, Constants.AUTHORITIES, Constants.PREFName)
                .getString(this.packageParam.packageName, null);
        if (!TextUtils.isEmpty(value)) {
            XposedBridge.log("ContentProvider: " + value);
            JSONObject jsonObject = new JSONObject(value);
            hookNormal(jsonObject);
            hookDevice(jsonObject);
            hookWifi(jsonObject);
            hookOther(jsonObject);
        }
    }

    private void hookNormal(JSONObject jSONObject) {
        if (jSONObject.has("android.os.Build.ro.product.manufacturer") && !jSONObject.optString("android.os.Build.ro.product.manufacturer").isEmpty()) {
            XposedFieldUtils.setStaticObjectField(Build.class, "MANUFACTURER", jSONObject.optString("android.os.Build.ro.product.manufacturer"));
            XposedFieldUtils.setStaticObjectField(Build.class, "PRODUCT", jSONObject.optString("android.os.Build.ro.product.manufacturer"));
            XposedFieldUtils.setStaticObjectField(Build.class, "BRAND", jSONObject.optString("android.os.Build.ro.product.manufacturer"));
            this.hashMap.put("ro.product.manufacturer", jSONObject.optString("android.os.Build.ro.product.manufacturer"));
            this.hashMap.put("ro.product.brand", jSONObject.optString("android.os.Build.ro.product.manufacturer"));
            this.hashMap.put("ro.product.name", jSONObject.optString("android.os.Build.ro.product.manufacturer"));
        }
        if (jSONObject.has("android.os.Build.ro.product.model") && !jSONObject.optString("android.os.Build.ro.product.model").isEmpty()) {
            XposedFieldUtils.setStaticObjectField(Build.class, "MODEL", jSONObject.optString("android.os.Build.ro.product.model"));
            XposedFieldUtils.setStaticObjectField(Build.class, "DEVICE", jSONObject.optString("android.os.Build.ro.product.model"));
            this.hashMap.put("ro.product.device", jSONObject.optString("android.os.Build.ro.product.model"));
            this.hashMap.put("ro.product.model", jSONObject.optString("android.os.Build.ro.product.model"));
        }
        if (jSONObject.has("android.os.Build.ro.serialno") && !jSONObject.optString("android.os.Build.ro.serialno").isEmpty()) {
            XposedFieldUtils.setStaticObjectField(Build.class, "SERIAL", jSONObject.optString("android.os.Build.ro.serialno"));
            this.hashMap.put("ro.serialno", jSONObject.optString("android.os.Build.ro.serialno"));
        }
        if (jSONObject.has("android.os.SystemProperties.android_id") && !jSONObject.optString("android.os.SystemProperties.android_id").isEmpty()) {
            this.hashMap.put("android_id", jSONObject.optString("android.os.SystemProperties.android_id"));
        }
        XC_MethodHook methodHook = new XC_MethodHook() {

            @Override
            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
//                Log.d("XposedEntry", String.format("%s#%s(%s) => %s", methodHookParam.thisObject.getClass().getName(), methodHookParam.method.getName(), Arrays.toString(methodHookParam.args), methodHookParam.getResult()));
                Log.d("XposedEntry", String.format("%s(%s) => %s", methodHookParam.method.getName(), Arrays.toString(methodHookParam.args), methodHookParam.getResult()));
                if (methodHookParam.args.length > 1 && methodHookParam.args[1] != null && DeviceMethodHook.this.hashMap.containsKey(methodHookParam.args[1].toString())) {
                    methodHookParam.setResult(DeviceMethodHook.this.hashMap.get(methodHookParam.args[1].toString()));
                }
            }
        };
        XC_MethodHook methodHook1 = new XC_MethodHook() {

            @Override
            public void afterHookedMethod(XC_MethodHook.MethodHookParam param) {
//                XposedBridge.log("XposedEntry: " + String.format("%s#%s(%s) => %s", param.thisObject.getClass().getName(), param.method.getName(), Arrays.toString(param.args), param.getResult()));
                XposedBridge.log("XposedEntry: " + String.format("%s(%s) => %s", param.method.getName(), Arrays.toString(param.args), param.getResult()));
                if (param.args.length > 0 && param.args[0] != null && DeviceMethodHook.this.hashMap.containsKey(param.args[0].toString())) {
                    param.setResult(DeviceMethodHook.this.hashMap.get(param.args[0].toString()));
                }
            }
        };
        XposedBridgeUtils.hookAllMethods(Settings.System.class, "getString", methodHook);
        XposedBridgeUtils.hookAllMethods(Settings.Secure.class, "getString", methodHook);
        XposedBridgeUtils.hookAllMethods(this.packageParam.classLoader, "android.os.SystemProperties", "get", methodHook1);
    }

    private void hookDevice(JSONObject jSONObject) {
        Class<NetworkInfo> networkInfoClass;
        String methodName;
        ValueMethodHook valueMethodHook;
        if (jSONObject.has("android.telephony.TelephonyManager.getLine1Number") && !jSONObject.optString("android.telephony.TelephonyManager.getLine1Number").isEmpty()) {
            XposedBridgeUtils.hookAllMethods(TelephonyManager.class, "getLine1Number", new ValueMethodHook(jSONObject.optString("android.telephony.TelephonyManager.getLine1Number")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getDeviceId") && !jSONObject.optString("android.telephony.TelephonyManager.getDeviceId").isEmpty()) {
            XposedBridgeUtils.hookAllMethods(TelephonyManager.class, "getDeviceId", new ValueMethodHook(jSONObject.optString("android.telephony.TelephonyManager.getDeviceId")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getSubscriberId") && !jSONObject.optString("android.telephony.TelephonyManager.getSubscriberId").isEmpty()) {
            XposedBridgeUtils.hookAllMethods(TelephonyManager.class, "hasIccCard", new ValueMethodHook(true));
            XposedBridgeUtils.hookAllMethods(TelephonyManager.class, "getSubscriberId", new ValueMethodHook(jSONObject.optString("android.telephony.TelephonyManager.getSubscriberId")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getSimOperator") && !jSONObject.optString("android.telephony.TelephonyManager.getSimOperator").isEmpty()) {
            XposedBridgeUtils.hookAllMethods(TelephonyManager.class, "getSimOperator", new ValueMethodHook(jSONObject.optString("android.telephony.TelephonyManager.getSimOperator")));
            XposedBridgeUtils.hookAllMethods(TelephonyManager.class, "getNetworkOperator", new ValueMethodHook(jSONObject.optString("android.telephony.TelephonyManager.getSimOperator")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getSimCountryIso") && !jSONObject.optString("android.telephony.TelephonyManager.getSimCountryIso").isEmpty()) {
            XposedBridgeUtils.hookAllMethods(TelephonyManager.class, "getSimCountryIso", new ValueMethodHook(jSONObject.optString("android.telephony.TelephonyManager.getSimCountryIso")));
            XposedBridgeUtils.hookAllMethods(TelephonyManager.class, "getNetworkCountryIso", new ValueMethodHook(jSONObject.optString("android.telephony.TelephonyManager.getSimCountryIso")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getSimOperatorName") && !jSONObject.optString("android.telephony.TelephonyManager.getSimOperatorName").isEmpty()) {
            XposedBridgeUtils.hookAllMethods(TelephonyManager.class, "getSimOperatorName", new ValueMethodHook(jSONObject.optString("android.telephony.TelephonyManager.getSimOperatorName")));
            XposedBridgeUtils.hookAllMethods(TelephonyManager.class, "getNetworkOperatorName", new ValueMethodHook(jSONObject.optString("android.telephony.TelephonyManager.getSimOperatorName")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getSimSerialNumber") && !jSONObject.optString("android.telephony.TelephonyManager.getSimSerialNumber").isEmpty()) {
            XposedBridgeUtils.hookAllMethods(TelephonyManager.class, "getSimSerialNumber", new ValueMethodHook(jSONObject.optString("android.telephony.TelephonyManager.getSimSerialNumber")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getSimState") && !jSONObject.optString("android.telephony.TelephonyManager.getSimState").isEmpty()) {
            XposedBridgeUtils.hookAllMethods(TelephonyManager.class, "getSimState", new ValueMethodHook(jSONObject.optInt("android.telephony.TelephonyManager.getSimState")));
        }
        if (jSONObject.has("android.net.NetworkInfo.getType") && !jSONObject.optString("android.net.NetworkInfo.getType").isEmpty()) {
            if (jSONObject.optString("android.net.NetworkInfo.getType").equalsIgnoreCase("wifi")) {
                XposedBridgeUtils.hookAllMethods(NetworkInfo.class, "getType", new ValueMethodHook(1));
                networkInfoClass = NetworkInfo.class;
                methodName = "getTypeName";
                valueMethodHook = new ValueMethodHook("WIFI");
            } else {
                networkInfoClass = NetworkInfo.class;
                methodName = "getTypeName";
                valueMethodHook = new ValueMethodHook("MOBILE");
            }
            XposedBridgeUtils.hookAllMethods(networkInfoClass, methodName, valueMethodHook);
        }
    }

    private void hookWifi(JSONObject jSONObject) {
        if (jSONObject.has("android.net.wifi.WifiInfo.getSSID") && !jSONObject.optString("android.net.wifi.WifiInfo.getSSID").isEmpty()) {
            XposedBridgeUtils.hookAllMethods(WifiInfo.class, "getSSID", new ValueMethodHook(jSONObject.optString("android.net.wifi.WifiInfo.getSSID")));
        }
        if (jSONObject.has("android.net.wifi.WifiInfo.getBSSID") && !jSONObject.optString("android.net.wifi.WifiInfo.getBSSID").isEmpty()) {
            XposedBridgeUtils.hookAllMethods(WifiInfo.class, "getBSSID", new ValueMethodHook(jSONObject.optString("android.net.wifi.WifiInfo.getBSSID")));
        }
        if (jSONObject.has("android.net.wifi.WifiInfo.getMacAddress") && !jSONObject.optString("android.net.wifi.WifiInfo.getMacAddress").isEmpty()) {
            XposedBridgeUtils.hookAllMethods(WifiInfo.class, "getMacAddress", new ValueMethodHook(jSONObject.optString("android.net.wifi.WifiInfo.getMacAddress")));
        }
    }

    private void hookOther(final JSONObject jSONObject) {
        if (jSONObject.has("android.content.res.language") && !jSONObject.optString("android.content.res.language").isEmpty()) {
            final Locale locale = AllUtils.getLocale(jSONObject.optString("android.content.res.language"));
            XposedBridgeUtils.hookAllMethods(Resources.class, "updateConfiguration", new XC_MethodHook() {

                @Override
                public void beforeHookedMethod(XC_MethodHook.MethodHookParam param) {
                    if (param.args.length > 0 && param.args[0] != null) {
                        Configuration configuration = (Configuration) param.args[0];
                        Locale.setDefault(locale);
                        configuration.setLocale(locale);
                        configuration.locale = locale;
                        param.args[0] = configuration;
                    }
                }
            });
        }
        if (jSONObject.has("android.content.res.display.dpi") && jSONObject.optInt("android.content.res.display.dpi") > 0) {
            final int valueOf = jSONObject.optInt("android.content.res.display.dpi");
            XposedBridgeUtils.hookAllMethods(Resources.class, "updateConfiguration", new XC_MethodHook() {

                @Override
                public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                    if (methodHookParam.args.length > 1 && methodHookParam.args[1] != null) {
                        Configuration configuration = (Configuration) methodHookParam.args[0];
                        DisplayMetrics displayMetrics = (DisplayMetrics) methodHookParam.args[1];
                        displayMetrics.density = ((float) valueOf) / 160.0f;
                        displayMetrics.densityDpi = valueOf;
                        XposedFieldUtils.setIntField(configuration, "densityDpi", valueOf);
                    }
                }
            });
        }
        if (jSONObject.has("android.web.head.user.agent") && !jSONObject.optString("android.web.head.user.agent").isEmpty()) {
            Log.d("XposedEntry", "hookOtherInfo: android.web.head.user.agent: " + jSONObject.optString("android.web.head.user.agent"));
            XposedBridgeUtils.hookAllMethods(System.class, "getProperty", new XC_MethodHook() {

                @Override
                public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                    if (methodHookParam.args[0].toString().equalsIgnoreCase("http.agent")) {
                        Log.d("XposedEntry", "afterHookedMethod: ==== System BEFORE ====" + methodHookParam.getResult());
                        methodHookParam.setResult(jSONObject.optString("android.web.head.user.agent"));
                        Log.d("XposedEntry", "afterHookedMethod: ==== System AFTER  ====" + methodHookParam.getResult());
                    }
                }
            });
            XposedBridgeUtils.hookAllMethods(WebSettings.class, "getDefaultUserAgent", new ValueMethodHook(jSONObject.optString("android.web.head.user.agent")));
            XposedBridgeUtils.hookAllMethods(WebView.class, "getSettings", new XC_MethodHook() {

                @Override
                public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                    XposedBridgeUtils.hookAllMethods(((WebSettings) methodHookParam.getResult()).getClass(), "getUserAgentString", new ValueMethodHook(jSONObject.optString("android.web.head.user.agent")));
                }
            });
        }
    }

}
