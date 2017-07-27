package com.barbasdev.realmexample.weather;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.barbasdev.realmexample.R;
import com.barbasdev.realmexample.base.BaseActivity;
import com.barbasdev.realmexample.databinding.ActivityWeatherBinding;
import com.barbasdev.realmexample.persistence.RealmHelper;

public class WeatherActivity extends BaseActivity {

    private static final String TAG = "WeatherActivity";

    private ActivityWeatherBinding binding;
    private WeatherViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_weather);
        setupViewModel();
    }

    @Override
    protected void onDestroy() {
        RealmHelper.closeRealmInstance(Thread.currentThread().getId());
        viewModel.onDestroy();

        super.onDestroy();
    }

    private void setupViewModel() {
        viewModel = new WeatherViewModel(new WeatherRouter(this));             // TODO: inject this instance using Dagger
        binding.setViewModel(viewModel);
    }
}
