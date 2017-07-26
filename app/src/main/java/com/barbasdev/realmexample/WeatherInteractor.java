package com.barbasdev.realmexample;

import android.util.Log;

import com.barbasdev.realmexample.datalayer.MemoryWeatherRepository;
import com.barbasdev.realmexample.datalayer.WeatherRepository;
import com.barbasdev.realmexample.datamodel.WeatherResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Edu on 24/07/2017.
 */

public class WeatherInteractor {

    public interface Contract {
        void setText(String text);
    }

    private static final String TAG = "WeatherInteractor";

    private final WeatherRepository weatherRepository;

    private Contract weatherContract;
    private SimpleDateFormat simpleDateFormat;
    private Disposable getWeatherDisposable;

    public WeatherInteractor(Contract weatherContract) {
        this.weatherContract = weatherContract;
//        weatherRepository = new RealmWeatherRepository(WeatherRepository.URL);
        weatherRepository = new MemoryWeatherRepository(WeatherRepository.URL);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
    }

    public void getWeather() {
        weatherContract.setText("LOADING...");
        weatherRepository.getWeather("Madrid")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWeatherObserver);
    }

    public void deleteWeather() {
        weatherRepository.deleteWeather();
        weatherContract.setText("DATA DELETED");
    }

    public void disposeAll() {
        if (getWeatherDisposable.isDisposed()) {
            return;
        }
        getWeatherDisposable.dispose();
    }

    private Observer<WeatherResult> getWeatherObserver = new Observer<WeatherResult>() {
        @Override
        public void onSubscribe(@NonNull Disposable d) {
            getWeatherDisposable = d;
        }

        @Override
        public void onNext(@NonNull WeatherResult weatherResult) {
            Long updateTime = weatherResult.getUpdateTime();

            Date date = new Date(updateTime);
            String updateString = simpleDateFormat.format(date);
            Log.d(TAG, "--------> updated on: " + updateString);

            weatherContract.setText(String.format("Last update was at\n%s", updateString));
        }

        @Override
        public void onError(@NonNull Throwable e) {
            weatherContract.setText(String.format("Error: %s", e.getMessage()));
        }

        @Override
        public void onComplete() {

        }
    };
}
