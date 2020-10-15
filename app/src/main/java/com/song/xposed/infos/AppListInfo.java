package com.song.xposed.infos;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.song.xposed.beans.ApplicationBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chensongsong on 2020/10/13.
 */
public class AppListInfo {

    /**
     * 获取应用列表
     *
     * @param context
     * @return
     */
    public static List<ApplicationBean> getAppListInfo(Context context) {
        List<ApplicationBean> list = new ArrayList<>();
        PackageManager packageManager = context.getApplicationContext().getPackageManager();
        List<ApplicationInfo> applications = packageManager.getInstalledApplications(0);
        for (ApplicationInfo applicationInfo : applications) {
            ApplicationBean bean = new ApplicationBean();
            bean.setName(applicationInfo.loadLabel(packageManager).toString());
            bean.setPackageName(applicationInfo.packageName);
            bean.setIcon(applicationInfo.loadIcon(packageManager));
            if ((ApplicationInfo.FLAG_SYSTEM & applicationInfo.flags) == 0) {
                list.add(bean);
                return list;
            }
        }
        return list;
    }

}
