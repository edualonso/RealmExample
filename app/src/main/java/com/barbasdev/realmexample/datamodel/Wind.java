package com.barbasdev.realmexample.datamodel;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Wind extends RealmObject {

    @SerializedName("speed")
    private Float speed;

    @SerializedName("deg")
    private Long deg;

    public Float getSpeed() {
        return speed;
    }

    public void setSpeed(Float speed) {
        this.speed = speed;
    }

    public Long getDeg() {
        return deg;
    }

    public void setDeg(Long deg) {
        this.deg = deg;
    }

}