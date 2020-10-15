package com.song.xposed.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;

import com.song.xposed.utils.ValueUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * Created by chensongsong on 2020/10/15.
 */
public abstract class PreferencesProvider extends ContentProvider implements SharedPreferences.OnSharedPreferenceChangeListener {

    private final Uri uri;
    private final PrefName[] prefNames;
    private final Map<String, SharedPreferences> map;
    private final UriMatcher uriMatcher;

    private static class Model {

        final String prefName;
        final String key;

        private Model(String prefName, String key) {
            this.prefName = prefName;
            this.key = key;
        }
    }

    public PreferencesProvider(String authorities, PrefName[] prefNames) {
        this.uri = Uri.parse("content://" + authorities);
        this.prefNames = prefNames;
        this.map = new HashMap<>(prefNames.length);
        this.uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        this.uriMatcher.addURI(authorities, "*/", 1);
        this.uriMatcher.addURI(authorities, "*/*", 2);
    }

    public PreferencesProvider(String authorities, String[] prefNames) {
        this(authorities, PrefName.parse(prefNames));
    }

    @Override
    public boolean onCreate() {
        for (PrefName prefName : this.prefNames) {
            Context context = getContext();
            if (context != null) {
                if (prefName.isMatch() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    context = context.createDeviceProtectedStorageContext();
                }
                SharedPreferences sharedPreferences = context.getSharedPreferences(prefName.getPrefName(), Context.MODE_PRIVATE);
                sharedPreferences.registerOnSharedPreferenceChangeListener(this);
                this.map.put(prefName.getPrefName(), sharedPreferences);
            }
        }
        return true;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        if (contentValues == null) {
            return null;
        }
        Model model = getModel(uri);
        String prefName = model.prefName;
        String key = getKey(model, contentValues);
        SharedPreferences.Editor edit = getSharedPreferences(prefName, key, true).edit();
        insertValue(edit, key, contentValues);
        if (edit.commit()) {
            return buildUri(prefName, key);
        }
        return null;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] contentValuesArray) {
        Model model = getModel(uri);
        String prefName = model.prefName;
        if (!isEmpty(model.key)) {
            SharedPreferences.Editor edit = getSharedPreferences(prefName).edit();
            for (ContentValues contentValues : contentValuesArray) {
                String value = getValue(contentValues);
                checkParameter(prefName, value, true);
                insertValue(edit, value, contentValues);
            }
            if (edit.commit()) {
                return contentValuesArray.length;
            }
            return 0;
        }
        throw new IllegalArgumentException("Cannot bulk insert with single key URI");
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        Model model = getModel(uri);
        String prefName = model.prefName;
        String key = model.key;
        SharedPreferences.Editor edit = getSharedPreferences(prefName, key, true).edit();
        if (isEmpty(key)) {
            edit.remove(key);
        } else {
            edit.clear();
        }
        return edit.commit() ? 1 : 0;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Model model = getModel(uri);
        String prefName = model.prefName;
        String key = model.key;
        Map<String, ?> all = getSharedPreferences(prefName, key, false).getAll();
        if (projection == null) {
            projection = new String[]{"key", "type", "value"};
        }
        MatrixCursor matrixCursor = new MatrixCursor(projection);
        if (isEmpty(key)) {
            matrixCursor.addRow(parseValues(projection, key, all.get(key)));
        } else {
            for (Map.Entry next : all.entrySet()) {
                matrixCursor.addRow(parseValues(projection, (String) next.getKey(), next.getValue()));
            }
        }
        return matrixCursor;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        return contentValues == null ? delete(uri, selection, selectionArgs) : insert(uri, contentValues) != null ? 1 : 0;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        PrefName prefName = getPrefName(sharedPreferences);
        Uri uri = buildUri(prefName.getPrefName(), key);
        Context context = getContext();
        if (context != null) {
            if (prefName.isMatch() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                context = context.createDeviceProtectedStorageContext();
            }
            context.getContentResolver().notifyChange(uri, null);
        }
    }

    private Uri buildUri(String prefName, String key) {
        Uri.Builder builder = this.uri.buildUpon().appendPath(prefName);
        if (isEmpty(key)) {
            builder.appendPath(key);
        }
        return builder.build();
    }

    private Model getModel(Uri uri) {
        int match = this.uriMatcher.match(uri);
        if (match == 2 || match == 1) {
            List<String> pathSegments = uri.getPathSegments();
            String prefName = pathSegments.get(0);
            String key = "";
            if (match == 2) {
                key = pathSegments.get(1);
            }
            return new Model(prefName, key);
        }
        throw new IllegalArgumentException("Invalid URI: " + uri);
    }

    private static String getValue(ContentValues contentValues) {
        String key = contentValues.getAsString("key");
        return key == null ? "" : key;
    }

    private String findSharedPreferences(SharedPreferences sharedPreferences) {
        for (Map.Entry<String, SharedPreferences> entry : this.map.entrySet()) {
            if (entry.getValue() == sharedPreferences) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("Unknown preference file");
    }

    private static String getKey(Model model, ContentValues contentValues) {
        String key = model.key;
        String contentKey = getValue(contentValues);
        if (key.length() == 0 || contentKey.length() == 0) {
            return key.length() != 0 ? key : contentKey.length() != 0 ? contentKey : "";
        }
        if (key.equals(contentKey)) {
            return key;
        }
        throw new IllegalArgumentException("Conflicting keys specified in URI and ContentValues");
    }

    private void insertValue(SharedPreferences.Editor editor, String key, ContentValues contentValues) {
        Integer type = contentValues.getAsInteger("type");
        if (type != null) {
            Object value = ValueUtils.castObject(contentValues.get("value"), type);
            if (isEmpty(key)) {
                switch (type) {
                    case 0:
                        editor.remove(key);
                        return;
                    case 1:
                        editor.putString(key, (String) value);
                        return;
                    case 2:
                        editor.putStringSet(key, ValueUtils.castSet(value));
                        return;
                    case 3:
                        editor.putInt(key, (int) value);
                        return;
                    case 4:
                        editor.putLong(key, (long) value);
                        return;
                    case 5:
                        editor.putFloat(key, (float) value);
                        return;
                    case 6:
                        editor.putBoolean(key, (boolean) value);
                        return;
                    default:
                        throw new IllegalArgumentException("Cannot set preference with type " + type);
                }
            } else if (type == 0) {
                editor.clear();
            } else {
                throw new IllegalArgumentException("Attempting to insert preference with null or empty key");
            }
        } else {
            throw new IllegalArgumentException("Invalid or no preference type specified");
        }
    }

    private static boolean isEmpty(String str) {
        return str != null && str.length() != 0;
    }

    private Object[] parseValues(String[] projection, String key, Object value) {
        Object[] objects = new Object[projection.length];
        for (int i = 0; i < objects.length; i++) {
            String field = projection[i];
            if ("key".equals(field)) {
                objects[i] = key;
            } else if ("type".equals(field)) {
                objects[i] = ValueUtils.parseType(value);
            } else if ("value".equals(field)) {
                objects[i] = ValueUtils.castObject(value);
            } else {
                throw new IllegalArgumentException("Invalid column name: " + field);
            }
        }
        return objects;
    }

    private SharedPreferences getSharedPreferences(String key) {
        SharedPreferences sharedPreferences = this.map.get(key);
        if (sharedPreferences != null) {
            return sharedPreferences;
        }
        throw new IllegalArgumentException("Unknown preference file name: " + key);
    }

    private PrefName getPrefName(SharedPreferences sharedPreferences) {
        String key = findSharedPreferences(sharedPreferences);
        for (PrefName prefName : this.prefNames) {
            if (prefName.getPrefName().equals(key)) {
                return prefName;
            }
        }
        throw new IllegalArgumentException("Unknown preference file");
    }

    private void checkParameter(String prefName, String key, boolean z) {
        if (!check(prefName, key, z)) {
            throw new SecurityException("Insufficient permissions to access: " + prefName + "/" + key);
        }
    }

    private SharedPreferences getSharedPreferences(String prefName, String key, boolean z) {
        checkParameter(prefName, key, z);
        return getSharedPreferences(prefName);
    }

    public boolean check(String prefName, String key, boolean z) {
        return true;
    }
}