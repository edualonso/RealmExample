package com.barbasdev.realmexample.weather.repository.realm;

import android.util.Log;

import com.barbasdev.realmexample.persistence.RealmHelper;
import com.barbasdev.realmexample.weather.datamodel.results.WeatherResult;
import com.barbasdev.realmexample.weather.datamodel.search.SearchDictionary;
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
        if (weatherResultRealmProxy == null) {
            WeatherResult weatherResultFromDictionary = queryDictionaryByName(query);
            if (weatherResultFromDictionary != null) {
                weatherResultRealmProxy = weatherResultFromDictionary;
            }
        }
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
    protected WeatherResult extractDataStoreResult(WeatherResult dataStoreResult) {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        return realm.copyFromRealm(dataStoreResult);
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
    protected void updateSearchDictionary(long id, String query) {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());

        SearchDictionary searchDictionary = realm.where(SearchDictionary.class).equalTo(SearchDictionary.KEY_QUERY, query).findFirst();
        String message;
        if (searchDictionary == null) {
            searchDictionary = new SearchDictionary();
            searchDictionary.setQuery(query);
            searchDictionary.setId(id);

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(searchDictionary);
            realm.commitTransaction();
            RealmHelper.closeRealmInstance(Thread.currentThread().getId());

            message = String.format(Locale.getDefault(), "Thread: %s, updated dictionary for query '%s' and id %d.", Thread.currentThread().getName(), query, id);
        } else {
            message = String.format(Locale.getDefault(), "Thread: %s, dictionary not update as the query '%s' already exists for id %d.", Thread.currentThread().getName(), query, id);
        }
        Log.d(TAG, message);
    }

    @Override
    public void deleteWeather() {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        realm.beginTransaction();
        realm.delete(WeatherResult.class);
        realm.commitTransaction();

        String message = String.format(Locale.getDefault(), "Thread: %s, deleted weather results.", Thread.currentThread().getName());
        Log.d(TAG, message);
    }

    @Override
    public void deleteDictionary() {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        realm.beginTransaction();
        realm.delete(SearchDictionary.class);
        realm.commitTransaction();

        String message = String.format(Locale.getDefault(), "Thread: %s, deleted search dictionary.", Thread.currentThread().getName());
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

    private WeatherResult queryDictionaryByName(String query) {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        SearchDictionary searchDictionary = realm.where(SearchDictionary.class).equalTo(SearchDictionary.KEY_QUERY, query).findFirst();
        if (searchDictionary != null) {
            return realm.where(WeatherResult.class).equalTo(WeatherResult.KEY_ID, searchDictionary.getId()).findFirst();
        }
        return null;
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
