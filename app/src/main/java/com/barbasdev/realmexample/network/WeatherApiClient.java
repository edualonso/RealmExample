package com.barbasdev.realmexample.network;

import com.barbasdev.realmexample.base.BaseApiClient;
import com.barbasdev.realmexample.datamodel.WeatherResult;

import io.reactivex.Observable;

/**
 * Created by Edu on 24/07/2017.
 */

public class WeatherApiClient extends BaseApiClient<WeatherApiService> {

    public static final String URL = "http://api.openweathermap.org/data/2.5/";

    private static final String API_KEY = "75805b09ea06260c9eb71391b785f444";

    public WeatherApiClient(String url) {
        super(url, WeatherApiService.class);
    }

    public Observable<WeatherResult> getWeather(String query) {
        return apiService.getWeather(API_KEY, query);
    }
}
