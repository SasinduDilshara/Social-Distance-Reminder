package com.example.social_distance_reminder.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.social_distance_reminder.UI.HomeActivity;

import static com.example.social_distance_reminder.helper.RandomIDGenerator.getBackgroundNotifictionID;

public class BackgroundServiceHandler extends Service {
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";
    private static boolean stillRunning = false;

    public static boolean isStillRunning() {
        return stillRunning;
    }

    public static void setStillRunning(boolean stillRunning) {
        BackgroundServiceHandler.stillRunning = stillRunning;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void test() {
        int i = 0;
        while (true) {
            System.out.println(i + "\n");
            i += 1;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if ( i == 700) {
                stopForeground(true);
                stillRunning = false;
                break;
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        int value = super.onStartCommand(intent, flags, startId);
        if (!stillRunning) {
            startForeground(getApplicationContext());
            test();
            stillRunning = true;
        }
        return value;
    }

    private void startForeground(Context context) {
        Notification notification = NotificationHelperService
                .createBackgroundNotificationForService("Running","App is running in background", context);
        startForeground(getBackgroundNotifictionID(), notification);
    }
}