package com.example.social_distance_reminder.helper;

import java.util.Random;

public class RandomIDGenerator {

    public static Random notificationGenerator = new Random();
    public static Random backgroundNotificationGenerator = new Random();

    public static int getNotifictionID () {
        return notificationGenerator.nextInt();
    }
    public static int getBackgroundNotifictionID () {
        return backgroundNotificationGenerator.nextInt();
    }
}
