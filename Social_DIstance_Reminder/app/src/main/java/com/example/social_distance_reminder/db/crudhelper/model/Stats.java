package com.example.social_distance_reminder.db.crudhelper.model;

public class Stats {
    private String minDistance = "0.0";
    public int closeCount = 0;
    public String lastMeetupTime = "";
    public int numDeclaration = 0;

    public Stats(String minDistance, int closeCount, String lastMeetupTime, int numDeclaration) {
        this.minDistance = minDistance;
        this.closeCount = closeCount;
        this.lastMeetupTime = lastMeetupTime;
        this.numDeclaration = numDeclaration;
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
