package com.bbeaggoo.poitest.data;

import io.realm.RealmObject;

public class Attribute extends RealmObject {
    private String elevatorID;
    //private int elevatorVendor;
    private String elevatorVendor;
    private String elvatorFloorList;
    private String desc;
    private String tel;

    public String getElevatorID() {
        return elevatorID;
    }

    public void setElevatorID(String elevatorID) {
        this.elevatorID = elevatorID;
    }

    public String getElevatorVendor() {
        return elevatorVendor;
    }

    public void setElevatorVendor(String elevatorVendor) {
        this.elevatorVendor = elevatorVendor;
    }

    public String getElvatorFloorList() {
        return elvatorFloorList;
    }

    public void setElvatorFloorList(String elvatorFloorList) {
        this.elvatorFloorList = elvatorFloorList;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "Attribute{" +
                "elevatorID='" + elevatorID  + ", elevatorVendor='" + elevatorVendor +
                ", elvatorFloorList='" + elvatorFloorList +
                ", desc='" + desc +
                ", tel='" + tel +
                '}';
    }
}
