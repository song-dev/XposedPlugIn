package com.song.xposed.enums;

public enum DetailsEnum {

    MANUFACTURER("厂商"),
    MODEL("型号"),
    SERIAL("序号"),
    VERSION("版本"),
    PHONE("手机号"),
    NETWORK("网络类型"),
    IMEI("IMEI"),
    IMSI("IMSI"),
    SIM_PRODUCT("SIM厂商"),
    SIM_COUNTRY("SIM国际"),
    SIM_NAME("SIM名称"),
    SIM_NUMBER("SIM序号"),
    SIM_STATUS("SIM状态"),
    SSID("SSID"),
    BSSID("BSSID"),
    MAC("MAC"),
    LANGUAGE("语言"),
    DPI("DPI"),
    ANDROID_ID("AndroidID"),
    UA("UA"),
    JSON("JSON");

    public String value;

    DetailsEnum(String value) {
        this.value = value;
    }

}
