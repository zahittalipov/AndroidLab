package itis.lectures.reactiveweather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import rx.Subscriber;
import rx.Subscription;

public class MainActivity extends AppCompatActivity {

    private static final String[] WEATHER_ITEMS = {
            "Kazan", "Moscow", "Paris", "London",
            "Washington", "Madrid", "Rome", "Berlin"
    };

    private WeatherAdapter mWeatherAdapter;
    private Subscription subscription;

    public static String[] getWeatherItems() {
        return WEATHER_ITEMS;
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("main", "main");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mWeatherAdapter = new WeatherAdapter(this);
        recyclerView.setAdapter(mWeatherAdapter);
        }

    @Override
    protected void onStart() {
        super.onStart();
        Subscriber<Weather> subscriber = new Subscriber<Weather>() {
            @Override
            public void onCompleted() {
                Log.d("subs", "completed");
            }

            @Override
            public void onError(Throwable e) {
                Log.d("subs", "error");
            }

            @Override
            public void onNext(Weather weather) {
                Log.d("subs", "next");
                mWeatherAdapter.add(weather);
            }
        };
        subscription = AppDelegate.getWeatherObservable().subscribe(subscriber);

    }
    //TODO : add weather forecast for all weather; use any rx architecture you like


    @Override
    protected void onDestroy() {
        super.onDestroy();
        subscription.unsubscribe();
    }
}
