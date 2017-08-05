package com.barbasdev.realmexample.weather.repository.realm;

import android.util.Log;

import com.barbasdev.realmexample.persistence.RealmHelper;
import com.barbasdev.realmexample.weather.datamodel.results.WeatherResult;
import com.barbasdev.realmexample.weather.repository.WeatherRepository;

import java.util.Locale;

import io.reactivex.Observable;
import io.realm.Realm;

/**
 * Created by Edu on 24/07/2017.
 */
public class RealmWeatherRepository extends WeatherRepository {

    private static final String TAG = "RealmWeatherRepository";

    public RealmWeatherRepository(String url) {
        super(url);
    }

    @Override
    protected Observable<WeatherResult> queryDataStoreObservable(String query) {
        WeatherResult weatherResultRealmProxy = queryWeatherByName(query);
        return emitWeatherResultObservable(weatherResultRealmProxy);
    }

    @Override
    protected WeatherResult queryDataStore(String query) {
        return queryWeatherByName(query);
    }

    @Override
    protected Observable<WeatherResult> queryDataStoreObservable(long id) {
        WeatherResult weatherResultRealmProxy = queryWeatherById(id);
        return emitWeatherResultObservable(weatherResultRealmProxy);
    }

    @Override
    protected WeatherResult queryDataStore(long id) {
        return queryWeatherById(id);
    }

    @Override
    protected void saveToDataStore(WeatherResult weatherResult) {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(weatherResult);
        realm.commitTransaction();
        RealmHelper.closeRealmInstance(Thread.currentThread().getId());

        String message = String.format(Locale.getDefault(), "Thread: %s, result saved.", Thread.currentThread().getName());
        Log.d(TAG, message);
    }

    @Override
    public void deleteWeather() {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        realm.beginTransaction();
        realm.delete(WeatherResult.class);
        realm.commitTransaction();

        String message = String.format(Locale.getDefault(), "Thread: %s, deleted all data.", Thread.currentThread().getName());
        Log.d(TAG, message);
    }

    /**
     * It's executed on the main thread. No need to close realm!
     * @param query
     * @return
     */
    private WeatherResult queryWeatherByName(String query) {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        return realm.where(WeatherResult.class).equalTo(WeatherResult.KEY_NAME, query).findFirst();
    }

    /**
     * It's executed on the main thread. No need to close realm!
     * @param id
     * @return
     */
    private WeatherResult queryWeatherById(long id) {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        return realm.where(WeatherResult.class).equalTo(WeatherResult.KEY_ID, id).findFirst();
    }

    private Observable<WeatherResult> emitWeatherResultObservable(WeatherResult weatherResultRealmProxy) {
        if (weatherResultRealmProxy != null) {
            String message = String.format(Locale.getDefault(), "Thread: %s, queryDataStoreObservable: successful with update time %d", Thread.currentThread().getName(), weatherResultRealmProxy.getUpdateTime());
            Log.d(TAG, message);
            return Observable.just(weatherResultRealmProxy);
        } else {
            String message = String.format(Locale.getDefault(), "Thread: %s, queryDataStoreObservable: got no result", Thread.currentThread().getName());
            Log.d(TAG, message);
            return Observable.empty();
        }
    }
}
