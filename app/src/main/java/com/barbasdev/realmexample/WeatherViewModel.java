package com.barbasdev.realmexample;

import com.barbasdev.realmexample.base.BaseViewModel;

/**
 * Created by Edu on 23/07/2017.
 */

public class WeatherViewModel extends BaseViewModel {

    private static final String TAG = "WeatherViewModel";

    private final WeatherModel model;

    public WeatherViewModel() {
        model = new WeatherModel();
    }

    public void onQueryClicked() {
        model.queryWeather();
    }

    public void onRefreshClicked() {
        model.refreshWeather();
    }

    public void onUpdateClicked() {
        model.updateWeather();
    }
}
