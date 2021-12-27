package com.example.social_distance_reminder.db.crudhelper.model;

import androidx.annotation.NonNull;

public class DeviceModel {

    private int ID;
    private String UserID;
    private String timeStamp;
    private double latitude;
    private double longitude;
    private int rssi;

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }



    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }



    public int getID() {
        return ID;
    }

    public String getUserID() {
        return UserID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public String toString() {
        return "DeviceModel{" +
                "ID=" + ID +
                ", UserID='" + UserID + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", rssi=" + rssi +
                '}';
    }
}