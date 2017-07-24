package com.barbasdev.realmexample.network;

import android.util.Log;

import com.barbasdev.realmexample.base.BaseRepository;
import com.barbasdev.realmexample.datamodel.WeatherResult;
import com.barbasdev.realmexample.persistence.RealmHelper;

import hu.akarnokd.rxjava.interop.RxJavaInterop;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.realm.Realm;

/**
 * Created by Edu on 24/07/2017.
 */


/**
 * https://medium.com/@Miqubel/caching-with-realm-and-rxjava-80f48c5f5e37
 */










public class WeatherRepository extends BaseRepository<WeatherApiService> {

    public static final String URL = "http://api.openweathermap.org/data/2.5/";

    private static final String TAG = "WeatherRepository";
    private static final String API_KEY = "75805b09ea06260c9eb71391b785f444";
//    private static final long WEATHER_RESULT_ID = 2618425;                               // Copenhagen
    private static final long CACHE_LIFESPAN_MS = 15000;

    public WeatherRepository(String url) {
        super(url, WeatherApiService.class);
    }

    /**
     * This method fetches the weather either locally or from the network (if the cache is expired)
     * @param query
     * @return
     */
    public Observable<WeatherResult> getWeather(String query) {
        Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", queryDataStore");

        Observable.concat(
                queryDataStore(query),
                queryApi(query))
                .filter(new Predicate<WeatherResult>() {
                    @Override
                    public boolean test(@NonNull WeatherResult weatherResult) throws Exception {
                        boolean result;
                        if (weatherResult != null) {
                            long now = System.currentTimeMillis();
                            if (now < weatherResult.getUpdateTime() + CACHE_LIFESPAN_MS) {
                                result = true;
                            } else {
                                result = false;
                            }
                        } else {
                            result = false;
                        }
                        return result;
                    }
                })
                .first(null);
        return apiService.getWeather(API_KEY, query);
    }

    private Observable<WeatherResult> queryDataStore(String query) {
        Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", queryDataStore");

        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        rx.Observable<WeatherResult> weatherResultObservable = realm.where(WeatherResult.class).equalTo(WeatherResult.SEARCH_ID, query).findFirst().asObservable();
        return RxJavaInterop.toV2Observable(weatherResultObservable);
    }

    private Observable<WeatherResult> queryApi(String query) {
        Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", queryApi");

        return apiService.getWeather(API_KEY, query)
                .flatMap(new Function<WeatherResult, ObservableSource<WeatherResult>>() {
                    @Override
                    public ObservableSource<WeatherResult> apply(@NonNull WeatherResult weatherResult) throws Exception {
                        weatherResult.setUpdateTime(System.currentTimeMillis());
                        return Observable.just(weatherResult);
                    }
                });
    }
}
