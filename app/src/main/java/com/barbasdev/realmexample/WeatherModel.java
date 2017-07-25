package com.barbasdev.realmexample;

import android.util.Log;

import com.barbasdev.realmexample.datalayer.RealmWeatherRepository;
import com.barbasdev.realmexample.datamodel.WeatherResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by Edu on 24/07/2017.
 */

public class WeatherModel {

    private static final String TAG = "WeatherModel";

    private final RealmWeatherRepository weatherRepository;

    private WeatherViewModel weatherViewModel;
    private SimpleDateFormat simpleDateFormat;

    public WeatherModel(WeatherViewModel weatherViewModel) {
        this.weatherViewModel = weatherViewModel;
        weatherRepository = new RealmWeatherRepository(RealmWeatherRepository.URL);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
    }

    public void getWeather() {
        weatherRepository.getWeather("Madrid")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<WeatherResult>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull WeatherResult weatherResult) {
                        Long updateTime = weatherResult.getUpdateTime();

                        Date date = new Date(updateTime);
                        String updateString = simpleDateFormat.format(date);
                        Log.d(TAG, "--------> updated on: " + updateString);

                        weatherViewModel.setText(String.format("Last update was at\n%s", updateString));
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        weatherViewModel.setText(String.format("Error: %s", e.getMessage()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void deleteWeather() {
        weatherRepository.deleteWeather();
        weatherViewModel.setText("DATA DELETED");
    }
}
