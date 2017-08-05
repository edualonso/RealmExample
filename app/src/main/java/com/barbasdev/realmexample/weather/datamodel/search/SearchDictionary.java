package com.barbasdev.realmexample.weather.datamodel.search;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by edu on 05/08/2017.
 */

public class SearchDictionary extends RealmObject {

    public static final String KEY_QUERY = "query";

    @PrimaryKey
    private String query;
    private Long id;

    public SearchDictionary() {

    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}












//package com.barbasdev.realmexample.weather.datamodel.search;
//
//import com.barbasdev.realmexample.weather.datamodel.utils.RealmString;
//
//import io.realm.RealmList;
//import io.realm.RealmObject;
//import io.realm.annotations.PrimaryKey;
//
///**
// * Created by edu on 05/08/2017.
// */
//
//public class SearchDictionary extends RealmObject {
//
//    public static final String KEY_ID = "id";
//
//    @PrimaryKey
//    private Long id;
//    private RealmList<RealmString> queries = new RealmList<>();
//
//    public SearchDictionary() {
//
//    }
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }
//
//    public RealmList<RealmString> getQueries() {
//        return queries;
//    }
//
//    public void addQuery(String query) {
//        RealmString realmString = new RealmString();
//        realmString.setValue(query);
//
//        if (!queries.contains(realmString)) {
//            queries.add(realmString);
//        }
//    }
//}
