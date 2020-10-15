package com.song.xposed.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chensongsong on 2020/10/15.
 */
public abstract class PreferencesProvider extends ContentProvider implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final Uri f7982;
    private final C2640[] f7983;
    private final Map<String, SharedPreferences> map;
    private final UriMatcher uriMatcher;

    private static class C2674 {

        public final String f7986;
        public final String f7987;

        private C2674(String str, String str2) {
            this.f7986 = str;
            this.f7987 = str2;
        }
    }

    public PreferencesProvider(String str, C2640[] r4) {
        this.f7982 = Uri.parse("content://" + str);
        this.f7983 = r4;
        this.map = new HashMap<>(r4.length);
        this.uriMatcher = new UriMatcher(-1);
        this.uriMatcher.addURI(str, "*/", 1);
        this.uriMatcher.addURI(str, "*/*", 2);
    }

    public PreferencesProvider(String authorities, String[] arrays) {
        this(authorities, C2640.m12073(arrays));
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    private Uri m12259(String str, String str2) {
        Uri.Builder appendPath = this.f7982.buildUpon().appendPath(str);
        if (m12265(str2)) {
            appendPath.appendPath(str2);
        }
        return appendPath.build();
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    private C2674 m12260(Uri uri) {
        int match = this.uriMatcher.match(uri);
        if (match == 2 || match == 1) {
            List<String> pathSegments = uri.getPathSegments();
            String str = pathSegments.get(0);
            String str2 = "";
            if (match == 2) {
                str2 = pathSegments.get(1);
            }
            return new C2674(str, str2);
        }
        throw new IllegalArgumentException("Invalid URI: " + uri);
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    private static String m12261(ContentValues contentValues) {
        String asString = contentValues.getAsString("key");
        return asString == null ? "" : asString;
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    private String m12262(SharedPreferences sharedPreferences) {
        for (Map.Entry next : this.map.entrySet()) {
            if (next.getValue() == sharedPreferences) {
                return (String) next.getKey();
            }
        }
        throw new IllegalArgumentException("Unknown preference file");
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    private static String m12263(C2674 r1, ContentValues contentValues) {
        String r12 = r1.f7987;
        String r2 = m12261(contentValues);
        if (r12.length() == 0 || r2.length() == 0) {
            return r12.length() != 0 ? r12 : r2.length() != 0 ? r2 : "";
        }
        if (r12.equals(r2)) {
            return r12;
        }
        throw new IllegalArgumentException("Conflicting keys specified in URI and ContentValues");
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    private void m12264(SharedPreferences.Editor editor, String str, ContentValues contentValues) {
        Integer asInteger = contentValues.getAsInteger("type");
        if (asInteger != null) {
            Object r5 = C1188.m5474(contentValues.get("value"), asInteger.intValue());
            if (m12265(str)) {
                switch (asInteger.intValue()) {
                    case 0:
                        editor.remove(str);
                        return;
                    case 1:
                        editor.putString(str, (String) r5);
                        return;
                    case 2:
                        if (Build.VERSION.SDK_INT >= 11) {
                            editor.putStringSet(str, C1188.m5476(r5));
                            return;
                        }
                        throw new IllegalArgumentException("String set preferences not supported on API < 11");
                    case 3:
                        editor.putInt(str, ((Integer) r5).intValue());
                        return;
                    case 4:
                        editor.putLong(str, ((Long) r5).longValue());
                        return;
                    case 5:
                        editor.putFloat(str, ((Float) r5).floatValue());
                        return;
                    case 6:
                        editor.putBoolean(str, ((Boolean) r5).booleanValue());
                        return;
                    default:
                        throw new IllegalArgumentException("Cannot set preference with type " + asInteger);
                }
            } else if (asInteger.intValue() == 0) {
                editor.clear();
            } else {
                throw new IllegalArgumentException("Attempting to insert preference with null or empty key");
            }
        } else {
            throw new IllegalArgumentException("Invalid or no preference type specified");
        }
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    private static boolean m12265(String str) {
        return (str == null || str.length() == 0) ? false : true;
    }

    /* renamed from: ˉ  reason: contains not printable characters */
    private Object[] m12266(String[] strArr, String str, Object obj) {
        Object[] objArr = new Object[strArr.length];
        for (int i = 0; i < objArr.length; i++) {
            String str2 = strArr[i];
            if ("key".equals(str2)) {
                objArr[i] = str;
            } else if ("type".equals(str2)) {
                objArr[i] = Integer.valueOf(C1188.m5478(obj));
            } else if ("value".equals(str2)) {
                objArr[i] = C1188.m5479(obj);
            } else {
                throw new IllegalArgumentException("Invalid column name: " + str2);
            }
        }
        return objArr;
    }

    /* renamed from: ˏ  reason: contains not printable characters */
    private SharedPreferences m12267(String str) {
        SharedPreferences sharedPreferences = this.map.get(str);
        if (sharedPreferences != null) {
            return sharedPreferences;
        }
        throw new IllegalArgumentException("Unknown preference file name: " + str);
    }

    /* renamed from: ˏ  reason: contains not printable characters */
    private C2640 m12268(SharedPreferences sharedPreferences) {
        String r6 = m12262(sharedPreferences);
        for (C2640 r3 : this.f7983) {
            if (r3.m12074().equals(r6)) {
                return r3;
            }
        }
        throw new IllegalArgumentException("Unknown preference file");
    }

    /* renamed from: ˏ  reason: contains not printable characters */
    private void m12269(String str, String str2, boolean z) {
        if (!m12271(str, str2, z)) {
            throw new SecurityException("Insufficient permissions to access: " + str + "/" + str2);
        }
    }

    /* renamed from: ˑ  reason: contains not printable characters */
    private SharedPreferences m12270(String str, String str2, boolean z) {
        m12269(str, str2, z);
        return m12267(str);
    }

    public int bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        C2674 r8 = m12260(uri);
        String r0 = r8.f7986;
        if (!m12265(r8.f7987)) {
            SharedPreferences.Editor edit = m12267(r0).edit();
            for (ContentValues contentValues : contentValuesArr) {
                String r5 = m12261(contentValues);
                m12269(r0, r5, true);
                m12264(edit, r5, contentValues);
            }
            if (edit.commit()) {
                return contentValuesArr.length;
            }
            return 0;
        }
        throw new IllegalArgumentException("Cannot bulk insert with single key URI");
    }

    public int delete(Uri uri, String str, String[] strArr) {
        C2674 r2 = m12260(uri);
        String r3 = r2.f7986;
        String r22 = r2.f7987;
        SharedPreferences.Editor edit = m12270(r3, r22, true).edit();
        if (m12265(r22)) {
            edit.remove(r22);
        } else {
            edit.clear();
        }
        return edit.commit() ? 1 : 0;
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues contentValues) {
        if (contentValues == null) {
            return null;
        }
        C2674 r4 = m12260(uri);
        String r1 = r4.f7986;
        String r42 = m12263(r4, contentValues);
        SharedPreferences.Editor edit = m12270(r1, r42, true).edit();
        m12264(edit, r42, contentValues);
        if (edit.commit()) {
            return m12259(r1, r42);
        }
        return null;
    }

    public boolean onCreate() {
        for (C2640 r4 : this.f7983) {
            Context context = getContext();
            if (r4.m12075() && Build.VERSION.SDK_INT >= 24) {
                context = context.createDeviceProtectedStorageContext();
            }
            SharedPreferences sharedPreferences = context.getSharedPreferences(r4.m12074(), 0);
            sharedPreferences.registerOnSharedPreferenceChangeListener(this);
            this.map.put(r4.m12074(), sharedPreferences);
        }
        return true;
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
        C2640 r3 = m12268(sharedPreferences);
        Uri r4 = m12259(r3.m12074(), str);
        Context context = getContext();
        if (r3.m12075() && Build.VERSION.SDK_INT >= 24) {
            context = context.createDeviceProtectedStorageContext();
        }
        context.getContentResolver().notifyChange(r4, (ContentObserver) null);
    }

    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        C2674 r1 = m12260(uri);
        String r3 = r1.f7986;
        String r12 = r1.f7987;
        Map<String, ?> all = m12270(r3, r12, false).getAll();
        if (strArr == null) {
            strArr = C1951.f6068;
        }
        MatrixCursor matrixCursor = new MatrixCursor(strArr);
        if (m12265(r12)) {
            matrixCursor.addRow(m12266(strArr, r12, (Object) all.get(r12)));
        } else {
            for (Map.Entry next : all.entrySet()) {
                matrixCursor.addRow(m12266(strArr, (String) next.getKey(), next.getValue()));
            }
        }
        return matrixCursor;
    }

    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return contentValues == null ? delete(uri, str, strArr) : insert(uri, contentValues) != null ? 1 : 0;
    }

    /* access modifiers changed from: protected */
    /* renamed from: ˉ  reason: contains not printable characters */
    public boolean m12271(String str, String str2, boolean z) {
        return true;
    }
}