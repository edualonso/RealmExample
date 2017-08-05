package com.barbasdev.realmexample.weather;

import android.util.Log;

import com.barbasdev.realmexample.BuildConfig;
import com.barbasdev.realmexample.weather.datamodel.results.WeatherResult;
import com.barbasdev.realmexample.weather.repository.WeatherRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Edu on 24/07/2017.
 */

public class WeatherInteractor implements WeatherContracts.Interactor {

    private static final String TAG = "WeatherInteractor";

    private final WeatherRepository weatherRepository;

    private WeatherContracts.ViewModelCallback viewModelCallback;
    private SimpleDateFormat simpleDateFormat;
    private Disposable getWeatherDisposable;

    public WeatherInteractor(WeatherContracts.ViewModelCallback viewModelCallback) {
        this.viewModelCallback = viewModelCallback;
        weatherRepository = WeatherRepository.Factory.build(BuildConfig.REPOSITORY);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
    }

    @Override
    public void getWeather(String query) {
        viewModelCallback.setText("LOADING...");
        weatherRepository.getWeather(query)
                .debounce(500, TimeUnit.MILLISECONDS)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(getWeatherObserver);
    }

    @Override
    public void deleteWeather() {
        weatherRepository.deleteWeather();
        weatherRepository.deleteDictionary();
        viewModelCallback.setText("DATA DELETED");
    }

    @Override
    public void dispose() {
        if (getWeatherDisposable != null) {
            if (getWeatherDisposable.isDisposed()) {
                return;
            }
            getWeatherDisposable.dispose();
        }
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

            viewModelCallback.setText(String.format("Last update was at\n%s", updateString));
        }

        @Override
        public void onError(@NonNull Throwable e) {
            viewModelCallback.setText(String.format("Error: %s", e.getMessage()));
        }

        @Override
        public void onComplete() {

        }
    };
}
