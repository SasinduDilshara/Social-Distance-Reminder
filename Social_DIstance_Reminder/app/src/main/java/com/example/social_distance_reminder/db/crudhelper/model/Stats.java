package com.example.social_distance_reminder.db.crudhelper.model;

public class Stats {
    private String minDistance = "0.0";
    public int closeCount = 0;
    public String lastMeetupTime = "";
    public int numDeclaration = 0;
    public int selctedDistance = 0;
    public int isSoundOn = 0;
    public int isBluetoothOn = 0;

    public Stats(String minDistance, int closeCount, String lastMeetupTime, int numDeclaration, int selctedDistance, int isSoundOn, int isBluetoothOn) {
        this.minDistance = minDistance;
        this.closeCount = closeCount;
        this.lastMeetupTime = lastMeetupTime;
        this.numDeclaration = numDeclaration;
        this.selctedDistance = selctedDistance;
        this.isSoundOn = isSoundOn;
        this.isBluetoothOn = isBluetoothOn;
    }

    public int getIsSoundOn() {
        return isSoundOn;
    }

    public void setIsSoundOn(int isSoundOn) {
        this.isSoundOn = isSoundOn;
    }

    public int getIsBluetoothOn() {
        return isBluetoothOn;
    }

    public void setIsBluetoothOn(int isBluetoothOn) {
        this.isBluetoothOn = isBluetoothOn;
    }

    public String getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(String minDistance) {
        this.minDistance = minDistance;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "minDistance=" + minDistance +
                ", closeCount=" + closeCount +
                ", lastMeetupTime='" + lastMeetupTime + '\'' +
                ", lastPersonDistance=" + numDeclaration +
                '}';
    }

    public int getSelctedDistance() {
        return selctedDistance;
    }

    public void setSelctedDistance(int selctedDistance) {
        this.selctedDistance = selctedDistance;
    }

    public int getCloseCount() {
        return closeCount;
    }

    public void setCloseCount(int closeCount) {
        this.closeCount = closeCount;
    }

    public String getLastMeetupTime() {
        return lastMeetupTime;
    }

    public void setLastMeetupTime(String lastMeetupTime) {
        this.lastMeetupTime = lastMeetupTime;
    }

    public int getNumDeclaration() {
        return numDeclaration;
    }

    public void setNumDeclaration(int numDeclaration) {
        this.numDeclaration = numDeclaration;
    }
}
