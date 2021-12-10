package com.example.social_distance_reminder.models;

public class BlacklistItem {
    String mobile_number;

    public BlacklistItem(String num){
        this.mobile_number = num;
    }

    public String getMobile_number() {
        return mobile_number;
    }
}
