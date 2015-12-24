package com.angelectro.zahittalipov.weather;

import android.content.ContentValues;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Zahit Talipov on 02.11.2015.
 */
public class Parser {
    String jsonStr;

    public Parser(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuffer buffer = new StringBuffer();
        while ((jsonStr=reader.readLine())!=null){
            buffer.append(jsonStr);
        }
        jsonStr=buffer.toString();
    }

    public ContentValues parse() throws JSONException {
        Log.d("weatherParse",jsonStr);
        JSONObject jsonObject= new JSONObject(jsonStr);
        String desc = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
        String name = jsonObject.getString("name");
        double temp = jsonObject.getJSONObject("main").getDouble("temp");
        ContentValues contentValues= new ContentValues();
        contentValues.put(SQLiteContentProvider.WEATHER_CITY_NAME,name);
        contentValues.put(SQLiteContentProvider.WEATHER_DESCRIPTION,desc);
        contentValues.put(SQLiteContentProvider.WEATHER_TEMP,temp);
        contentValues.put(SQLiteContentProvider.WEATHER_STATE_COLUMN,"current");
        return contentValues;
        }
        }
