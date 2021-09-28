package com.example.social_distance_reminder;

import android.app.Activity;
import android.os.Bundle;

import com.example.social_distance_reminder.auth.FirebaseAuthHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.social_distance_reminder.databinding.ActivityMainBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements AuthRedirectHandler {

    EditText phntext, codeText;
    Button addPhone, verifyPhone;
    LinearLayout container, container2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phntext = (EditText)findViewById(R.id.phonenumber);
        addPhone = (Button)findViewById(R.id.addphone);
        container = (LinearLayout)findViewById(R.id.container);

        codeText = (EditText)findViewById(R.id.code);
        verifyPhone = (Button)findViewById(R.id.verify);
        container2 = (LinearLayout)findViewById(R.id.container2);

        codeText.setVisibility(View.INVISIBLE);
        verifyPhone.setVisibility(View.INVISIBLE);

        phntext.setText("+1 650-555-3434");

    }

    public void verifyPhone(View view) {
//        Toast.makeText(getApplicationContext(),codeText.getText(),Toast.LENGTH_SHORT).show();
        FirebaseAuthHelper.verifyUsingCode(codeText.getText().toString(), this, this);
    }

    public void addPhone(View view) {
//        Toast.makeText(getApplicationContext(),phntext.getText(),Toast.LENGTH_SHORT).show();
//        this.popupVerifyActivity();
        FirebaseAuthHelper.verifyUsingPhoneNumber(phntext.getText().toString(), this, this);
    }

    @Override
    public void onAuthComplete() {
        Toast.makeText(getApplicationContext()," YOU SUCCESSFULLY COMPLETED THE AUTHENTICATION ",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthFail(String message) {
        Toast.makeText(getApplicationContext(),"YOU FAIL TO CPMPLETE THE AUTHENTICATION",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void popupVerifyActivity() {
        codeText.setVisibility(View.VISIBLE);
        verifyPhone.setVisibility(View.VISIBLE);
    }
}

