package com.barbasdev.realmexample.datalayer;

import com.barbasdev.realmexample.datamodel.WeatherResult;

import io.reactivex.Observable;

/**
 * Created by Edu on 25/07/2017.
 */

public interface WeatherRepository {
    Observable<WeatherResult> getWeather(String query);
    Observable<Void> deleteWeather();
}
