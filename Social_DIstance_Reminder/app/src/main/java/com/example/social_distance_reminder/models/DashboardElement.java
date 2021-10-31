package com.example.social_distance_reminder.models;


public class DashboardElement {
    private int proximity_device_count;
    private int val1;
    private int val3;
    private int val4;
    private int val5;
    private int val6;

    public DashboardElement(int a,int proximity, int b, int c, int d, int e){
        this.val1 = a;
        this.proximity_device_count = proximity;
        this.val3 = b;
        this.val4 = c;
        this.val5 = d;
        this.val6 = e;
    }

    public String getProximity_device_count() {
        return String.valueOf(proximity_device_count);
    }

    public String getVal1() {
        return String.valueOf(val1);
    }

    public String getVal3() {
        return String.valueOf(val3);
    }

    public String getVal4() {
        return String.valueOf(val4);
    }

    public String getVal5() {
        return String.valueOf(val5);
    }

    public String getVal6() {
        return String.valueOf(val6);
    }
}
