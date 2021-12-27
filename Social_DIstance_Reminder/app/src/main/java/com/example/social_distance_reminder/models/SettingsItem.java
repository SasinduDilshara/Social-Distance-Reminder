package com.example.social_distance_reminder.models;

public class SettingsItem {

    String distance_threshold;
    int alarm_on;

    public SettingsItem(String distance_threshold, int alarm_on){
        this.distance_threshold = distance_threshold;
        this.alarm_on = alarm_on;
    }

    public String getDistance_threshold() {
        return distance_threshold;
    }

    public boolean getAlarm_on() {
        return  alarm_on == 1;
    }
}
