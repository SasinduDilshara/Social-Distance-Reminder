package com.example.social_distance_reminder.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.services.NotificationHelperService;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
//        startService(new Intent(this, NotificationHelperService.class));
    }

    public void sendTestNotification(View view) {
        NotificationHelperService.sendNormalNotification("Title", "Description", this);
    }

    public void sendTestIdentifiedNotification(View view) {
        NotificationHelperService.sendIdentifiedNotification("ID Title", "ID Description", this);
    }

    public void redirectToNotificationPage(View view) {
        Intent viewNotificationPage = new Intent(this, ViewNotificationsActivity.class);
        startActivity(viewNotificationPage);
    }
}