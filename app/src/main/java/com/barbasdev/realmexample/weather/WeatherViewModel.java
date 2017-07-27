package com.barbasdev.realmexample.weather;

import android.databinding.Bindable;

import com.barbasdev.realmexample.BR;
import com.barbasdev.realmexample.base.BaseViewModel;

/**
 * Created by Edu on 23/07/2017.
 */

public class WeatherViewModel extends BaseViewModel
        implements WeatherContracts.ViewModel, WeatherContracts.ViewModelCallback {

    private static final String TAG = "WeatherViewModel";

    private final WeatherInteractor interactor;
    private final WeatherRouter router;

    private String text;

    public WeatherViewModel(WeatherRouter router) {
        this.interactor = new WeatherInteractor(this);
        this.router = router;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        interactor.dispose();
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

    public void onGetWeatherSimpleClicked() {
        interactor.getWeather("Madrid");
    }

    public void onGetWeatherComplexClicked() {
        interactor.getWeather("Madrid, Spain");
    }

    public void onDeleteWeatherClicked() {
        interactor.deleteWeather();
    }

    public void onExitClicked() {
        router.exit();
    }
}
