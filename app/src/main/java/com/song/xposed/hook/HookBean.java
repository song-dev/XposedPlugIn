package com.song.xposed.hook;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;

public class HookBean implements Serializable {

    @JSONField(name = "android.telephony.TelephonyManager.getSimOperator")
    public String f4086;

    @JSONField(name = "android.content.res.language")
    public String f4087;

    @JSONField(name = "android.telephony.TelephonyManager.getSimState")
    public String f4088;

    @JSONField(name = "android.net.NetworkInfo.getType")
    public String f4089;

    @JSONField(name = "android.telephony.TelephonyManager.getSimCountryIso")
    public String f4090;

    @JSONField(name = "android.net.wifi.WifiInfo.getSSID")
    public String f4091;

    @JSONField(name = "android.os.Build.ro.product.manufacturer")
    public String f4092;

    @JSONField(name = "android.os.SystemProperties.android_id")
    public String f4093;

    @JSONField(name = "android.telephony.TelephonyManager.getSimOperatorName")
    public String f4094;

    @JSONField(name = "android.os.Build.ro.product.model")
    public String f4095;

    @JSONField(name = "android.os.Build.ro.serialno")
    public String f4096;

    @JSONField(name = "android.os.Build.VERSION.SDK_INT")
    public String f4097;

    @JSONField(name = "android.net.wifi.WifiInfo.getMacAddress")
    public String f4098;

    @JSONField(name = "android.content.res.display.dpi")
    public String f4099;

    @JSONField(name = "android.telephony.TelephonyManager.getLine1Number")
    public String f4100;

    @JSONField(name = "android.telephony.TelephonyManager.getSubscriberId")
    public String f4101;

    @JSONField(name = "android.net.wifi.WifiInfo.getBSSID")
    public String f4102;

    @JSONField(name = "android.os.Build.VERSION.RELEASE")
    public String f4103;

    @JSONField(name = "android.telephony.TelephonyManager.getDeviceId")
    public String f4104;

    @JSONField(name = "android.web.head.user.agent")
    public String f4105;

    @JSONField(name = "android.telephony.TelephonyManager.getSimSerialNumber")
    public String f4106;
}