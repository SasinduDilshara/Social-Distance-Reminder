package com.example.social_distance_reminder.helper;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.db.crudhelper.model.Stats;
import com.example.social_distance_reminder.exceptions.NotificationManagerException;
import com.example.social_distance_reminder.notifications.MySingleton;
import com.example.social_distance_reminder.ui.PrimeActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.example.social_distance_reminder.helper.RandomIDGenerator.getNotifictionID;
import com.example.social_distance_reminder.db.crudhelper.SqlLiteHelper;

public class NotificationHelper {
    private Context context;

    private static NotificationManager notificationManager = null;

    public NotificationHelper(Context context) {
        this.context = context;
    }

    private static NotificationHelper notificationHelper = null;

    private static boolean isNormalNotificationChannelActive = false;
    private static boolean identifiedNotificationChannelActive = false;
    private static boolean isbackgroundNotificationChannelActive = false;

    private static int normalNotoficationLockScreenVisiblity = Notification.VISIBILITY_PRIVATE;
    private static int normalNotificationLightColor = Color.BLUE;
    private static String normalNotificationID = "NORMAL_NOTIFICATION_CHANNEL_ID";
    private static CharSequence normalNotificationChannelName = "NORMAL_NOTIFICATION_CHANNEL_NAME";
    private static String normalNotificationChannelDescription = "NORMAL_NOTIFICATION_CHANNEL_DESCRIPTION";
    private static int normalNotificationChannelImportance = NotificationManager.IMPORTANCE_DEFAULT;
    private static Uri normalNotificationChannelSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    private static int identifiedNotoficationLockScreenVisiblity = Notification.VISIBILITY_PUBLIC;
    private static int identifiedNotificationLightColor = Color.BLUE;
    private static String identifiedNotificationID = "IDENTIFIED_NOTIFICATION_CHANNEL_ID";
    private static CharSequence identifiedNotificationChannelName = "IDENTIFIED_NOTIFICATION_CHANNEL_NAME";
    private static String identifiedNotificationChannelDescription = "IDENTIFIED_NOTIFICATION_CHANNEL_DESCRIPTION";
    private static int identifiedNotificationChannelImportance = NotificationManager.IMPORTANCE_HIGH;
    private static Uri identifiedNotificationChannelSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    private static int backgroundNotoficationLockScreenVisiblity = Notification.VISIBILITY_PRIVATE;
    private static int backgroundNotificationLightColor = Color.BLUE;
    private static String backgroundNotificationID = "BACKGROUND_NOTIFICATION_CHANNEL_ID";
    private static CharSequence backgroundNotificationChannelName = "BACKGROUND_NOTIFICATION_CHANNEL_NAME";
    private static String backgroundNotificationChannelDescription = "BACKGROUND_NOTIFICATION_CHANNEL_DESCRIPTION";
    private static int backgroundNotificationChannelImportance = NotificationManager.IMPORTANCE_DEFAULT;
    private static Uri backgroundNotificationChannelSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    private NotificationManager createNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) this.context.getSystemService(NotificationManager.class);
        }
        return notificationManager;
    }

    public static NotificationHelper getInstance(Context context) {
        if (notificationHelper == null || notificationHelper.context != context) {
            notificationHelper = new NotificationHelper(context);
        }
        return notificationHelper;
    }

    private void showNormalNotification(String textTitle, String textContent) throws Exception {
        this.createNormalNotificationChannel();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.context, normalNotificationID)
                .setSmallIcon(R.drawable.distanzia_logo_foreground_original)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);
        notificationManager.notify(getNotifictionID(), notificationBuilder.build());
    }

    private void showIdentifiedNotification(String textTitle, String textContent) throws Exception {
        this.createIdentifiedNotificationChannel();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.context, identifiedNotificationID)
                .setSmallIcon(R.drawable.distanzia_logo_foreground_original)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true);
        ;
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);
        notificationManager.notify(getNotifictionID(), notificationBuilder.build());
    }

    private int showBackgroundNotification(String textTitle, String textContent) throws Exception {
        this.createBackgroundNotificationChannel();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.context, backgroundNotificationID)
                .setSmallIcon(R.drawable.distanzia_logo_foreground_original)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);
        int notificationId = getNotifictionID();
        notificationManager.notify(notificationId, notificationBuilder.build());
        return notificationId;
    }

    private Notification showBackgroundNotificationForService(String textTitle, String textContent) throws Exception {
        this.createBackgroundNotificationChannel();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this.context, backgroundNotificationID)
                .setSmallIcon(R.drawable.distanzia_logo_foreground_original)
                .setContentTitle(textTitle)
                .setContentText(textContent)
                .setContentIntent(getPendingIntent())
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);
        int notificationId = getNotifictionID();
        return notificationBuilder.build();
    }

    public static void sendNormalNotification(String textTitle, String textContent, Context context) {
        try {
            getInstance(context).showNormalNotification(textTitle, textContent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendIdentifiedNotification(String textTitle, String textContent, Context context) {
        try {
            getInstance(context).showIdentifiedNotification(textTitle, textContent);
        } catch (Exception e) {
            //TODO: Handle these
            e.printStackTrace();
        }
    }

    public static int createBackgroundNotification(String textTitle, String textContent, Context context) {
        try {
            return getInstance(context).showBackgroundNotification(textTitle, textContent);
        } catch (Exception e) {
            return -1;
        }
    }

    public static Notification createBackgroundNotificationForService(String textTitle, String textContent, Context context) {
        try {
            return getInstance(context).showBackgroundNotificationForService(textTitle, textContent);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void removeBackgroundNotification(int id, Context context) {
        getInstance(context).createNotificationManager().cancel(id);
    }

    private void createNormalNotificationChannel() throws Exception {
        // TODO: Check for versions

        if (isNormalNotificationChannelActive) return;

        NotificationChannel notificationChannel = new NotificationChannel(normalNotificationID, normalNotificationChannelName, normalNotificationChannelImportance);
        notificationChannel.setLightColor(normalNotificationLightColor);
        notificationChannel.setLockscreenVisibility(normalNotoficationLockScreenVisiblity);
        notificationChannel.setDescription(normalNotificationChannelDescription);
        notificationChannel.setSound(normalNotificationChannelSound, null);

        NotificationManager manager = this.createNotificationManager();
        if (manager != null) {
            manager.createNotificationChannel(notificationChannel);
            isNormalNotificationChannelActive = true;
        } else {
            throw new NotificationManagerException("Sound system doesn't Responding. Try again!!");
        }
    }

    private void createIdentifiedNotificationChannel() throws Exception {
        // TODO: Check for versions

        if (identifiedNotificationChannelActive) return;

        NotificationChannel notificationChannel = new NotificationChannel(identifiedNotificationID, identifiedNotificationChannelName, identifiedNotificationChannelImportance);
        notificationChannel.setLightColor(identifiedNotificationLightColor);
        notificationChannel.setLockscreenVisibility(identifiedNotoficationLockScreenVisiblity);
        notificationChannel.setDescription(identifiedNotificationChannelDescription);
        notificationChannel.setSound(identifiedNotificationChannelSound, null);

        NotificationManager manager = this.createNotificationManager();
        if (manager != null) {
            manager.createNotificationChannel(notificationChannel);
            identifiedNotificationChannelActive = true;
        } else {
            throw new NotificationManagerException("Sound system doesn't Responding. Try again!!");
        }
    }

    private void createBackgroundNotificationChannel() throws Exception {
        // TODO: Check for versions

        if (isbackgroundNotificationChannelActive) return;

        NotificationChannel notificationChannel = new NotificationChannel(backgroundNotificationID, backgroundNotificationChannelName, backgroundNotificationChannelImportance);
        notificationChannel.setLightColor(backgroundNotificationLightColor);
        notificationChannel.setLockscreenVisibility(backgroundNotoficationLockScreenVisiblity);
        notificationChannel.setDescription(backgroundNotificationChannelDescription);
        notificationChannel.setSound(backgroundNotificationChannelSound, null);

        NotificationManager manager = this.createNotificationManager();
        if (manager != null) {
            manager.createNotificationChannel(notificationChannel);
            isbackgroundNotificationChannelActive = true;
        } else {
            throw new NotificationManagerException("Sound system doesn't Responding. Try again!!");
        }
    }

    private PendingIntent getPendingIntent() {
        //TODO: Pending activity can change by if conditions or parameters
        Intent intent = new Intent(this.context, PrimeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, 0);
        return pendingIntent;
    }


    final private static String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private static String serverKey = "key=AAAA6NYYD9E:APA91bHkC_6fqKP8rh3_GaCK6mRRSKqBcZRRI7Nb_jzkjuFGgzjMJr38jn3khw7CHFKuJJNXF0l5qAK8jLBxsEO7X4YUTA53dJi4j9GshqPMRHdsKAC7HZKLMO1UuEPTXg8X4bjanAk9\t\n";
    final private static String contentType = "application/json";
    final private static String TAG = "Notification Helper";

    private static JSONObject createNotificationObject(com.example.social_distance_reminder.models.Notification notification, String token) {
        JSONObject notificationObject = new JSONObject();
        JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("title", notification.getTitle());
            notificationBody.put("message", notification.getDescription());
            notificationBody.put("date", notification.getDate());
            notificationBody.put("importance", notification.isImportant());
//                notification.put("to", "cJBFXY1FQzCcqHjmepWc_t:APA91bEaSjUd5wzC0dUc-NOBTTaFIDKv1fBW2ZrN3o5uayfzqRl4RJbkhpo4IEGOOEtZlCF5ZwjmHuXIRYQaz5BtPK1s1l_AAiiqiG1yWWq2KCjS7Oi20Ad2ddTWJC7rM2XtjnP3kYs_");
            notificationObject.put("to", token);
//            notificationObject.put("to", "/topics/All");
            notificationObject.put("data", notificationBody);
        } catch (JSONException e) {
            Log.e("NotificationHelper", "onCreate: " + e.getMessage());
        }
        return notificationObject;
    }

    public static void sendWarningNotification(String msgToken, Context context) {
        com.example.social_distance_reminder.models.Notification declaration = new com.example.social_distance_reminder.models.Notification("WARNING", new Date(), "A person who came near you has declared as a COVID patient", true);
        sendDeclarationNotification(createNotificationObject(declaration,msgToken),context);
    }

    private static void sendDeclarationNotification(JSONObject notification, Context context) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> Log.i(TAG, "onResponse: " + response.toString()),
                error -> {
                    Toast.makeText(context, "Request error", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onErrorResponse: Didn't work");
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

}
