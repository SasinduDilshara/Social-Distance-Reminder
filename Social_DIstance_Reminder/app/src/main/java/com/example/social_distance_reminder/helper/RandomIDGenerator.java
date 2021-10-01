package com.example.social_distance_reminder.helper;

import java.util.Random;

public class RandomIDGenerator {

    public static Random notificationIDGenerator = new Random();
    public static Random foregroundIDGenerator = new Random();

    public static int getNotifictionID () {
        return notificationIDGenerator.nextInt();
    }
    public static int getForegroundID () {
        return foregroundIDGenerator.nextInt();
    }
}
