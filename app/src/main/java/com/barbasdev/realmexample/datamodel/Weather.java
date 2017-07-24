package com.barbasdev.realmexample.datamodel;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Weather extends RealmObject {

    @SerializedName("id")
    @PrimaryKey
    private Long id;

    @SerializedName("main")

    private String main;

    @SerializedName("description")
    private String description;

    @SerializedName("icon")
    private String icon;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

}