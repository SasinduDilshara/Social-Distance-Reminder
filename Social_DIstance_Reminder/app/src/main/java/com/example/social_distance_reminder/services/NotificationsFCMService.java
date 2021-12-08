package com.example.social_distance_reminder.services;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.auth.FirebaseAuthHelper;
import com.example.social_distance_reminder.db.crudhelper.FirebaseCRUDHelper;
import com.example.social_distance_reminder.db.crudhelper.SqlLiteHelper;
import com.example.social_distance_reminder.models.Notification;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class NotificationsFCMService extends FirebaseMessagingService {
    String TAG = "FCM";
    public String userToken;
    SharedPreferences sharedPreferences;
    String token = null;

    public static void setFCMToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        new FirebaseCRUDHelper().updateMessageToken(token);
                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d("FCM", msg);
                    }
                });
//        FirebaseInstallations.getInstance().getId().addOnCompleteListener(
//                task -> {
//                    if (task.isSuccessful()) {
//                        String token = task.getResult();
//                        new FirebaseCRUDHelper().updateMessageToken(token);
//                    }
//                }
//        );
    }

    public static void setFCMToken(String id) {
        System.out.println("Inside setFCMToken(String id) :- " + id);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        String token = task.getResult();
                        new FirebaseCRUDHelper().updateMessageToken(token, id);
                        // Log and toast
//                        String msg = getString(R.string.msg_token_fmt, token);
//                        Log.d("FCM", msg);
                    }


                });
//        FirebaseInstallations.getInstance().getId().addOnCompleteListener(
//                task -> {
//                    if (task.isSuccessful()) {
//                        String token = task.getResult();
//                        new FirebaseCRUDHelper().updateMessageToken(token);
//                    }
//                }
//        );
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData());
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.default_notification_channel_id));
        String title = remoteMessage.getData().get("title");
        String content = remoteMessage.getData().get("message");
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.distanzia_logo_foreground_original);
        builder.setChannelId(getString(R.string.default_notification_channel_id));

        Notification notification = new Notification(title, Calendar.getInstance().getTime(), content, true);
        SqlLiteHelper.getInstance(getApplicationContext()).addDeclareNotification(notification);

        NotificationManager nm = (NotificationManager) (getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE));
        nm.notify(100, builder.build());
    }

    @Override
    public void onNewToken(@NonNull String s) {
        //TODO: send the token to back end with user
        System.out.println("FCM token is :- " + s);
        Log.d(TAG, "onNewToken: "+ s);
        super.onNewToken(s);
//        if (FirebaseAuthHelper.getCurrentUser() != null) {
//            new FirebaseCRUDHelper().updateMessageToken(s);
//        }
        try {
            new FirebaseCRUDHelper().updateMessageToken(s);
        } catch (Exception ex) {

        }
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

