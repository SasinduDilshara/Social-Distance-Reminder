package com.example.social_distance_reminder.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.auth.AuthRedirectHandler;
import com.example.social_distance_reminder.auth.FirebaseAuthHelper;
import com.example.social_distance_reminder.db.crudhelper.FirebaseCRUDHelper;
import com.example.social_distance_reminder.db.crudhelper.SqlLiteHelper;

import androidx.appcompat.app.AppCompatActivity;
import static com.example.social_distance_reminder.helper.ServiceHelper.generateHash;

public class LoginActivity extends AppCompatActivity implements AuthRedirectHandler {

    EditText phoneText, codeText1,codeText2,codeText3,codeText4,codeText5,codeText6;
    Button phoneButton, codeButton;
    LinearLayout phoneLayout, codeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneText = findViewById(R.id.txt_Login_phone);
        phoneButton = findViewById(R.id.btn_Login_phone);
        phoneLayout = findViewById(R.id.lnr_Login_phone);

        codeButton = findViewById(R.id.btn_Login_code);
        codeText1 = findViewById(R.id.txt_Login_code1);
        codeText2 = findViewById(R.id.txt_Login_code2);
        codeText3 = findViewById(R.id.txt_Login_code3);
        codeText4 = findViewById(R.id.txt_Login_code4);
        codeText5 = findViewById(R.id.txt_Login_code5);
        codeText6 = findViewById(R.id.txt_Login_code6);
        codeLayout = findViewById(R.id.lnr_Login_code);

        setupOTPText();

        phoneLayout.setVisibility(View.VISIBLE);
        codeLayout.setVisibility(View.GONE);
        phoneText.setText("+1 650-555-3434");
        phoneButton.setOnClickListener(this::addPhone);
        codeButton.setOnClickListener(this::verifyPhone);

    }

    private void setupOTPText() {
        codeText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    codeText2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        codeText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    codeText3.requestFocus();
                }else{
                    codeText1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        codeText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    codeText4.requestFocus();
                }else{
                    codeText2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        codeText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    codeText5.requestFocus();
                }else{
                    codeText3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        codeText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().trim().isEmpty()){
                    codeText6.requestFocus();
                }else{
                    codeText4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        codeText6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().isEmpty()) {
                    codeText5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    String readOTP(){
        String OTP = "";
        String code1 = codeText1.getText().toString().trim();
        String code2 = codeText2.getText().toString().trim();
        String code3 = codeText3.getText().toString().trim();
        String code4 = codeText4.getText().toString().trim();
        String code5 = codeText5.getText().toString().trim();
        String code6 = codeText6.getText().toString().trim();
        if(!code1.isEmpty()) { OTP += code1;}
        if(!code2.isEmpty()) { OTP += code2;}
        if(!code3.isEmpty()) { OTP += code3;}
        if(!code4.isEmpty()) { OTP += code4;}
        if(!code5.isEmpty()) { OTP += code5;}
        if(!code6.isEmpty()) { OTP += code6;}
        return OTP;
    }

    @Override
    public void popupVerifyActivity() {
        phoneLayout.setVisibility(View.GONE);
        codeLayout.setVisibility(View.VISIBLE);
    }

    public void verifyPhone(View view) {
        String OTP = readOTP();
        if(OTP.length() == 6) {
            FirebaseAuthHelper.verifyUsingCode(OTP, this, this);
        }else{
            Toast.makeText(this, "OTP should have 6 numbers", Toast.LENGTH_SHORT).show();
        }
    }

    public void addPhone(View view) {
        FirebaseAuthHelper.verifyUsingPhoneNumber(phoneText.getText().toString(), this, this);
    }

    public void redirectPrime() {
        Intent homePage = new Intent(this, PrimeActivity.class);
        startActivity(homePage);
    }

    public void redirectToHome(View view) {
        Intent homePage = new Intent(this, TestActivity.class);
        startActivity(homePage);
    }

    @Override
    public void onAuthComplete(String phonenumber) {
        Toast.makeText(getApplicationContext()," YOU SUCCESSFULLY COMPLETED THE AUTHENTICATION ",Toast.LENGTH_SHORT).show();
        redirectPrime();
        String userId = generateHash(phonenumber,this);
        Context context = this;
        SqlLiteHelper.getInstance(context).insertUserId(userId);
        new FirebaseCRUDHelper().onCreteUser(userId);
    }

    @Override
    public void onAuthFail(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


}