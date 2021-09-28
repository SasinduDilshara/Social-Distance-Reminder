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
            if ( i == 500) {
                stopForeground(true);
                stillRunning = false;
                break;
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        if (!stillRunning) {
            startForeground();
            test();
            stillRunning = true;
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startForeground() {
        System.out.println("\n\n\n22");
        String NOTIFICATION_CHANNEL_ID = "com.example.social_distance_reminder";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
//                .setSmallIcon(Icon.CONTENTS_FILE_DESCRIPTOR)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
}