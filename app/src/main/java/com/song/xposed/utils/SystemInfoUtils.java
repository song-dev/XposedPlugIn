package com.song.xposed.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.song.xposed.infos.HookInfo;

public class SystemInfoUtils {

    @SuppressLint({"MissingPermission", "HardwareIds"})
    public static HookInfo getDefaultInfo(Context context) {
        HookInfo hookInfo = new HookInfo();
        hookInfo.buildManufacturer = Build.MANUFACTURER;
        hookInfo.buildModel = Build.MODEL;
        hookInfo.buildVersionCodeName = Build.VERSION.CODENAME;
        try {

            hookInfo.systemLanguage = context.getResources().getConfiguration().locale.getLanguage();
            hookInfo.displayDip = context.getResources().getConfiguration().densityDpi + "";
            hookInfo.settingsSecureAndroidId = Settings.System.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            hookInfo.webUserAgent = System.getProperty("http.agent");

            hookInfo.wifiInfoGetSSID = GatewayUtils.getSsid(context);
            hookInfo.wifiInfoGetBSSID = GatewayUtils.getBssid(context);
            hookInfo.wifiInfoGetMacAddress = GatewayUtils.getMacAddress(context);

            TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                hookInfo.telephonyGetNetworkType = tm.getNetworkType() + "";
                hookInfo.telephonyGetSimOperatorName = tm.getSimOperator();
                hookInfo.telephonyGetSimCountryISO = tm.getSimCountryIso();
                hookInfo.telephonyGetSimOperatorName = tm.getSimOperatorName();
                hookInfo.telephonyGetSimState = tm.getSimState() + "";
                hookInfo.telephonyGetSimSerialNumber = tm.getSimSerialNumber();
                hookInfo.telephonyGetLine1Number = tm.getLine1Number();
                hookInfo.telephonyGetDeviceId = tm.getDeviceId();
                hookInfo.telephonyGetSubscriberId = tm.getSubscriberId();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                hookInfo.buildSerial = Build.getSerial();
            } else {
                hookInfo.buildSerial = Build.SERIAL;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hookInfo;
    }

}
