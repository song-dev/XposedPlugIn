package com.song.xposed.beans;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

/**
 * Created by chensongsong on 2020/8/11.
 */
public class ApplicationBean {

    private String name;
    private String packageName;
    private String version;
    private int buildVersion;
    private Drawable icon;
    private boolean systemApp;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getBuildVersion() {
        return buildVersion;
    }

    public void setBuildVersion(int buildVersion) {
        this.buildVersion = buildVersion;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isSystemApp() {
        return systemApp;
    }

    public void setSystemApp(boolean systemApp) {
        this.systemApp = systemApp;
    }

    @NonNull
    @Override
    public String toString() {
        return "ApplicationBean{" +
                "name='" + name + '\'' +
                ", packageName='" + packageName + '\'' +
                ", version='" + version + '\'' +
                ", buildVersion=" + buildVersion +
                ", icon=" + icon +
                ", systemApp=" + systemApp +
                '}';
    }
}
