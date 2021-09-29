package com.example.social_distance_reminder.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.services.BackgroundServiceHandler;
import com.example.social_distance_reminder.auth.FirebaseAuthHelper;
import com.example.social_distance_reminder.services.NotificationHelperService;

public class HomeActivity extends AppCompatActivity {
    private static boolean closed = false;
    private int notificatoionId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (!closed) {
//            startService(new Intent(this, BackgroundServiceHandler.class));
            try {
                Thread.sleep(1000);
                closed = true;
//                stopService(new Intent(this, BackgroundServiceHandler.class));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
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

    public void sendTestBackgroundNotification(View view) {
        this.notificatoionId = NotificationHelperService.createBackgroundNotification("Background Title", "Background Description", this);
    }

    public void RemoveTestBackgroundNotification(View view) {
        NotificationHelperService.removeBackgroundNotification(this.notificatoionId, this);
    }

    public void logout(View view) {
        try {
            FirebaseAuthHelper.logout();
            Intent landingPage = new Intent(this, LandingActivity.class);
            landingPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(landingPage);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
}