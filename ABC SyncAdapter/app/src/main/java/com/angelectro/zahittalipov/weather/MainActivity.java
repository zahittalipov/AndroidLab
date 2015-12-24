package com.angelectro.zahittalipov.weather;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.angelectro.zahittalipov.weather.sync.SyncService;

public class MainActivity extends AppCompatActivity {


    ListView listView;
    Uri uri = Uri.withAppendedPath(SQLiteContentProvider.WEATHER_CONTENT_URI, "current");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContentResolver.addPeriodicSync(AppDelegate.sAccount, AppDelegate.AUTHORITY, Bundle.EMPTY, 1L);
        listView = (ListView) findViewById(R.id.rootlistView);
        String[] from = {SQLiteContentProvider.WEATHER_CITY_NAME,
                SQLiteContentProvider.WEATHER_TEMP,
                SQLiteContentProvider.WEATHER_DESCRIPTION};
        int[] to = {R.id.cityTV, R.id.tempTV, R.id.desctv};
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.show_current_weather, cursor, from, to, 0);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
