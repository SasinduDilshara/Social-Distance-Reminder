package com.example.social_distance_reminder.models;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DashboardElement {
    private String min_distance;
    private int proximity_device_count;
    private String last_meetup;
    private int declare_count;


    public DashboardElement(String minDistance,int proximity_count, String last_meetup, int dec_count){

        if(minDistance.contains(".")){
            int index = minDistance.indexOf('.');
            minDistance = minDistance.substring(0,3);
        }

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd\nhh:mm");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        if(!last_meetup.equals("-")) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(last_meetup));
            last_meetup = formatter.format(calendar.getTime());
        }
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
