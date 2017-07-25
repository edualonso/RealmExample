package com.barbasdev.realmexample.datalayer;

import android.util.Log;

import com.barbasdev.realmexample.base.BaseRepository;
import com.barbasdev.realmexample.datamodel.WeatherResult;
import com.barbasdev.realmexample.persistence.RealmHelper;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by Edu on 24/07/2017.
 */
public class RealmWeatherRepository extends BaseRepository<WeatherApiService> implements WeatherRepository {

    public static final String URL = "http://api.openweathermap.org/data/2.5/";

    public static final long CACHE_LIFESPAN_MS = 10000;

    private static final String TAG = "RealmWeatherRepository";
    private static final String API_KEY = "75805b09ea06260c9eb71391b785f444";

    public RealmWeatherRepository(String url) {
        super(url, WeatherApiService.class);
    }

    /**
     * This method fetches the weather either locally or from the network (if the cache is expired)
     *
     * @param query
     * @return
     */
    @Override
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

    /**
     * Deletes all weather data present in the database.
     */
    @Override
    public Observable<Void> deleteWeather() {
        return Observable
                .create(new ObservableOnSubscribe<Void>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Void> aVoid) throws Exception {
                        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
                        realm.beginTransaction();
                        realm.delete(WeatherResult.class);
                        realm.commitTransaction();
                        Log.d(TAG, "Thread: " + Thread.currentThread().getName() + ", deleted all data");
                    }
                });
    }

    private Observable<WeatherResult> queryDataStore(String query) {
        WeatherResult weatherResultRealmProxy = queryWeather(query);
        if (weatherResultRealmProxy != null) {
            Log.d(TAG, "Thread: " + Thread.currentThread().getName() + ", queryDataStore: successful with update time " + weatherResultRealmProxy.getUpdateTime());
            return Observable.just(weatherResultRealmProxy);
        } else {
            Log.d(TAG, "Thread: " + Thread.currentThread().getName() + ", queryDataStore: got no result");
            return Observable.empty();
        }
    }

    private WeatherResult queryWeather(String query) {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        return realm.where(WeatherResult.class).equalTo(WeatherResult.SEARCH_ID, query).findFirst();
    }

    private Observable<WeatherResult> queryApiWithSave(String query) {
        return queryApi(query)
                .subscribeOn(Schedulers.io())
                .map(new Function<WeatherResult, WeatherResult>() {
                    @Override
                    public WeatherResult apply(@NonNull WeatherResult weatherResult) throws Exception {
                        saveToDisk(weatherResult);
                        return weatherResult;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(new Consumer<WeatherResult>() {
                    @Override
                    public void accept(WeatherResult weatherResult) throws Exception {
                        WeatherResult weatherResultRealmProxy = queryWeather(weatherResult.getName());
                        weatherResultRealmProxy.addChangeListener(changeListener);

                        Log.d(TAG, "Thread: " + Thread.currentThread().getName() + ", added change listener");
                    }
                });
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

    private void saveToDisk(WeatherResult weatherResult) {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(weatherResult);
        realm.commitTransaction();
        Log.d(TAG, "Thread: " + Thread.currentThread().getName() + ", result saved.");
    }

    private RealmChangeListener<WeatherResult> changeListener = new RealmChangeListener<WeatherResult>() {
        @Override
        public void onChange(WeatherResult weatherResult) {
            Log.d(TAG, "Thread: " + Thread.currentThread().getName() + ", object updated");
        }
    };
}
