package com.example.social_distance_reminder.models;


public class DashboardElement {
    private String min_distance;
    private int proximity_device_count;
    private String last_meetup;
    private int declare_count;

    public DashboardElement(String minDistance,int proximity_count, String last_meetup, int dec_count){
        this.min_distance = minDistance;
        this.proximity_device_count = proximity_count;
        this.last_meetup = last_meetup;
        this.declare_count = dec_count;
    }

    public String getProximity_device_count() {
        return String.valueOf(proximity_device_count);
    }

    public String getMin_distance() {
        return min_distance;
    }

    public String getLast_meetup() {
        return last_meetup;
    }

    public String getDeclare_count() {
        return String.valueOf(declare_count);
    }
}
