package com.bbeaggoo.poitest.data;

import io.realm.RealmObject;

public class FloorName extends RealmObject {
    private String en;
    private String kr;

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getKr() {
        return kr;
    }

    public void setKo(String ko) {
        this.kr = ko;
    }

    @Override
    public String toString() {
        return "FloorName{" +
                "en='" + en +
                ", kr='" + kr +
                '}';
    }
}
