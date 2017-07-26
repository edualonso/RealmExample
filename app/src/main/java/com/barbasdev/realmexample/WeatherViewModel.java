package com.barbasdev.realmexample;

import android.databinding.Bindable;

import com.barbasdev.realmexample.base.BaseViewModel;

/**
 * Created by Edu on 23/07/2017.
 */

public class WeatherViewModel extends BaseViewModel implements WeatherInteractor.Contract {

    private static final String TAG = "WeatherViewModel";

    private final WeatherInteractor interactor;
    private String text;

    public WeatherViewModel() {
        interactor = new WeatherInteractor(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        interactor.disposeAll();
    }

    @Bindable
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    public void onGetWeatherClicked() {
        interactor.getWeather();
    }

    public void onDeleteWeatherClicked() {
        interactor.deleteWeather();
    }
}
