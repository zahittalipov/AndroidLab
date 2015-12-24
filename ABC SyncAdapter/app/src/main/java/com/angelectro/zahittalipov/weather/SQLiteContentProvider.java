package com.angelectro.zahittalipov.weather;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import java.util.LinkedList;

/**
 * Created by Zahit Talipov on 28.10.2015.
 */
public class SQLiteContentProvider extends ContentProvider {
    static final String DB_NAME = "weathers.db";
    static final int DB_VERSION = 12;
    static final String WEATHER_TABLE = "weather";
    static final String WEATHER_ID = "_id";
    static final String WEATHER_CITY_NAME = "city_name";
    static final String WEATHER_TIME_CALCULATION = "time";
    static final String WEATHER_SPEED_WINT = "speed_wint";
    static final String WEATHER_MAIN = "main";
    static final String WEATHER_DESCRIPTION = "description";
    static final String WEATHER_STATE_COLUMN = "state";
    static final String WEATHER_TEMP = "temp";
    static final String AUTHORITY = "com.angelectro.zahittalipov.weather";
    static final String WEATHER_PATH = "weather";
    public static final Uri WEATHER_CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + WEATHER_PATH);

    static final String WEATHER_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + WEATHER_PATH;

    // одна строка
    static final String WEATHER_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + WEATHER_PATH;

    static final int URI_CURRENT_WEATHER = 1;
    static final int URI_WEATHER = 2;
    private static final String LOG_TAG = "weather";
    private static UriMatcher URI_MATCHER = null;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
        URI_MATCHER.addURI(AUTHORITY, WEATHER_PATH, URI_WEATHER);
        URI_MATCHER.addURI(AUTHORITY, WEATHER_PATH + "/*", URI_CURRENT_WEATHER);
    }


    private DBHelper dbHelper;
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {

        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(LOG_TAG, "query " + uri.toString());
        switch (URI_MATCHER.match(uri)) {
            case URI_CURRENT_WEATHER: {
                if (uri.getLastPathSegment().contains("current")) {
                    Log.d("weather", "current");
                    if (TextUtils.isEmpty(selection)) {
                        selection = WEATHER_STATE_COLUMN + " = ?";
                    } else {
                        selection = selection + " AND " + WEATHER_STATE_COLUMN + "= ?";
                    }
                }
                break;
            }
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        if (selectionArgs != null) {
            String temp[] = new String[selectionArgs.length + 1];
            for (int i = 0; i < selectionArgs.length; i++) {
                temp[i] = selectionArgs[i];
            }
            temp[selectionArgs.length] = uri.getLastPathSegment();
            selectionArgs = temp;
        } else {
            selectionArgs = new String[]{uri.getLastPathSegment()};
        }
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(WEATHER_TABLE, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        Log.d(LOG_TAG, "getType, " + uri.toString());
        switch (URI_MATCHER.match(uri)) {
            case URI_CURRENT_WEATHER: {
                return WEATHER_CONTENT_ITEM_TYPE;
            }
            case URI_WEATHER: {
                return WEATHER_CONTENT_TYPE;
            }
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.d(LOG_TAG, "insert " + uri.toString());
        database = dbHelper.getWritableDatabase();
        long row = database.insert(WEATHER_TABLE, null, values);
        Uri result = ContentUris.withAppendedId(WEATHER_CONTENT_URI, row);
        getContext().getContentResolver().notifyChange(result, null);
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "update " + uri.toString());
        switch (URI_MATCHER.match(uri)) {
            case URI_CURRENT_WEATHER: {
                if (uri.getLastPathSegment().contains("current")) {
                    Log.d("weather", "current");
                    if (TextUtils.isEmpty(selection)) {
                        selection = WEATHER_STATE_COLUMN + "= ?";
                    } else {
                        selection = selection + " AND " + WEATHER_STATE_COLUMN + "= ?";
                    }
                }
                break;
            }
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        if (selectionArgs != null) {
            LinkedList<String> strings = new LinkedList<>();
            strings.toArray(selectionArgs);
            strings.add(uri.getLastPathSegment());
            selectionArgs = (String[]) strings.toArray();
        }
        else {
            selectionArgs = new String[]{uri.getLastPathSegment()};
        }
        int col = database.update(WEATHER_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return col;
    }


    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "create db");
            db.execSQL("create table " + WEATHER_TABLE + "("
                    + WEATHER_ID + " integer primary key autoincrement, "
                    + WEATHER_CITY_NAME + " text, "
                    + WEATHER_DESCRIPTION + " text, "
                    + WEATHER_TIME_CALCULATION + " integer, "
                    + WEATHER_TEMP + " real, "
                    + WEATHER_SPEED_WINT + " real, "
                    + WEATHER_MAIN + " text,"
                    + WEATHER_STATE_COLUMN + " text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (oldVersion != newVersion) {
                db.execSQL("drop table " + WEATHER_TABLE);
                db.execSQL("create table " + WEATHER_TABLE + "("
                        + WEATHER_ID + " integer primary key autoincrement, "
                        + WEATHER_CITY_NAME + " text, "
                        + WEATHER_DESCRIPTION + " text, "
                        + WEATHER_TIME_CALCULATION + " integer, "
                        + WEATHER_TEMP + " real, "
                        + WEATHER_SPEED_WINT + " real, "
                        + WEATHER_MAIN + " text,"
                        + WEATHER_STATE_COLUMN + " text);");
                ContentValues contentValues = new ContentValues();
                contentValues.put(WEATHER_CITY_NAME, "Kazan");
                contentValues.put(WEATHER_TEMP, -1);
                contentValues.put(WEATHER_DESCRIPTION, "overcast clouds");
                contentValues.put(WEATHER_STATE_COLUMN, "current");
                db.insert(WEATHER_TABLE, null, contentValues);
            }
        }
    }
}
