package com.example.social_distance_reminder.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.services.BackgroundServiceHandler;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        startService(new Intent(this, BackgroundServiceHandler.class));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        while(true) {
//            if (!(BackgroundServiceHandler.isStillRunning())) {
//                stopService(new Intent(this, BackgroundServiceHandler.class));
//                break;
//            }
//        }
    }
}