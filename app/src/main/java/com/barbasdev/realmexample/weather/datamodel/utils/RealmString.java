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

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof RealmString)) {
            return false;
        }

        RealmString tempRealmString = (RealmString) obj;

        return value.equals(tempRealmString.value);
    }
}