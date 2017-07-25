package com.barbasdev.realmexample;

import android.databinding.Bindable;

import com.barbasdev.realmexample.base.BaseViewModel;

/**
 * Created by Edu on 23/07/2017.
 */

public class WeatherViewModel extends BaseViewModel {

    private static final String TAG = "WeatherViewModel";

    private final WeatherModel model;
    private String text;

    public WeatherViewModel() {
        model = new WeatherModel(this);
    }

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    public void onGetWeatherClicked() {
        model.getWeather();
    }

    public void onDeleteWeatherClicked() {
        model.deleteWeather();
    }
}
