package com.barbasdev.realmexample.weather;

import android.databinding.Bindable;

import com.barbasdev.realmexample.BR;
import com.barbasdev.realmexample.base.BaseActivity;
import com.barbasdev.realmexample.base.BaseViewModel;

import java.lang.ref.WeakReference;

/**
 * Created by Edu on 23/07/2017.
 */

public class WeatherViewModel extends BaseViewModel implements WeatherInteractor.Contract {

    private static final String TAG = "WeatherViewModel";

    private final WeatherInteractor interactor;
    private final WeatherRouter router;

    private String text;

    public WeatherViewModel(BaseActivity activity) {
        interactor = new WeatherInteractor(this);
        router = new WeatherRouter(activity);
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

    public void onExitClicked() {
        router.exit();
    }
}
