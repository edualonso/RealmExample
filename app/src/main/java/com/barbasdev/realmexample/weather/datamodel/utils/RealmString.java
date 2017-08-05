package com.barbasdev.realmexample.weather.datamodel.utils;

import io.realm.RealmObject;

public class RealmString extends RealmObject {
    private String value;

    public RealmString() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}