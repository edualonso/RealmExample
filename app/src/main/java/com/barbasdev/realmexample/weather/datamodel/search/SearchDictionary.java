package com.barbasdev.realmexample.weather.datamodel.search;

import com.barbasdev.realmexample.weather.datamodel.utils.RealmString;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by edu on 05/08/2017.
 */

public class SearchDictionary extends RealmObject {

    @PrimaryKey
    private Long id;
    private RealmList<RealmString> searchValues;

    public SearchDictionary() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RealmList<RealmString> getSearchValues() {
        return searchValues;
    }

    public void setSearchValues(RealmList<RealmString> searchValues) {
        this.searchValues = searchValues;
    }
}
