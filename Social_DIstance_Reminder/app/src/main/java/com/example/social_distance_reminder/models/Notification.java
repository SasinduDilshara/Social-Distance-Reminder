package com.example.social_distance_reminder.models;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Notification {
    private String title;
    private Date date;
    private String description;
    private boolean important;

    public Notification(String title, Date date, String description, boolean important){
        this.title = title;
        this.date = date;
        this.description = description;
        this.important = important;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        Format formatter = new SimpleDateFormat("yyyy/MM/dd");
        return formatter.format(date);
    }

    public String isImportant() {
        return String.valueOf(important);
    }

    public String getDescription() {
        return description;
    }
}
