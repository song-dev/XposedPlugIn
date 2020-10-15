package com.song.xposed.beans;

import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by chensongsong on 2020/10/13.
 */
public class ApplicationBean implements Parcelable {

    private String name;
    private String packageName;
    private Drawable icon;

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

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.packageName);
    }

    public ApplicationBean() {
    }

    public ApplicationBean(Parcel in) {
        this.name = in.readString();
        this.packageName = in.readString();
    }

    public static final Parcelable.Creator<ApplicationBean> CREATOR = new Parcelable.Creator<ApplicationBean>() {
        @Override
        public ApplicationBean createFromParcel(Parcel source) {
            return new ApplicationBean(source);
        }

        @Override
        public ApplicationBean[] newArray(int size) {
            return new ApplicationBean[size];
        }
    };
}
