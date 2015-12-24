package com.angelectro.zahittalipov.weather.sync;


import android.accounts.Account;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.content.res.Resources;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.angelectro.zahittalipov.weather.MainActivity;
import com.angelectro.zahittalipov.weather.Parser;
import com.angelectro.zahittalipov.weather.R;
import com.angelectro.zahittalipov.weather.SQLiteContentProvider;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class SyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String url = "http://api.openweathermap.org/data/2.5/weather?q=Kazan,ru&appid=e9308aa6a77df4a99b1038ccf18ca0bf&units=metric";
    private ContentResolver contentResolver;
    private Uri uri = Uri.withAppendedPath(SQLiteContentProvider.WEATHER_CONTENT_URI, "current");

    public SyncAdapter(Context context) {
        super(context, true);
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        Log.d("weather", "update current weather");
        contentResolver = getContext().getContentResolver();
        updateCurrentWeather();
    }

    private void updateCurrentWeather() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            Parser parser = new Parser(connection.getInputStream());
            ContentValues values=parser.parse();
            boolean check=isChanges(values);
            Log.d("weatherResult",String.valueOf(check));
            if(check) {
                sendNotification();
            }
            contentResolver.update(uri, values, null, null);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean isChanges(ContentValues values) {
       // contentResolver.update(uri,values,null,null);
        Cursor cursor=contentResolver.query(uri, null, "abs(temp-?)>?", new String[]{values.getAsString("temp"),"2.0"}, null);
        return cursor.moveToFirst();
    }

    private void sendNotification() {
        Resources resources=getContext().getResources();
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("OpenWeather Message")
                .setContentText(resources.getString(R.string.notificationText))
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
