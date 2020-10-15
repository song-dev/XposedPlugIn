package com.song.xposed.preferences;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.text.TextUtils;

import com.song.xposed.utils.ValueUtils;
import com.song.xposed.utils.AppException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

public class ProviderUtils implements SharedPreferences {

    private final Context context;
    private final Handler handler;
    private final Uri uri;
    private final boolean allow;
    private final WeakHashMap<SharedPreferences.OnSharedPreferenceChangeListener, PrefObserver> weakHashMap;

    private class PrefObserver extends ContentObserver {

        private final WeakReference<SharedPreferences.OnSharedPreferenceChangeListener> f2696;

        private PrefObserver(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
            super(ProviderUtils.this.handler);
            this.f2696 = new WeakReference<>(onSharedPreferenceChangeListener);
        }

        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean z, Uri uri) {
            String lastPathSegment = uri.getLastPathSegment();
            SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = (SharedPreferences.OnSharedPreferenceChangeListener) this.f2696.get();
            if (onSharedPreferenceChangeListener == null) {
                ProviderUtils.this.context.getContentResolver().unregisterContentObserver(this);
            } else {
                onSharedPreferenceChangeListener.onSharedPreferenceChanged(ProviderUtils.this, lastPathSegment);
            }
        }
    }

    private class AppEditor implements SharedPreferences.Editor {

        private final ArrayList<ContentValues> list;

        private AppEditor() {
            this.list = new ArrayList<>();
        }

        @Override
        public void apply() {
            commit();
        }

        @Override
        public SharedPreferences.Editor clear() {
            delete("");
            return this;
        }

        @Override
        public boolean commit() {
            Uri uri = ProviderUtils.this.uri.buildUpon().appendPath("").build();
            return ProviderUtils.this.bulkInsert(uri, this.list.toArray(new ContentValues[0]));
        }

        @Override
        public SharedPreferences.Editor putBoolean(String key, boolean value) {
            makeValues(key, 6).put("value", value ? 1 : 0);
            return this;
        }

        @Override
        public SharedPreferences.Editor putFloat(String key, float value) {
            makeValues(key, 5).put("value", value);
            return this;
        }

        @Override
        public SharedPreferences.Editor putInt(String key, int value) {
            makeValues(key, 3).put("value", value);
            return this;
        }

        @Override
        public SharedPreferences.Editor putLong(String key, long value) {
            makeValues(key, 4).put("value", value);
            return this;
        }

        @Override
        public SharedPreferences.Editor putString(String key, String value) {
            makeValues(key, 1).put("value", value);
            return this;
        }

        @Override
        public SharedPreferences.Editor putStringSet(String key, Set<String> set) {
            makeValues(key, 2).put("value", ValueUtils.castString(set));
            return this;
        }

        @Override
        public SharedPreferences.Editor remove(String key) {
            ProviderUtils.check(key);
            delete(key);
            return this;
        }

        private void delete(String key) {
            ContentValues contentValues = newContentValues(key, 0);
            contentValues.putNull("value");
            this.list.add(0, contentValues);
        }

        private ContentValues newContentValues(String key, int type) {
            ContentValues contentValues = new ContentValues(4);
            contentValues.put("key", key);
            contentValues.put("type", type);
            return contentValues;
        }

        private ContentValues makeValues(String key, int type) {
            ProviderUtils.check(key);
            ContentValues contentValues = newContentValues(key, type);
            this.list.add(contentValues);
            return contentValues;
        }
    }

    public ProviderUtils(Context context, String authorities, String prefName) {
        this(context, authorities, prefName, false);
    }

    public ProviderUtils(Context context, String authorities, String prefName, boolean allow) {
        checkParameter("context", context);
        checkParameter("authority", authorities);
        checkParameter("prefName", prefName);
        this.context = context;
        this.handler = new Handler(context.getMainLooper());
        this.uri = Uri.parse("content://" + authorities).buildUpon().appendPath(prefName).build();
        this.weakHashMap = new WeakHashMap<>();
        this.allow = allow;
    }

    private Cursor query(Uri uri, String[] projection) {
        Cursor cursor;
        try {
            cursor = this.context.getContentResolver().query(uri, projection, null, null, null);
        } catch (Exception e) {
            cursor = null;
            throwAppException(e);
        }
        if (cursor != null || !this.allow) {
            return cursor;
        }
        throw new AppException("query() failed or returned null cursor");
    }

    private Object queryValue(Cursor cursor, int typeIndex, int valueIndex) {
        int type = cursor.getInt(typeIndex);
        switch (type) {
            case 1:
                return cursor.getString(valueIndex);
            case 2:
                return ValueUtils.parseSet(cursor.getString(valueIndex));
            case 3:
                return cursor.getInt(valueIndex);
            case 4:
                return cursor.getLong(valueIndex);
            case 5:
                return cursor.getFloat(valueIndex);
            case 6:
                return cursor.getInt(valueIndex) != 0;
            default:
                throw new AssertionError("Invalid expected type: " + type);
        }
    }

    private Object query(String key, Object defValue, int type) {
        check(key);
        Cursor cursor = query(this.uri.buildUpon().appendPath(key).build(), new String[]{"type", "value"});
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    int indexType = cursor.getColumnIndexOrThrow("type");
                    int i = cursor.getInt(indexType);
                    if (i == 0) {
                        return defValue;
                    }
                    if (i == type) {
                        Object value = queryValue(cursor, indexType, cursor.getColumnIndexOrThrow("value"));
                        cursor.close();
                        return value;
                    }
                    throw new ClassCastException("Preference type mismatch");
                }
            } finally {
                cursor.close();
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return defValue;
    }

    private Map<String, Object> bulkQuery() {
        Cursor query = query(this.uri.buildUpon().appendPath("").build(), new String[]{"key", "type", "value"});
        try {
            HashMap<String, Object> hashMap = new HashMap<>();
            if (query == null) {
                return hashMap;
            }
            int indexKey = query.getColumnIndexOrThrow("key");
            int indexType = query.getColumnIndexOrThrow("type");
            int indexValue = query.getColumnIndexOrThrow("value");
            while (query.moveToNext()) {
                hashMap.put(query.getString(indexKey), queryValue(query, indexType, indexValue));
            }
            query.close();
            return hashMap;
        } finally {
            if (query != null) {
                query.close();
            }
        }
    }

    private void throwAppException(Exception exc) {
        if (this.allow) {
            throw new AppException(exc);
        }
    }

    private static void checkParameter(String name, Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException(name + " is null");
        }
    }

    public boolean bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        try {
            int bulkInsert = this.context.getContentResolver().bulkInsert(uri, contentValuesArr);
            if (bulkInsert == contentValuesArr.length || !this.allow) {
                return bulkInsert == contentValuesArr.length;
            }
            throw new AppException("bulkInsert() failed");
        } catch (Exception e) {
            throwAppException(e);
            return false;
        }
    }

    private static void check(String key) {
        if (TextUtils.isEmpty(key)) {
            throw new IllegalArgumentException("Key is null or empty");
        }
    }

    private boolean containsKey(String key) {
        check(key);
        boolean result = true;
        Cursor query = query(this.uri.buildUpon().appendPath(key).build(), new String[]{"type"});
        if (query != null) {
            try {
                if (query.moveToFirst()) {
                    if (query.getInt(query.getColumnIndexOrThrow("type")) == 0) {
                        result = false;
                    }
                    return result;
                }
            } finally {
                query.close();
            }
        }
        if (query != null) {
            query.close();
        }
        return false;
    }

    @Override
    public boolean contains(String key) {
        return containsKey(key);
    }

    @Override
    public SharedPreferences.Editor edit() {
        return new AppEditor();
    }

    @Override
    public Map<String, ?> getAll() {
        return bulkQuery();
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        return (boolean) query(key, defValue, 6);
    }

    @Override
    public float getFloat(String key, float defValue) {
        return (float) query(key, defValue, 5);
    }

    @Override
    public int getInt(String key, int defValue) {
        return (int) query(key, defValue, 3);
    }

    @Override
    public long getLong(String key, long defValue) {
        return (long) query(key, defValue, 4);
    }

    @Override
    public String getString(String key, String defValue) {
        return (String) query(key, defValue, 1);
    }

    @Override
    @TargetApi(11)
    public Set<String> getStringSet(String str, Set<String> set) {
        return ValueUtils.castSet(query(str, set, 2));
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        checkParameter("listener", onSharedPreferenceChangeListener);
        if (!this.weakHashMap.containsKey(onSharedPreferenceChangeListener)) {
            PrefObserver observer = new PrefObserver(onSharedPreferenceChangeListener);
            this.weakHashMap.put(onSharedPreferenceChangeListener, observer);
            this.context.getContentResolver().registerContentObserver(this.uri, true, observer);
        }
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        checkParameter("listener", onSharedPreferenceChangeListener);
        PrefObserver observer = this.weakHashMap.remove(onSharedPreferenceChangeListener);
        if (observer != null) {
            this.context.getContentResolver().unregisterContentObserver(observer);
        }
    }
}