package com.example.social_distance_reminder.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.social_distance_reminder.auth.FirebaseAuthHelper;
import com.example.social_distance_reminder.databinding.ActivityTestBinding;
import com.example.social_distance_reminder.exceptions.BluetoothNotSupportException;
import com.example.social_distance_reminder.helper.NotificationHelper;
import com.example.social_distance_reminder.models.Notification;
import com.example.social_distance_reminder.notifications.MySingleton;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;

import static com.example.social_distance_reminder.services.MacAddressService.getBluetoothMacAddress;
import static com.example.social_distance_reminder.services.MacAddressService.getMacAddress;

public class TestActivity extends AppCompatActivity {
    private static final String TAG = "Testing";
    private static boolean closed = true;
    private int notificatoionId = 0;
    private ActivityTestBinding binding;


    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=AAAA6NYYD9E:APA91bHkC_6fqKP8rh3_GaCK6mRRSKqBcZRRI7Nb_jzkjuFGgzjMJr38jn3khw7CHFKuJJNXF0l5qAK8jLBxsEO7X4YUTA53dJi4j9GshqPMRHdsKAC7HZKLMO1UuEPTXg8X4bjanAk9\t\n";
    final private String contentType = "application/json";

    String NOTIFICATION_TITLE;
    String NOTIFICATION_MESSAGE;
    String TOPIC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTestBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        FirebaseMessaging.getInstance().subscribeToTopic("All");

        binding.testButton.setOnClickListener(v -> {
            TOPIC = "/topics/All"; //topic has to match what the receiver subscribed to
            NOTIFICATION_TITLE = binding.testTitle.getText().toString();
            NOTIFICATION_MESSAGE = binding.testMessage.getText().toString();

            JSONObject notification = new JSONObject();
            JSONObject notifcationBody = new JSONObject();
            try {
                notifcationBody.put("title", NOTIFICATION_TITLE);
                notifcationBody.put("message", NOTIFICATION_MESSAGE);

//                notification.put("to", "cJBFXY1FQzCcqHjmepWc_t:APA91bEaSjUd5wzC0dUc-NOBTTaFIDKv1fBW2ZrN3o5uayfzqRl4RJbkhpo4IEGOOEtZlCF5ZwjmHuXIRYQaz5BtPK1s1l_AAiiqiG1yWWq2KCjS7Oi20Ad2ddTWJC7rM2XtjnP3kYs_");
                notification.put("to", TOPIC);
                notification.put("data", notifcationBody);
            } catch (JSONException e) {
                Log.e(TAG, "onCreate: " + e.getMessage());
            }
            sendNotification(notification);
        });
        //UpdateToken();
    }

    private void sendNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> Log.i(TAG, "onResponse: " + response.toString()),
                error -> {
                    Toast.makeText(getApplicationContext(), "Request error", Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    public void sendTestNotification(View view) {
        NotificationHelper.sendNormalNotification("Title", "Description", this);
    }

    public void sendTestIdentifiedNotification(View view) {
        NotificationHelper.sendIdentifiedNotification("ID Title", "ID Description", this);
    }

    public void redirectToNotificationPage(View view) {
        Intent viewNotificationPage = new Intent(this, ViewNotificationsActivity.class);
        startActivity(viewNotificationPage);
    }

    public void sendTestBackgroundNotification(View view) {
        this.notificatoionId = NotificationHelper.createBackgroundNotification("Background Title", "Background Description", this);
        try {
            System.out.println("\n\nThis is the Mac address : - " + getMacAddress(this) + "\n\n");
            System.out.println("\n\n" + "Bluetooth MAC is :-" + getBluetoothMacAddress() + "\n\n");
        } catch (BluetoothNotSupportException e) {
            e.printStackTrace();
        }
    }

    public void RemoveTestBackgroundNotification(View view) {
        NotificationHelper.removeBackgroundNotification(this.notificatoionId, this);
    }

    public void logout(View view) {
        try {
            FirebaseAuthHelper.logout();
            Intent loginPage = new Intent(this, LoginActivity.class);
            loginPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginPage);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void redirectToBluetoothTestPage(View view) {
        Intent bluetoothTestPage = new Intent(this, BluetoothTestActivity.class);
        startActivity(bluetoothTestPage);
    }

}