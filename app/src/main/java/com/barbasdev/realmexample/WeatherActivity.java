package com.barbasdev.realmexample;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.barbasdev.realmexample.base.BaseActivity;
import com.barbasdev.realmexample.databinding.ActivityMainBinding;
import com.barbasdev.realmexample.persistence.RealmHelper;

public class WeatherActivity extends BaseActivity {

    private static final String TAG = "WeatherActivity";

    private ActivityMainBinding binding;
    private WeatherViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setupViewModel();
    }

    @Override
    protected void onDestroy() {
        RealmHelper.closeRealmInstance(Thread.currentThread().getId());
        viewModel.onDestroy();

        super.onDestroy();
    }

    private void setupViewModel() {
        viewModel = new WeatherViewModel();             // TODO: inject this instance using Dagger
        binding.setViewModel(viewModel);
    }
}
