package com.example.social_distance_reminder.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.social_distance_reminder.auth.AuthRedirectHandler;
import com.example.social_distance_reminder.auth.FirebaseAuthHelper;
import com.example.social_distance_reminder.db.crudhelper.FirebaseCRUDHelper;
import com.example.social_distance_reminder.db.crudhelper.SqlLiteHelper;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.social_distance_reminder.R;

import static com.example.social_distance_reminder.helper.ServiceHelper.generateHash;

public class LoginActivity extends AppCompatActivity implements AuthRedirectHandler {

    EditText phntext, codeText;
    Button addPhone, verifyPhone;
    LinearLayout container, container2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phntext = (EditText)findViewById(R.id.phonenumber);
        addPhone = (Button)findViewById(R.id.addphone);
        container = (LinearLayout)findViewById(R.id.container);

        codeText = (EditText)findViewById(R.id.code);
        verifyPhone = (Button)findViewById(R.id.verify);
        container2 = (LinearLayout)findViewById(R.id.container2);

        codeText.setVisibility(View.INVISIBLE);
        verifyPhone.setVisibility(View.INVISIBLE);

        phntext.setText("+1 650-555-3434");
        codeText.setText("123456");
    }

    public void verifyPhone(View view) {
        FirebaseAuthHelper.verifyUsingCode(codeText.getText().toString(), this, this);
    }

    public void addPhone(View view) {
        FirebaseAuthHelper.verifyUsingPhoneNumber(phntext.getText().toString(), this, this);
    }

    public void redirectToHome() {
        Intent homePage = new Intent(this, HomeActivity.class);
        startActivity(homePage);
    }

    @Override
    public void onAuthComplete(String phonenumber) {
        Toast.makeText(getApplicationContext()," YOU SUCCESSFULLY COMPLETED THE AUTHENTICATION ",Toast.LENGTH_SHORT).show();
        redirectToHome();
        String userId = generateHash(phonenumber,this);
        Context context = this;
        SqlLiteHelper.getInstance(context).insertUserId(userId);
        new FirebaseCRUDHelper().onCreteUser(userId);
    }

    @Override
    public void onAuthFail(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void popupVerifyActivity() {
        codeText.setVisibility(View.VISIBLE);
        verifyPhone.setVisibility(View.VISIBLE);
    }
}