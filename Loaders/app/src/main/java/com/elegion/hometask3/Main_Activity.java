package com.elegion.hometask3;

import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class Main_Activity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    public static final int LOADER_ID = 1;
    private EditText countTV = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countTV = (EditText) findViewById(R.id.countET);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        Loader<String> loader = null;
        if (id == LOADER_ID) {
            loader = new NLoader(this, args);
        }
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
               if(loader.getId()==LOADER_ID)
               {
                   Log.d("ApplicationResult",data);
               }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }


    public void goStartRunnables(View view) {
        Log.d("myLoader","click");
        int count = Integer.parseInt(String.valueOf(countTV.getText()));
        final Random random = new Random();
        MyList runnables = new MyList();
        for (int i = 0; i < count; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < random.nextInt(10); i++) {
                        Log.d("run", this.hashCode()+ "sleep");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
            runnables.add(runnable);
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("run", runnables);
        Loader<String> loader = getLoaderManager().initLoader(LOADER_ID, bundle, this);
        loader.forceLoad();
    }
}
