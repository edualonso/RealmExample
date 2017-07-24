package com.barbasdev.realmexample.persistence;

import java.util.LinkedHashMap;
import java.util.Map;

import io.realm.Realm;

/**
 * Created by Edu on 23/07/2017.
 */

public class RealmHelper {

    private static Map<Long, Realm> realmInstances = new LinkedHashMap<>();

    public static Realm getRealmInstance(long threadId) {
        Realm realm = realmInstances.get(threadId);
        if (realm == null) {
            realm = Realm.getDefaultInstance();
            realmInstances.put(Thread.currentThread().getId(), realm);
        }
        return realm;
    }

    public static void closeRealmInstances() {
        for (Realm realm : realmInstances.values()) {
            realm.close();
        }
    }
}
