package com.example.social_distance_reminder.models;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Notification {
    private String title;
    private Date date;
    private String description;

    public Notification(String title, Date date, String description){
        this.title = title;
        this.date = date;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        Format formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(date);
    }

    public String getDescription() {
        return description;
    }
}
