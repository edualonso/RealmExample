package com.barbasdev.realmexample.weather.repository.memory;

import com.barbasdev.realmexample.weather.datamodel.results.WeatherResult;
import com.barbasdev.realmexample.weather.repository.WeatherRepository;
import com.google.gson.internal.LinkedTreeMap;

import java.util.LinkedHashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Memory repository. Used for quick access to data.
 */
public class MemoryWeatherRepository extends WeatherRepository {

    private static final String TAG = "MemoryWeatherRepository";

    private Map<String, WeatherResult> weatherResults = new LinkedHashMap<>();
    private Map<String, Integer> searchDictionary = new LinkedTreeMap<>();

    public MemoryWeatherRepository(String url) {
        super(url);
    }

    @Override
    protected Observable<WeatherResult> queryDataStoreObservable(String query) {
        WeatherResult item = weatherResults.get(query);
        return Observable.just(item == null ? new WeatherResult() : item);
    }

    @Override
    protected WeatherResult queryDataStore(String name) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    protected Observable<WeatherResult> queryDataStoreObservable(long id) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    protected WeatherResult queryDataStore(long id) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    protected void saveToDataStore(WeatherResult weatherResult) {
        weatherResults.put(weatherResult.getName(), weatherResult);
    }

    @Override
    protected void updateSearchDictionary(long id, String query) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void deleteWeather() {
        weatherResults.clear();
    }

    @Override
    public void deleteDictionary() {
        throw new RuntimeException("Not implemented");
    }
}
