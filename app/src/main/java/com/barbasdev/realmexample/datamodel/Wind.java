package com.barbasdev.realmexample.datamodel;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Wind extends RealmObject {

    @SerializedName("speed")
    private Float speed;

    @SerializedName("deg")
    private Float deg;

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Float getDeg() {
        return deg;
    }

    public void setDeg(Float deg) {
        this.deg = deg;
    }

}