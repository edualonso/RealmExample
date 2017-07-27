package com.barbasdev.realmexample.weather;

/**
 * Created by edu on 27/07/2017.
 */

public class WeatherContracts {

    public interface ViewModel {
        void onGetWeatherSimpleClicked();

        void onGetWeatherComplexClicked();

        void onDeleteWeatherClicked();

        void onExitClicked();
    }

    public interface ViewModelCallback {
        void setText(String text);
    }

    public interface Interactor {
        void getWeather(String query);

        void deleteWeather();

        void dispose();
    }

    public interface Router {
        void exit();
    }
}
