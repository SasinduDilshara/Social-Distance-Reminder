package com.example.social_distance_reminder.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.social_distance_reminder.R;

public class LandingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
    }

    public void redirectToLogin(View view) {
        Intent loginPage = new Intent(this, LoginActivity.class);
        startActivity(loginPage);
    }
}