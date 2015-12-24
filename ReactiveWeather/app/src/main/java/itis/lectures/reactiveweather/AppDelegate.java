package itis.lectures.reactiveweather;

import android.app.Application;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Zahit Talipov on 07.12.2015.
 */
public class AppDelegate extends Application {
    private static Observable<Weather> weatherObservable;

    public static Observable<Weather> getWeatherObservable() {
        return weatherObservable;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("delegate", "delegate");
        Observable<Weather> observable = ApiFactory.weatherFromQuery(MainActivity.getWeatherItems()[0])
                .mergeWith(ApiFactory.weatherFromQuery(MainActivity.getWeatherItems()[1]))
                .mergeWith(ApiFactory.weatherFromQuery(MainActivity.getWeatherItems()[2]))
                .mergeWith(ApiFactory.weatherFromQuery(MainActivity.getWeatherItems()[3]))
                .mergeWith(ApiFactory.weatherFromQuery(MainActivity.getWeatherItems()[4]))
                .mergeWith(ApiFactory.weatherFromQuery(MainActivity.getWeatherItems()[5]))
                .mergeWith(ApiFactory.weatherFromQuery(MainActivity.getWeatherItems()[6]))
                .mergeWith(ApiFactory.weatherFromQuery(MainActivity.getWeatherItems()[7]));
                 weatherObservable = Observable.interval(0,30,TimeUnit.SECONDS).flatMap(i -> observable)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).cache();

    }

}
