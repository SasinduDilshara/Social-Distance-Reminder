package com.example.social_distance_reminder.services;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.models.Notification;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.type.Date;

import org.json.JSONObject;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class NotificationsFCMService extends FirebaseMessagingService {
    String TAG = "FCM";
    public String userToken;
    SharedPreferences sharedPreferences;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id));
        notifyFragment(remoteMessage.getData());

        builder.setContentTitle(remoteMessage.getData().get("title"));
        builder.setContentText(remoteMessage.getData().get("message"));
        builder.setSmallIcon(R.drawable.distanzia_logo_foreground_original);
        builder.setChannelId(getString(R.string.default_notification_channel_id));

        NotificationManager nm = (NotificationManager) (getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE));
        nm.notify(100, builder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        Log.d(TAG, "onNewToken: "+s);
        //TODO: send the token to back end with user
        super.onNewToken(s);
    }

    private void notifyFragment(Map<String,String> object){
        Log.d(TAG, "notifyFragment:broacast sent1");
        Intent intent = new Intent("ReceiveNotification");
        Bundle bundle = new Bundle();
        bundle.putString("title", object.get("title"));
        bundle.putString("message", object.get("message"));
        bundle.putString("date", object.get("date"));
        bundle.putString("importance", object.get("importance"));
        intent.putExtras(bundle);
        Log.d(TAG, "notifyFragment:broacast sent2");
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d(TAG, "notifyFragment:broacast sent");
    }
}

