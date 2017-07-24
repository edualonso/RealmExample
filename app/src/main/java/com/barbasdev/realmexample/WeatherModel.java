package com.barbasdev.realmexample;

import android.support.annotation.NonNull;
import android.util.Log;

import com.barbasdev.realmexample.datamodel.WeatherResult;
import com.barbasdev.realmexample.network.WeatherApiClient;
import com.barbasdev.realmexample.network.WeatherApiService;
import com.barbasdev.realmexample.persistence.RealmHelper;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Edu on 24/07/2017.
 */

public class WeatherModel {

    private static final String TAG = "WeatherModel";

    private static final Long WEATHER_RESULT_ID = 2618425L;                               // Copenhagen

    private final WeatherApiClient weatherApiClient;

    // TODO: consider using the repository pattern
    public WeatherModel() {
        weatherApiClient = new WeatherApiClient(WeatherApiClient.URL);
    }

    public void queryWeather() {
        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        WeatherResult weatherResult = realm.where(WeatherResult.class).equalTo(WeatherResult.KEY_ID, WEATHER_RESULT_ID).findFirst();

        Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", query. Name: " + weatherResult.getName());
    }

    public void refreshWeather() {
        Log.e(TAG, "Thread: " + Thread.currentThread().getName());

        weatherApiClient.getWeather("Copenhagen")
                .subscribeOn(Schedulers.io())
                .map(new Function<WeatherResult, Boolean>() {
                    @Override
                    public Boolean apply(@NonNull WeatherResult weatherResult) throws Exception {
                        Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", result received. Name: " + weatherResult.getName());

                        try {
                            Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
                            realm.beginTransaction();
                            realm.copyToRealmOrUpdate(weatherResult);
                            realm.commitTransaction();

                            Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", result saved.");
                        } catch (Exception e) {
                            return false;
                        }

                        return true;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.e(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Boolean aBoolean) {
                        Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", adding change listener");

                        if (aBoolean) {
                            Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
                            WeatherResult result = realm.where(WeatherResult.class).equalTo(WeatherResult.KEY_ID, WEATHER_RESULT_ID).findFirst();
                            result.removeAllChangeListeners();
                            result.addChangeListener(changeListener);
                        } else {
                            Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", something went wrong...");
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError; " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "onComplete");
                    }
                });
    }

    public void updateWeather() {
        Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", updating weather...");

        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
        realm.beginTransaction();
        WeatherResult result = realm.where(WeatherResult.class).equalTo(WeatherResult.KEY_ID, WEATHER_RESULT_ID).findFirst();

        result.setName("MUAHAHAHAHA");
        realm.copyToRealmOrUpdate(result);
        realm.commitTransaction();
    }

    private RealmChangeListener<WeatherResult> changeListener = new RealmChangeListener<WeatherResult>() {
        @Override
        public void onChange(WeatherResult weatherResult) {
            Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", object updated. Name: " + weatherResult.getName());
        }
    };
}
