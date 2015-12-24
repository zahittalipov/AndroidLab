package com.elegion.hometask3;

import android.content.Context;
import android.content.Loader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Zahit Talipov on 17.11.2015.
 */
public class NLoader extends Loader<String> {
    MyList list;
    ExecutorService executor = Executors.newFixedThreadPool(5);
    StringBuffer result = new StringBuffer();
    int count = 0;
    int completed=0;
    public NLoader(Context context, Bundle bundle) {
        super(context);
        Log.d("myLoader", "create loader " + this.hashCode());
        list = (MyList) bundle.getSerializable("run");
        count = list.size();
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }

    @Override
    protected void onForceLoad() {
        for (Runnable runnable : list) {
            new MyAsyncTask().executeOnExecutor(executor, runnable);
        }
        super.onForceLoad();
    }

    class MyAsyncTask extends AsyncTask<Runnable, String, String> {

        @Override
        protected String doInBackground(Runnable... params) {
            Runnable runnable = params[0];
            runnable.run();
            return String.valueOf(runnable.hashCode());
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            getResult(s);
        }

        public void getResult(String s) {
            result.append("stop runnable " + s + "\n");
            completed++;
            if (count==completed) {
                deliverResult(result.toString());
            }
        }
    }
}
