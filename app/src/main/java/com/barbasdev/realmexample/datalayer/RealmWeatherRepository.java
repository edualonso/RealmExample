package com.barbasdev.realmexample.datalayer;

import android.util.Log;

import com.barbasdev.realmexample.datamodel.WeatherResult;
import com.barbasdev.realmexample.persistence.RealmHelper;

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
    protected Observable<WeatherResult> queryDataStore(String query) {
        WeatherResult weatherResultRealmProxy = queryWeather(query);
        if (weatherResultRealmProxy != null) {
            Log.d(TAG, "Thread: " + Thread.currentThread().getName() + ", queryDataStore: successful with update time " + weatherResultRealmProxy.getUpdateTime());
            return Observable.just(weatherResultRealmProxy);
        } else {
            Log.d(TAG, "Thread: " + Thread.currentThread().getName() + ", queryDataStore: got no result");
            return Observable.empty();
        }
    }

    @Override
    protected void saveToDataStore(WeatherResult weatherResult) {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(weatherResult);
        realm.commitTransaction();
        RealmHelper.closeRealmInstance(Thread.currentThread().getId());
        Log.d(TAG, "Thread: " + Thread.currentThread().getName() + ", result saved.");
    }

    @Override
    public void deleteWeather() {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        realm.beginTransaction();
        realm.delete(WeatherResult.class);
        realm.commitTransaction();
        Log.d(TAG, "Thread: " + Thread.currentThread().getName() + ", deleted all data");
    }

    /**
     * It's executed on the main thread. No need to close realm!
     * @param query
     * @return
     */
    private WeatherResult queryWeather(String query) {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        return realm.where(WeatherResult.class).equalTo(WeatherResult.SEARCH_ID, query).findFirst();
    }
}
