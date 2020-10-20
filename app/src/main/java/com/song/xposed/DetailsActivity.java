package com.song.xposed;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.qmuiteam.qmui.widget.QMUILoadingView;
import com.qmuiteam.qmui.widget.grouplist.QMUICommonListItemView;
import com.qmuiteam.qmui.widget.grouplist.QMUIGroupListView;
import com.song.xposed.beans.ApplicationBean;
import com.song.xposed.enums.DetailsEnum;
import com.song.xposed.infos.HookInfo;
import com.song.xposed.utils.PreferencesUtils;
import com.song.xposed.utils.RandomHelper;
import com.song.xposed.utils.SystemInfoUtils;
import com.song.xposed.utils.ThreadPoolUtils;

import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.group_list_view)
    QMUIGroupListView mGroupListView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.empty_view_loading)
    QMUILoadingView loadingView;

    private HookInfo hookInfo;
    private ApplicationBean applicationBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        applicationBean = getIntent().getParcelableExtra("app");
        getSupportActionBar().setTitle(Objects.requireNonNull(applicationBean).getName());

        initData();
    }

    private void storageHookInfo() {
        PreferencesUtils.putString(getApplicationContext(), applicationBean.getPackageName(), hookInfo.toString());
    }

    private void initData() {
        ThreadPoolUtils.getInstance().execute(() -> {
            String value = PreferencesUtils.getString(getApplicationContext(), applicationBean.getPackageName());
            if (!TextUtils.isEmpty(value)) {
                hookInfo = JSON.parseObject(value, HookInfo.class);
            } else {
                hookInfo = SystemInfoUtils.getDefaultInfo(getApplicationContext());
            }
            DetailsActivity.this.runOnUiThread(() -> {
                loadingView.setVisibility(View.GONE);
                initGroupListView();
            });
        });
    }

    private void initGroupListView() {
        int height = QMUIDisplayHelper.dp2px(getApplicationContext(), 48);
        QMUICommonListItemView manufacturerItem = mGroupListView.createItemView(
                null,
                DetailsEnum.MANUFACTURER.value,
                hookInfo.buildManufacturer,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView modelItem = mGroupListView.createItemView(
                null,
                DetailsEnum.MODEL.value,
                hookInfo.buildModel,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView serialItem = mGroupListView.createItemView(
                null,
                DetailsEnum.SERIAL.value,
                hookInfo.buildSerial,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView versionItem = mGroupListView.createItemView(
                null,
                DetailsEnum.VERSION.value,
                hookInfo.buildVersionCodeName,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        // *****************************************Phone*******************************************
        QMUICommonListItemView phoneItem = mGroupListView.createItemView(
                null,
                DetailsEnum.PHONE.value,
                hookInfo.telephonyGetLine1Number,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView netTypeItem = mGroupListView.createItemView(
                null,
                DetailsEnum.NETWORK.value,
                hookInfo.telephonyGetNetworkType,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView imeiItem = mGroupListView.createItemView(
                null,
                DetailsEnum.IMEI.value,
                hookInfo.telephonyGetDeviceId,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView imsiItem = mGroupListView.createItemView(
                null,
                DetailsEnum.IMSI.value,
                hookInfo.telephonyGetSubscriberId,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView simProductItem = mGroupListView.createItemView(
                null,
                DetailsEnum.SIM_PRODUCT.value,
                hookInfo.telephonyGetSimOperator,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView simCountryItem = mGroupListView.createItemView(
                null,
                DetailsEnum.SIM_COUNTRY.value,
                hookInfo.telephonyGetSimCountryISO,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView simNameItem = mGroupListView.createItemView(
                null,
                DetailsEnum.SIM_NAME.value,
                hookInfo.telephonyGetSimOperatorName,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView simNumberItem = mGroupListView.createItemView(
                null,
                DetailsEnum.SIM_NUMBER.value,
                hookInfo.telephonyGetSimSerialNumber,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView simStatusItem = mGroupListView.createItemView(
                null,
                DetailsEnum.SIM_STATUS.value,
                hookInfo.telephonyGetSimState,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        // *****************************************WIFI*******************************************
        QMUICommonListItemView ssidItem = mGroupListView.createItemView(
                null,
                DetailsEnum.SSID.value,
                hookInfo.wifiInfoGetSSID,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView bssidItem = mGroupListView.createItemView(
                null,
                DetailsEnum.BSSID.value,
                hookInfo.wifiInfoGetBSSID,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView wifiMacItem = mGroupListView.createItemView(
                null,
                DetailsEnum.MAC.value,
                hookInfo.wifiInfoGetMacAddress,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        // *****************************************其他********************* **********************
        QMUICommonListItemView languageItem = mGroupListView.createItemView(
                null,
                DetailsEnum.LANGUAGE.value,
                hookInfo.systemLanguage,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView dpiItem = mGroupListView.createItemView(
                null,
                DetailsEnum.DPI.value,
                hookInfo.displayDip,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        final QMUICommonListItemView androidIdItem = mGroupListView.createItemView(
                null,
                DetailsEnum.ANDROID_ID.value,
                hookInfo.settingsSecureAndroidId,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);
        QMUICommonListItemView uaItem = mGroupListView.createItemView(
                null,
                DetailsEnum.UA.value,
                hookInfo.webUserAgent,
                QMUICommonListItemView.HORIZONTAL,
                QMUICommonListItemView.ACCESSORY_TYPE_NONE,
                height);

        View.OnClickListener onClickListener = v -> {
            if (v instanceof QMUICommonListItemView) {
                QMUICommonListItemView itemView = (QMUICommonListItemView) v;
                Toast.makeText(getApplicationContext(), itemView.getText(), Toast.LENGTH_SHORT).show();
                if (TextUtils.equals(itemView.getText().toString(), DetailsEnum.ANDROID_ID.value)) {
                    hookInfo.settingsSecureAndroidId = RandomHelper.getInstance().randomAndroidId();
                    androidIdItem.setDetailText(hookInfo.settingsSecureAndroidId);
                } else if (TextUtils.equals(itemView.getText().toString(), DetailsEnum.IMEI.value)) {
                    hookInfo.telephonyGetDeviceId = RandomHelper.getInstance().randomTelephonyGetDeviceId();
                    imeiItem.setDetailText(hookInfo.telephonyGetDeviceId);
                }
            }
        };

        QMUIGroupListView.newSection(this)
                .setTitle("常规")
                .addItemView(manufacturerItem, onClickListener)
                .addItemView(modelItem, onClickListener)
                .addItemView(serialItem, onClickListener)
                .addItemView(versionItem, onClickListener)
                .addTo(mGroupListView);
        QMUIGroupListView.newSection(this)
                .setTitle("手机")
                .addItemView(phoneItem, onClickListener)
                .addItemView(netTypeItem, onClickListener)
                .addItemView(imeiItem, onClickListener)
                .addItemView(imsiItem, onClickListener)
                .addItemView(simProductItem, onClickListener)
                .addItemView(simCountryItem, onClickListener)
                .addItemView(simNameItem, onClickListener)
                .addItemView(simNumberItem, onClickListener)
                .addItemView(simStatusItem, onClickListener)
                .addTo(mGroupListView);
        QMUIGroupListView.newSection(this)
                .setTitle("WIFI")
                .addItemView(ssidItem, onClickListener)
                .addItemView(bssidItem, onClickListener)
                .addItemView(wifiMacItem, onClickListener)
                .addTo(mGroupListView);
        QMUIGroupListView.newSection(this)
                .setTitle("其他")
                .addItemView(languageItem, onClickListener)
                .addItemView(dpiItem, onClickListener)
                .addItemView(androidIdItem, onClickListener)
                .addItemView(uaItem, onClickListener)
                .addTo(mGroupListView);
    }

    public void onRunApp(View view) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setPackage(applicationBean.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, 0);
        if (list.size() > 0) {
            ResolveInfo ri = list.get(0);
            String packageName = ri.activityInfo.packageName;
            String name = ri.activityInfo.name;
            ComponentName componentName = new ComponentName(packageName, name);
            intent.setComponent(componentName);
            startActivity(intent);
        }

//        LibSuHelper.getInstance().addCommand("monkey -p " + applicationBean.getPackageName() + " -c android.intent.category.LAUNCHER 1", 0, new Shell.OnCommandResultListener() {
//            @Override
//            public void onCommandResult(int commandCode, int exitCode, List<String> output) {
//                if (exitCode != 0) {
//                    Snackbar.make(getApplicationContext(), getString(R.string.start_app_error) + exitCode, Snackbar.LENGTH_LONG).show();
//                }
//            }
//        });
    }

    public void onClearApp(View view) {
        PreferencesUtils.remove(getApplicationContext(), applicationBean.getPackageName());
//        if (wipeDataConfirm) {
//            wipeDataConfirm = false;
//            LibSuHelper.getInstance().addCommand("pm clear " + applicationBean.getPackageName(), 0, new Shell.OnCommandResultListener() {
//                @Override
//                public void onCommandResult(int commandCode, int exitCode, List<String> output) {
//                    if (exitCode != 0)
//                        Snackbar.make(mDetailContent, getString(R.string.wipe_data_error) + exitCode, Snackbar.LENGTH_LONG).show();
//                    else
//                        Snackbar.make(mDetailContent, R.string.wipe_data_success, Snackbar.LENGTH_LONG).show();
//                }
//            });
//        } else {
//            wipeDataConfirm = true;
//            new Timer().schedule(new TimerTask() {
//                @Override
//                public void run() {
//                    if (wipeDataConfirm) {
//                        Snackbar.make(mDetailContent, "清除数据为敏感操作，请在2秒内连续点击次。", Snackbar.LENGTH_LONG).show();
//                    }
//                    wipeDataConfirm = false;
//                }
//            }, 2000);
//        }
    }

    public void onForceStopApp(View view) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", applicationBean.getPackageName(), null));
        startActivity(intent);

//        LibSuHelper.getInstance().addCommand("am force-stop " + applicationBean.getPackageName(), 0, (commandCode, exitCode, output) -> {
//            if (exitCode != 0)
//                Snackbar.make(mDetailContent, getString(R.string.force_stop_error) + exitCode, Snackbar.LENGTH_LONG).show();
//            else
//                Snackbar.make(mDetailContent, R.string.force_stop_success, Snackbar.LENGTH_LONG).show();
//        });
    }

    public void onSaveConfig(View view) {
        storageHookInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
