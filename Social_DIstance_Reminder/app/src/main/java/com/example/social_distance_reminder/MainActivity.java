package com.example.social_distance_reminder;

import android.content.Intent;
import android.os.Bundle;

import com.example.social_distance_reminder.UI.HomeActivity;
import com.example.social_distance_reminder.UI.LandingActivity;
import com.example.social_distance_reminder.UI.LoginActivity;
import com.example.social_distance_reminder.auth.AuthRedirectHandler;
import com.example.social_distance_reminder.auth.FirebaseAuthHelper;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        redirectToHome();
    }

    private void redirectToHome() {
        Intent nextPage;
        if (FirebaseAuthHelper.getCurrentUser() != null) {
            nextPage = new Intent(this, HomeActivity.class);
        } else {
            nextPage = new Intent(this, LandingActivity.class);
        }
        startActivity(nextPage);
    }
}

