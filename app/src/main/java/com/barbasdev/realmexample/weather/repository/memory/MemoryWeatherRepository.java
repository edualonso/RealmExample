package com.barbasdev.realmexample.weather.repository.memory;

import com.barbasdev.realmexample.weather.datamodel.WeatherResult;
import com.barbasdev.realmexample.weather.repository.WeatherRepository;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;

/**
 * Created by Edu on 26/07/2017.
 */
public class MemoryWeatherRepository extends WeatherRepository {

    private static final String TAG = "MemoryWeatherRepository";

    private Map<String, WeatherResult> weatherResults = new LinkedHashMap<>();

    public MemoryWeatherRepository(String url) {
        super(url);
    }

    @Override
    protected ObservableSource<WeatherResult> queryDataStore(String query) {
        WeatherResult item = weatherResults.get(query);
        return Observable.just(item == null ? new WeatherResult() : item);
    }

    @Override
    protected void saveToDataStore(WeatherResult weatherResult) {
        weatherResults.put(weatherResult.getName(), weatherResult);
    }

    @Override
    public void deleteWeather() {
        weatherResults.clear();
    }
}
