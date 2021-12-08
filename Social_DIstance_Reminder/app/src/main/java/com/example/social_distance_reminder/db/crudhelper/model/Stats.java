package com.example.social_distance_reminder.db.crudhelper.model;

public class Stats {
    private double minDistance = 0.0;
    public int closeCount = 0;
    public String lastMeetupTime = "";

    public double getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(double minDistance) {
        this.minDistance = minDistance;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "minDistance=" + minDistance +
                ", closeCount=" + closeCount +
                ", lastMeetupTime='" + lastMeetupTime + '\'' +
                ", lastPersonDistance=" + lastPersonDistance +
                '}';
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

    public double getLastPersonDistance() {
        return lastPersonDistance;
    }

    public void setLastPersonDistance(double lastPersonDistance) {
        this.lastPersonDistance = lastPersonDistance;
    }

    public double lastPersonDistance = 0.0;
}
