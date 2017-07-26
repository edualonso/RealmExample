package com.barbasdev.realmexample.datalayer;

import android.util.Log;

import com.barbasdev.realmexample.base.BaseRepository;
import com.barbasdev.realmexample.datamodel.WeatherResult;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Edu on 25/07/2017.
 */

public abstract class WeatherRepository extends BaseRepository<WeatherApiService> {

    public static final String URL = "http://api.openweathermap.org/data/2.5/";

    public static final long CACHE_LIFESPAN_MS = 5000;

    private static final String API_KEY = "75805b09ea06260c9eb71391b785f444";

    private static final String TAG = "WeatherRepository";

    public WeatherRepository(String url) {
        super(url, WeatherApiService.class);
    }

    /**
     * Fetches a weather result from the repository's datastore.
     * @param query
     * @return
     */
    protected abstract ObservableSource<WeatherResult> queryDataStore(String query);

    /**
     * It's executed on an IO thread. Remember to close realm once done!
     * @param weatherResult
     */
    protected abstract void saveToDataStore(WeatherResult weatherResult);

    /**
     * Deletes all weather data present in the database. It's executed on the main thread.
     * No need to close realm!
     */
    public abstract void deleteWeather();

    /**
     * This method fetches the weather either locally or from the network (if the cache is expired)
     *
     * @param query
     * @return
     */
    public Observable<WeatherResult> getWeather(String query) {
        return Observable.concat(
                queryDataStore(query),
                queryApiWithSave(query))
                .filter(new Predicate<WeatherResult>() {
                    @Override
                    public boolean test(@NonNull WeatherResult weatherResult) throws Exception {
                        boolean usable = weatherResult.isUsable();
                        Log.d(TAG, "Thread: " + Thread.currentThread().getName() + ", is data usable? " + usable);

                        return usable;
                    }
                })
                .first(new WeatherResult())
                .toObservable();
    }

    private Observable<WeatherResult> queryApiWithSave(String query) {
        return queryApi(query)
                .subscribeOn(Schedulers.io())
                .map(new Function<WeatherResult, WeatherResult>() {
                    @Override
                    public WeatherResult apply(@NonNull WeatherResult weatherResult) throws Exception {
                        saveToDataStore(weatherResult);
                        return weatherResult;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<WeatherResult> queryApi(String query) {
        return apiService.getWeather(API_KEY, query)
                .subscribeOn(Schedulers.io())
                .flatMap(new Function<WeatherResult, ObservableSource<WeatherResult>>() {
                    @Override
                    public ObservableSource<WeatherResult> apply(@NonNull WeatherResult weatherResult) throws Exception {
                        Log.d(TAG, "Thread: " + Thread.currentThread().getName() + ", queryApi successful");
                        weatherResult.setUpdateTime(System.currentTimeMillis());

                        return Observable.just(weatherResult);
                    }
                });
    }


}
