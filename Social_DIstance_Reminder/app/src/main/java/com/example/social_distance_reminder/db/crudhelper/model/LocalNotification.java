package com.example.social_distance_reminder.db.crudhelper.model;

public class LocalNotification {
    private int ID;
    private String UserID;
    private String timeStamp;
    private String location;
    private int rssi;

    @Override
    public String toString() {
        return "LocalNotification{" +
                "ID=" + ID +
                ", UserID='" + UserID + '\'' +
                ", timeStamp='" + timeStamp + '\'' +
                ", location='" + location + '\'' +
                ", rssi=" + rssi +
                '}';
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserID() {
        return UserID;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}
