package com.bbeaggoo.poitest.data;

import io.realm.RealmObject;

public class POIData extends RealmObject {
//{"poiId":"5f6b4ce9-2458-4a96-8113-1aef6b74617f",
// "floorId":"SNUH_SEOUL_DH_B2",
// "floorName":{"en":"B2","kr":"지하2층"},
// "floorOrder":-2,
// "attributes":{"elevatorID":"0012312","elevatorVendor":3,"elvatorFloorList":"Mitsubishi"},
// "radius":3,
// "isRestricted":0,
// "name":{"kr":"엘레베이터"},
// "pos":{"x":1006,"y":-1300},
// "theta":0,"type":3},
    private String poiId;

    private String floorId;
    private FloorName floorName;
    private int                 floorOrder;
    private Attribute attributes;
    private int                 radius;
    private int                 isRestricted;
    private Name                name;
    private Position pos;
    private int                 theta;
    private int                 type;
    private boolean             isHome;
    private boolean             isCharger;

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getFloorId() {
        return floorId;
    }

    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }

    public FloorName getFloorName() {
        return floorName;
    }

    public void setFloorName(FloorName floorName) {
        this.floorName = floorName;
    }

    public int getFlooirOrder() {
        return floorOrder;
    }

    public void setFlooirOrder(int flooirOrder) {
        this.floorOrder = flooirOrder;
    }

    public Attribute getAttributes() {
        return attributes;
    }

    public void setAttributes(Attribute attributes) {
        this.attributes = attributes;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public int getIsRestricted() {
        return isRestricted;
    }

    public void setIsRestricted(int isRestricted) {
        this.isRestricted = isRestricted;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public int getTheta() {
        return theta;
    }

    public void setTheta(int theta) {
        this.theta = theta;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isHome() {
        return isHome;
    }

    public void setHome(boolean home) {
        isHome = home;
    }

    public boolean isCharger() {
        return isCharger;
    }

    public void setCharger(boolean charger) {
        isCharger = charger;
    }
}
