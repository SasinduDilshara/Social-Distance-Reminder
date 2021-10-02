package com.example.social_distance_reminder;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.social_distance_reminder.ui.LoginActivity;
import com.example.social_distance_reminder.auth.FirebaseAuthHelper;
import com.example.social_distance_reminder.ui.PrimeActivity;

import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {

    private ImageView welcomeLogoView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        welcomeLogoView = findViewById(R.id.img_Welcome_logo);
        Animation logo_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.logo_anim);
        welcomeLogoView.setAnimation(logo_anim);

        logo_anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                finish();
                redirectToHome();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void redirectToHome() {
        Intent nextPage;
        if (FirebaseAuthHelper.getCurrentUser() != null) {
            nextPage = new Intent(this, PrimeActivity.class);
        } else {
            nextPage = new Intent(this, LoginActivity.class);
        }
        startActivity(nextPage);
    }


}

