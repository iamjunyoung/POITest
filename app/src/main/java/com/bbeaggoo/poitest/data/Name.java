package com.bbeaggoo.poitest.data;

import io.realm.RealmObject;

public class Name extends RealmObject {
    private String kr;

    public String getKr() {
        return kr;
    }

    public void setKo(String ko) {
        this.kr = kr;
    }
}
