package com.barbasdev.realmexample;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.barbasdev.realmexample.base.BaseActivity;
import com.barbasdev.realmexample.datamodel.Main;
import com.barbasdev.realmexample.datamodel.WeatherResult;
import com.barbasdev.realmexample.network.WeatherApiService;
import com.barbasdev.realmexample.persistence.RealmHelper;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherActivity extends BaseActivity {

    public static final Long WEATHER_RESULT_ID = 2618425L;                               // Copenhagen
    public static final String API_KEY = "75805b09ea06260c9eb71391b785f444";
    public static final String BASE_URL = "http://api.openweathermap.org/data/2.5/";

    private static final String TAG = "WeatherActivity";
    private Button buttonQuery;
    private Button buttonSave;
    private Button buttonUpdate;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);
        buttonQuery = findViewById(R.id.buttonQuery);
        buttonSave = findViewById(R.id.buttonSave);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        RxView.clicks(buttonQuery).share()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
                        WeatherResult weatherResult = realm.where(WeatherResult.class).equalTo(WeatherResult.KEY_ID, WEATHER_RESULT_ID).findFirst();
//                        Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", query. Temperature: " + weatherResult.getMain().getTemp());
                        Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", query. Name: " + weatherResult.getName());
                    }
                });

        RxView.clicks(buttonSave).share()
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Log.e(TAG, "Thread: " + Thread.currentThread().getName());

                        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
                        OkHttpClient client = new OkHttpClient.Builder()
                                .addInterceptor(logging)
                                .build();

                        Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl(BASE_URL)
                                .addConverterFactory(GsonConverterFactory.create())
                                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                                .client(client)
                                .build();

                        WeatherApiService weatherService = retrofit.create(WeatherApiService.class);
                        weatherService.getWeather(API_KEY, "Copenhagen")
//                                .subscribeOn(Schedulers.io())
                                .subscribeOn(Schedulers.single())
                                .map(new Function<WeatherResult, Boolean>() {
                                    @Override
                                    public Boolean apply(@NonNull WeatherResult weatherResult) throws Exception {
//                                        Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", result received. Temperature: " + weatherResult.getMain().getTemp());
                                        Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", result received. Name: " + weatherResult.getName());

                                        try {
                                            Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
                                            realm.beginTransaction();
                                            realm.copyToRealmOrUpdate(weatherResult);
                                            realm.commitTransaction();

                                            Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", result saved.");
                                        } catch (Exception e) {
                                            return false;
                                        }

                                        return true;
                                    }
                                })
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<Boolean>() {
                                    @Override
                                    public void onSubscribe(@NonNull Disposable d) {
                                        Log.e(TAG, "onSubscribe");
                                    }

                                    @Override
                                    public void onNext(@NonNull Boolean aBoolean) {
                                        Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", adding change listener");

                                        if (aBoolean) {
                                            Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
                                            WeatherResult result = realm.where(WeatherResult.class).equalTo(WeatherResult.KEY_ID, WEATHER_RESULT_ID).findFirst();
                                            result.removeAllChangeListeners();
                                            result.addChangeListener(changeListener);
                                        } else {
                                            Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", something went wrong...");
                                        }
                                    }

                                    @Override
                                    public void onError(@NonNull Throwable e) {
                                        Log.e(TAG, "onError; " + e.getMessage());
                                    }

                                    @Override
                                    public void onComplete() {
                                        Log.e(TAG, "onComplete");
                                    }
                                });
                    }
                });

        RxView.clicks(buttonUpdate).share()
                .observeOn(Schedulers.single())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", updating weather...");

                        Realm realm = RealmHelper.getRealmInstance(Thread.currentThread().getId());
                        realm.beginTransaction();
                        WeatherResult result = realm.where(WeatherResult.class).equalTo(WeatherResult.KEY_ID, WEATHER_RESULT_ID).findFirst();
//                        Main newMain = Main.MainBuilder.with(result.getMain());
//                        newMain.setTemp(666F);
//                        result.setMain(newMain);
                        result.setName("MUAHAHAHAHA");
                        realm.copyToRealmOrUpdate(result);
                        realm.commitTransaction();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        RealmHelper.closeRealmInstances();
    }

    private RealmChangeListener<WeatherResult> changeListener = new RealmChangeListener<WeatherResult>() {
        @Override
        public void onChange(WeatherResult weatherResult) {
//            Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", object updated. Temperature: " + weatherResult.getMain().getTemp());
            Log.e(TAG, "Thread: " + Thread.currentThread().getName() + ", object updated. Name: " + weatherResult.getName());
        }
    };
}
