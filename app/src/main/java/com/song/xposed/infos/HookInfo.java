package com.song.xposed.infos;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.lang.reflect.Field;

import androidx.annotation.NonNull;

/**
 * Created by chensongsong on 2020/10/14.
 */
public class HookInfo implements Serializable {

    public String buildDisplay = null;
    public String buildProduct = null;
    public String buildDevice = null;
    public String buildBoard = null;
    public String buildManufacturer = null;
    public String buildBrand = null;
    public String buildModel = null;
    public String buildBootloader = null;
    public String buildRadio = null;
    public String buildHardware = null;
    public String buildSerial = null;
    public String buildType = null;
    public String buildUser = null;
    public String buildHost = null;
    public String buildTags = null;
    public String buildCpuAbi = null;
    public String buildCpuAbi2 = null;
    public String buildFingerprint = null;

    public String buildVersionIncremental = null;
    public String buildVersionRelease = null;
    public String buildVersionSdkInt = null;
    public String buildVersionCodeName = null;
    public String buildVersionSdk = null;

    @JSONField(name = "android.os.SystemProperties.android_id")
    public String settingsSecureAndroidId = null;

    public String telephonyGetDeviceId = null;
    public String telephonyGetDeviceSoftwareVersion = null;
    public String telephonyGetLine1Number = null;
    public String telephonyGetNetworkCountryISO = null;
    public String telephonyGetNetworkOperator = null;
    public String telephonyGetNetworkOperatorName = null;
    public String telephonyGetNetworkType = null;
    public String telephonyGetPhoneType = null;
    public String telephonyGetSimCountryISO = null;
    public String telephonyGetSimOperator = null;
    public String telephonyGetSimOperatorName = null;
    public String telephonyGetSimSerialNumber = null;
    public String telephonyGetSimState = null;
    public String telephonyGetSubscriberId = null;

    public String webUserAgent = null;

    public String wifiInfoGetSSID = null;
    public String wifiInfoGetBSSID = null;
    public String wifiInfoGetMacAddress = null;
    public String wifiInfoGetNetworkId = null;
    public String wifiInfoGetIpAddress = null;

    public String displayDip = null;

    public String systemCpuInfo = null;
    public String systemLanguage = null;

    @NonNull
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public boolean isEmpty() {
        for (Field field : this.getClass().getFields()) {
            try {
                if (field.get(this) != null && !field.get(this).toString().isEmpty())
                    return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public com.alibaba.fastjson.JSONObject toJSON() {
        return (com.alibaba.fastjson.JSONObject) JSON.toJSON(this);
    }

    public void merge(HookInfo allHookInfo) {
        for (Field field : this.getClass().getFields()) {
            try {
                Object thisObject = field.get(this);
                Object mergeObject = field.get(allHookInfo);


                if (thisObject == null && mergeObject != null) {
                    field.setAccessible(true);
                    field.set(this, mergeObject);
                    continue;
                }

                if (thisObject instanceof String && thisObject.toString().isEmpty() && mergeObject != null) {
                    field.setAccessible(true);
                    field.set(this, mergeObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
