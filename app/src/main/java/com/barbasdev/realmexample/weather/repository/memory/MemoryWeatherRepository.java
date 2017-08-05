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
    private Map<String, Long> searchDictionary = new LinkedTreeMap<>();

    public MemoryWeatherRepository(String url) {
        super(url);
    }

    @Override
    protected Observable<WeatherResult> queryDataStoreObservable(String query) {
        WeatherResult item = weatherResults.get(query);
        if (item == null) {
            WeatherResult itemFromDictionary = queryDictionaryByName(query);
            if (itemFromDictionary != null) {
                return Observable.just(itemFromDictionary);
            }
            return Observable.empty();
        } else {
            return Observable.just(item);
        }
    }

    @Override
    protected WeatherResult queryDataStore(String name) {
        return weatherResults.get(name);
    }

    @Override
    protected Observable<WeatherResult> queryDataStoreObservable(long id) {
        WeatherResult weatherResult = queryDataStore(id);
        if (weatherResult != null) {
            return Observable.just(weatherResult);
        } else {
            return Observable.empty();
        }
    }

    @Override
    protected WeatherResult queryDataStore(long id) {
        for (WeatherResult weatherResult : weatherResults.values()) {
            if (weatherResult.getId().equals(id)) {
                return weatherResult;
            }
        }
        return null;
    }

    @Override
    protected WeatherResult extractDataStoreResult(WeatherResult dataStoreResult) {
        return dataStoreResult;
    }

    @Override
    protected void saveToDataStore(WeatherResult weatherResult) {
        weatherResults.put(weatherResult.getName(), weatherResult);
    }

    @Override
    protected void updateSearchDictionary(long id, String query) {
        if (searchDictionary.get(query) == null) {
            searchDictionary.put(query, id);
        }
    }

    @Override
    public void deleteWeather() {
        weatherResults.clear();
    }

    @Override
    public void deleteDictionary() {
        searchDictionary.clear();
    }

    private WeatherResult queryDictionaryByName(String query) {
        Long id = searchDictionary.get(query);
        for (WeatherResult weatherResult : weatherResults.values()) {
            if (weatherResult.getId().equals(id)) {
                return weatherResult;
            }
        }
        return null;
    }
}
