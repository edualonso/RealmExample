package com.barbasdev.realmexample.network;

import com.barbasdev.realmexample.datamodel.WeatherResult;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Edu on 23/07/2017.
 */

public interface WeatherApiService {

    // http://samples.openweathermap.org/data/2.5/weather?
    //  q=London,uk&
    //  appid=b1b15e88fa797225412429c1c50c122a1

    @GET("weather")
    Observable<WeatherResult> getWeather(@Query("appid") String appId, @Query("q") String query);
}
