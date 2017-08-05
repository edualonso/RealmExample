package com.barbasdev.realmexample.weather.network;

import com.barbasdev.realmexample.weather.datamodel.results.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Edu on 23/07/2017.
 */

public interface WeatherApiService {
    @GET("weather")
    Observable<WeatherResult> getWeather(@Query("appid") String appId, @Query("q") String query);
}
