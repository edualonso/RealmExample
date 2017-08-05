package com.barbasdev.realmexample.weather.datamodel.results;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Main extends RealmObject {

    @SerializedName("temp")
    private Float temp;

    @SerializedName("pressure")
    private Float pressure;

    @SerializedName("humidity")
    private Long humidity;

    @SerializedName("temp_min")
    private Float tempMin;

    @SerializedName("temp_max")
    private Float tempMax;

    @SerializedName("sea_level")
    private Float seaLevel;

    @SerializedName("grnd_level")
    private Float grndLevel;

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Float getPressure() {
        return pressure;
    }

    public void setPressure(Float pressure) {
        this.pressure = pressure;
    }

    public Long getHumidity() {
        return humidity;
    }

    public void setHumidity(Long humidity) {
        this.humidity = humidity;
    }

    public Float getTempMin() {
        return tempMin;
    }

    public void setTempMin(Float tempMin) {
        this.tempMin = tempMin;
    }

    public Float getTempMax() {
        return tempMax;
    }

    public void setTempMax(Float tempMax) {
        this.tempMax = tempMax;
    }

    public Float getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(Float seaLevel) {
        this.seaLevel = seaLevel;
    }

    public Float getGrndLevel() {
        return grndLevel;
    }

    public void setGrndLevel(Float grndLevel) {
        this.grndLevel = grndLevel;
    }

}