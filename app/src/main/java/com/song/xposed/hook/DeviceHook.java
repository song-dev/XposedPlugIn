package com.song.xposed.hook;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.song.xposed.preferences.ProviderUtils;
import com.song.xposed.utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by chensongsong on 2020/10/15.
 */
public class DeviceHook extends XC_MethodHook {



    public final HashMap<String, String> f7970 = new HashMap<>();
    private XC_LoadPackage.LoadPackageParam packageParam;

    public DeviceHook(XC_LoadPackage.LoadPackageParam loadPackageParam) {
        this.packageParam = loadPackageParam;
    }

    @Override
    public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) throws Throwable {
        super.afterHookedMethod(methodHookParam);
        String value = new ProviderUtils((Context) methodHookParam.thisObject, Constants.AUTHORITIES, Constants.PREFName)
                .getString(this.packageParam.packageName, null);
        if (value != null && value.length() > 0) {
            try {
                JSONObject jsonObject = new JSONObject(value);
                hookNormal(jsonObject);
                hookDevice(jsonObject);
                hookWifi(jsonObject);
                hookOther(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void hookNormal(JSONObject jSONObject) {
        if (jSONObject.has("android.os.Build.ro.product.manufacturer") && !jSONObject.optString("android.os.Build.ro.product.manufacturer").isEmpty()) {
            C1810.m8133((Class<?>) Build.class, "MANUFACTURER", (Object) jSONObject.optString("android.os.Build.ro.product.manufacturer"));
            C1810.m8133((Class<?>) Build.class, "PRODUCT", (Object) jSONObject.optString("android.os.Build.ro.product.manufacturer"));
            C1810.m8133((Class<?>) Build.class, "BRAND", (Object) jSONObject.optString("android.os.Build.ro.product.manufacturer"));
            this.f7970.put("ro.product.manufacturer", jSONObject.optString("android.os.Build.ro.product.manufacturer"));
            this.f7970.put("ro.product.brand", jSONObject.optString("android.os.Build.ro.product.manufacturer"));
            this.f7970.put("ro.product.name", jSONObject.optString("android.os.Build.ro.product.manufacturer"));
        }
        if (jSONObject.has("android.os.Build.ro.product.model") && !jSONObject.optString("android.os.Build.ro.product.model").isEmpty()) {
            C1810.m8133((Class<?>) Build.class, "MODEL", (Object) jSONObject.optString("android.os.Build.ro.product.model"));
            C1810.m8133((Class<?>) Build.class, "DEVICE", (Object) jSONObject.optString("android.os.Build.ro.product.model"));
            this.f7970.put("ro.product.device", jSONObject.optString("android.os.Build.ro.product.model"));
            this.f7970.put("ro.product.model", jSONObject.optString("android.os.Build.ro.product.model"));
        }
        if (jSONObject.has("android.os.Build.ro.serialno") && !jSONObject.optString("android.os.Build.ro.serialno").isEmpty()) {
            C1810.m8133((Class<?>) Build.class, "SERIAL", (Object) jSONObject.optString("android.os.Build.ro.serialno"));
            this.f7970.put("ro.serialno", jSONObject.optString("android.os.Build.ro.serialno"));
        }
        if (jSONObject.has("android.os.SystemProperties.android_id") && !jSONObject.optString("android.os.SystemProperties.android_id").isEmpty()) {
            this.f7970.put("android_id", jSONObject.optString("android.os.SystemProperties.android_id"));
        }
        XC_MethodHook r4 = new XC_MethodHook() {
            /* access modifiers changed from: protected */
            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                Log.d("XposedEntry", String.format("%s#%s(%s) => %s", new Object[]{methodHookParam.thisObject.getClass().getName(), methodHookParam.method.getName(), Arrays.toString(methodHookParam.args), methodHookParam.getResult()}));
                if (methodHookParam.args.length > 1 && methodHookParam.args[1] != null && DeviceHook.this.f7970.containsKey(methodHookParam.args[1].toString())) {
                    methodHookParam.setResult(DeviceHook.this.f7970.get(methodHookParam.args[1].toString()));
                }
            }
        };
        XC_MethodHook r0 = new XC_MethodHook() {
            /* access modifiers changed from: protected */
            public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                Log.d("XposedEntry", String.format("%s#%s(%s) => %s", new Object[]{methodHookParam.thisObject.getClass().getName(), methodHookParam.method.getName(), Arrays.toString(methodHookParam.args), methodHookParam.getResult()}));
                if (methodHookParam.args.length > 0 && methodHookParam.args[0] != null && DeviceHook.this.f7970.containsKey(methodHookParam.args[0].toString())) {
                    methodHookParam.setResult(DeviceHook.this.f7970.get(methodHookParam.args[0].toString()));
                }
            }
        };
        C0429.m2603(Settings.System.class, "getString", r4);
        C0429.m2603(Settings.Secure.class, "getString", r4);
        C0429.m2604(this.packageParam.classLoader, "android.os.SystemProperties", "get", r0);
    }

    private void hookDevice(JSONObject jSONObject) {
        Class<NetworkInfo> cls;
        String str;
        C2847 r1;
        if (jSONObject.has("android.telephony.TelephonyManager.getLine1Number") && !jSONObject.optString("android.telephony.TelephonyManager.getLine1Number").isEmpty()) {
            C0429.m2603(TelephonyManager.class, "getLine1Number", new C2847(jSONObject.optString("android.telephony.TelephonyManager.getLine1Number")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getDeviceId") && !jSONObject.optString("android.telephony.TelephonyManager.getDeviceId").isEmpty()) {
            C0429.m2603(TelephonyManager.class, "getDeviceId", new C2847(jSONObject.optString("android.telephony.TelephonyManager.getDeviceId")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getSubscriberId") && !jSONObject.optString("android.telephony.TelephonyManager.getSubscriberId").isEmpty()) {
            C0429.m2603(TelephonyManager.class, "hasIccCard", new C2847(true));
            C0429.m2603(TelephonyManager.class, "getSubscriberId", new C2847(jSONObject.optString("android.telephony.TelephonyManager.getSubscriberId")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getSimOperator") && !jSONObject.optString("android.telephony.TelephonyManager.getSimOperator").isEmpty()) {
            C0429.m2603(TelephonyManager.class, "getSimOperator", new C2847(jSONObject.optString("android.telephony.TelephonyManager.getSimOperator")));
            C0429.m2603(TelephonyManager.class, "getNetworkOperator", new C2847(jSONObject.optString("android.telephony.TelephonyManager.getSimOperator")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getSimCountryIso") && !jSONObject.optString("android.telephony.TelephonyManager.getSimCountryIso").isEmpty()) {
            C0429.m2603(TelephonyManager.class, "getSimCountryIso", new C2847(jSONObject.optString("android.telephony.TelephonyManager.getSimCountryIso")));
            C0429.m2603(TelephonyManager.class, "getNetworkCountryIso", new C2847(jSONObject.optString("android.telephony.TelephonyManager.getSimCountryIso")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getSimOperatorName") && !jSONObject.optString("android.telephony.TelephonyManager.getSimOperatorName").isEmpty()) {
            C0429.m2603(TelephonyManager.class, "getSimOperatorName", new C2847(jSONObject.optString("android.telephony.TelephonyManager.getSimOperatorName")));
            C0429.m2603(TelephonyManager.class, "getNetworkOperatorName", new C2847(jSONObject.optString("android.telephony.TelephonyManager.getSimOperatorName")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getSimSerialNumber") && !jSONObject.optString("android.telephony.TelephonyManager.getSimSerialNumber").isEmpty()) {
            C0429.m2603(TelephonyManager.class, "getSimSerialNumber", new C2847(jSONObject.optString("android.telephony.TelephonyManager.getSimSerialNumber")));
        }
        if (jSONObject.has("android.telephony.TelephonyManager.getSimState") && !jSONObject.optString("android.telephony.TelephonyManager.getSimState").isEmpty()) {
            C0429.m2603(TelephonyManager.class, "getSimState", new C2847(Integer.valueOf(jSONObject.optInt("android.telephony.TelephonyManager.getSimState"))));
        }
        if (jSONObject.has("android.net.NetworkInfo.getType") && !jSONObject.optString("android.net.NetworkInfo.getType").isEmpty()) {
            if (jSONObject.optString("android.net.NetworkInfo.getType").equalsIgnoreCase("wifi")) {
                C0429.m2603(NetworkInfo.class, "getType", new C2847(1));
                cls = NetworkInfo.class;
                str = "getTypeName";
                r1 = new C2847("WIFI");
            } else {
                cls = NetworkInfo.class;
                str = "getTypeName";
                r1 = new C2847("MOBILE");
            }
            C0429.m2603(cls, str, r1);
        }
    }

    private void hookWifi(JSONObject jSONObject) {
        if (jSONObject.has("android.net.wifi.WifiInfo.getSSID") && !jSONObject.optString("android.net.wifi.WifiInfo.getSSID").isEmpty()) {
            C0429.m2603(WifiInfo.class, "getSSID", new C2847(jSONObject.optString("android.net.wifi.WifiInfo.getSSID")));
        }
        if (jSONObject.has("android.net.wifi.WifiInfo.getBSSID") && !jSONObject.optString("android.net.wifi.WifiInfo.getBSSID").isEmpty()) {
            C0429.m2603(WifiInfo.class, "getBSSID", new C2847(jSONObject.optString("android.net.wifi.WifiInfo.getBSSID")));
        }
        if (jSONObject.has("android.net.wifi.WifiInfo.getMacAddress") && !jSONObject.optString("android.net.wifi.WifiInfo.getMacAddress").isEmpty()) {
            C0429.m2603(WifiInfo.class, "getMacAddress", new C2847(jSONObject.optString("android.net.wifi.WifiInfo.getMacAddress")));
        }
    }

    private void hookOther(final JSONObject jSONObject) {
        if (jSONObject.has("android.content.res.language") && !jSONObject.optString("android.content.res.language").isEmpty()) {
            final Locale r0 = C1552.m7149(jSONObject.optString("android.content.res.language"));
            C0429.m2603(Resources.class, "updateConfiguration", new XC_MethodHook() {
                /* access modifiers changed from: protected */
                public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                    if (methodHookParam.args.length > 0 && methodHookParam.args[0] != null) {
                        Configuration configuration = (Configuration) methodHookParam.args[0];
                        Locale.setDefault(r0);
                        if (Build.VERSION.SDK_INT >= 17) {
                            configuration.setLocale(r0);
                        }
                        configuration.locale = r0;
                        methodHookParam.args[0] = configuration;
                    }
                }
            });
        }
        if (jSONObject.has("android.content.res.display.dpi") && jSONObject.optInt("android.content.res.display.dpi") > 0) {
            final Integer valueOf = Integer.valueOf(jSONObject.optInt("android.content.res.display.dpi"));
            C0429.m2603(Resources.class, "updateConfiguration", new XC_MethodHook() {
                /* access modifiers changed from: protected */
                public void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                    if (methodHookParam.args.length > 1 && methodHookParam.args[1] != null) {
                        Configuration configuration = (Configuration) methodHookParam.args[0];
                        DisplayMetrics displayMetrics = (DisplayMetrics) methodHookParam.args[1];
                        displayMetrics.density = ((float) valueOf.intValue()) / 160.0f;
                        displayMetrics.densityDpi = valueOf.intValue();
                        if (Build.VERSION.SDK_INT >= 17) {
                            C1810.m8134((Object) configuration, "densityDpi", valueOf.intValue());
                        }
                    }
                }
            });
        }
        if (jSONObject.has("android.web.head.user.agent") && !jSONObject.optString("android.web.head.user.agent").isEmpty()) {
            Log.d("XposedEntry", "hookOtherInfo: android.web.head.user.agent: " + jSONObject.optString("android.web.head.user.agent"));
            C0429.m2603(System.class, "getProperty", new XC_MethodHook() {
                /* access modifiers changed from: protected */
                public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                    if (methodHookParam.args[0].toString().equalsIgnoreCase("http.agent")) {
                        Log.d("XposedEntry", "afterHookedMethod: ==== System BEFORE ====" + methodHookParam.getResult());
                        methodHookParam.setResult(jSONObject.optString("android.web.head.user.agent"));
                        Log.d("XposedEntry", "afterHookedMethod: ==== System AFTER  ====" + methodHookParam.getResult());
                    }
                }
            });
            if (Build.VERSION.SDK_INT >= 17) {
                C0429.m2603(WebSettings.class, "getDefaultUserAgent", new C2847(jSONObject.optString("android.web.head.user.agent")));
            }
            C0429.m2603(WebView.class, "getSettings", new XC_MethodHook() {
                /* access modifiers changed from: protected */
                public void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                    C0429.m2603(((WebSettings) methodHookParam.getResult()).getClass(), "getUserAgentString", new C2847(jSONObject.optString("android.web.head.user.agent")));
                }
            });
        }
    }

}
