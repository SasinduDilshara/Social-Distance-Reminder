package com.example.social_distance_reminder.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.auth.AuthRedirectHandler;
import com.example.social_distance_reminder.auth.FirebaseAuthHelper;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements AuthRedirectHandler {

    EditText phoneText, codeText;
    Button phoneButton, codeButton;
    LinearLayout phoneLayout, codeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneText = findViewById(R.id.txt_Login_phone);
        phoneButton = findViewById(R.id.btn_Login_phone);
        phoneLayout = findViewById(R.id.lnr_Login_phone);

        codeText = findViewById(R.id.txt_Login_code);
        codeButton = findViewById(R.id.btn_Login_code);
        codeLayout = findViewById(R.id.lnr_Login_code);

        phoneLayout.setVisibility(View.VISIBLE);
        codeLayout.setVisibility(View.GONE);
        phoneText.setText("+1 650-555-3434");
        phoneButton.setOnClickListener(this::addPhone);
        codeButton.setOnClickListener(this::verifyPhone);

    }

    @Override
    public void popupVerifyActivity() {
        phoneLayout.setVisibility(View.GONE);
        codeLayout.setVisibility(View.VISIBLE);
    }

    public void verifyPhone(View view) {
        FirebaseAuthHelper.verifyUsingCode(codeText.getText().toString(), this, this);
    }

    public void addPhone(View view) {
        FirebaseAuthHelper.verifyUsingPhoneNumber(phoneText.getText().toString(), this, this);
    }

    public void redirectToHome() {
        Intent homePage = new Intent(this, HomeActivity.class);
        startActivity(homePage);
    }

    @Override
    public void onAuthComplete() {
        Toast.makeText(getApplicationContext(), " YOU SUCCESSFULLY COMPLETED THE AUTHENTICATION ", Toast.LENGTH_SHORT).show();
        redirectToHome();
    }

    @Override
    public void onAuthFail(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


}