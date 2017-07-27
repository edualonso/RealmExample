package com.barbasdev.realmexample.base;

import com.barbasdev.realmexample.BuildConfig;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Edu on 24/07/2017.
 */

public abstract class BaseRepository<T> {

    protected T apiService;

    /**
     * Constructor for a repository that uses a Retrofit service for network calls.
     * @param url
     * @param serviceClass
     */
    public BaseRepository(String url, Class serviceClass) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
            clientBuilder.addInterceptor(logging);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(clientBuilder.build())
                .build();

        apiService = (T) retrofit.create(serviceClass);              // TODO: inject this using Dagger
    }
}
