package com.barbasdev.realmexample.weather;

import com.barbasdev.realmexample.base.BaseActivity;

import java.lang.ref.WeakReference;

/**
 * Created by Edu on 26/07/2017.
 */

class WeatherRouter {

    private final WeakReference<BaseActivity> activityWeakReference;

    public WeatherRouter(BaseActivity baseActivity) {
        this.activityWeakReference = new WeakReference<BaseActivity>(baseActivity);
    }

    public void exit() {
        BaseActivity activity = activityWeakReference.get();
        if (activity != null && !activity.isFinishing()) {
            activity.finish();
        }
    }
}
