package com.example.social_distance_reminder.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.auth.AuthRedirectHandler;
import com.example.social_distance_reminder.auth.FirebaseAuthHelper;
import com.example.social_distance_reminder.databinding.ActivityLoginBinding;
import com.example.social_distance_reminder.databinding.PopupTermsBinding;
import com.example.social_distance_reminder.db.crudhelper.FirebaseCRUDHelper;
import com.example.social_distance_reminder.db.crudhelper.SqlLiteHelper;
import com.example.social_distance_reminder.services.NotificationsFCMService;

import java.util.Timer;
import java.util.TimerTask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.databinding.DataBindingUtil;

import static com.example.social_distance_reminder.helper.ServiceHelper.generateHash;

public class LoginActivity extends AppCompatActivity implements AuthRedirectHandler {

    ActivityLoginBinding loginBinding;
    PopupTermsBinding popupBinding;
    Dialog termsPopup;
    String phoneNum = "";
    CountDownTimer timer = null;
    String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());

        termsPopup = new Dialog(this);
        popupBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.popup_terms, null, false);
        termsPopup.setContentView(popupBinding.getRoot());

        setupOTPText();

        loginBinding.lnrLoginPhone.setVisibility(View.VISIBLE);
        loginBinding.lnrLoginCode.setVisibility(View.GONE);

        SpannableString termsConditionsString = new SpannableString("I agree on these terms and conditions");
        ClickableSpan clickableSpanTerms = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                popupBinding.btnDeclareClose.setOnClickListener(v2 -> termsPopup.dismiss());
                termsPopup.show();
            }
        };
        termsConditionsString.setSpan(clickableSpanTerms, 17, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        loginBinding.lblLoginTerms.setText(termsConditionsString);
        loginBinding.lblLoginTerms.setMovementMethod(LinkMovementMethod.getInstance());

        loginBinding.btnLoginPhone.setEnabled(false);
        loginBinding.chkLoginTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                loginBinding.btnLoginPhone.setEnabled(b);
            }
        });
        loginBinding.txtLoginPhone.setText("+1 650-555-3434");
        loginBinding.btnLoginPhone.setOnClickListener(this::addPhone);
        loginBinding.btnLoginCode.setOnClickListener(this::verifyPhone);
        loginBinding.btnLoginResend.setOnClickListener((view) -> ResendOTP());
        loginBinding.btnLoginChange.setOnClickListener((view -> {
            loginBinding.lnrLoginCode.setVisibility(View.GONE);
            loginBinding.lnrLoginPhone.setVisibility(View.VISIBLE);
        }));

        AnimationDrawable anim_loginGraphic = new AnimationDrawable();
        anim_loginGraphic.addFrame(getResources().getDrawable(R.drawable.login1), 4000);
        anim_loginGraphic.addFrame(getResources().getDrawable(R.drawable.login2), 4000);
        anim_loginGraphic.addFrame(getResources().getDrawable(R.drawable.login3), 4000);
        anim_loginGraphic.addFrame(getResources().getDrawable(R.drawable.login4), 4000);
        anim_loginGraphic.setOneShot(false);

        loginBinding.imgLoginGraphic.setImageDrawable(anim_loginGraphic);
        anim_loginGraphic.start();

    }

    private void setupOTPText() {
        loginBinding.txtLoginCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    loginBinding.txtLoginCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        loginBinding.txtLoginCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    loginBinding.txtLoginCode3.requestFocus();
                } else {
                    loginBinding.txtLoginCode1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        loginBinding.txtLoginCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    loginBinding.txtLoginCode4.requestFocus();
                } else {
                    loginBinding.txtLoginCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        loginBinding.txtLoginCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    loginBinding.txtLoginCode5.requestFocus();
                } else {
                    loginBinding.txtLoginCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        loginBinding.txtLoginCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    loginBinding.txtLoginCode6.requestFocus();
                } else {
                    loginBinding.txtLoginCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        loginBinding.txtLoginCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty()) {
                    loginBinding.txtLoginCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    String readOTP() {
        String OTP = "";
        String code1 = loginBinding.txtLoginCode1.getText().toString().trim();
        String code2 = loginBinding.txtLoginCode2.getText().toString().trim();
        String code3 = loginBinding.txtLoginCode3.getText().toString().trim();
        String code4 = loginBinding.txtLoginCode4.getText().toString().trim();
        String code5 = loginBinding.txtLoginCode5.getText().toString().trim();
        String code6 = loginBinding.txtLoginCode6.getText().toString().trim();
        if (!code1.isEmpty()) {
            OTP += code1;
        }
        if (!code2.isEmpty()) {
            OTP += code2;
        }
        if (!code3.isEmpty()) {
            OTP += code3;
        }
        if (!code4.isEmpty()) {
            OTP += code4;
        }
        if (!code5.isEmpty()) {
            OTP += code5;
        }
        if (!code6.isEmpty()) {
            OTP += code6;
        }
        return OTP;
    }

    @Override
    public void popupVerifyActivity() {
        loginBinding.lnrLoginPhone.setVisibility(View.GONE);
        loginBinding.lnrLoginCode.setVisibility(View.VISIBLE);
    }

    public void verifyPhone(View view) {
        String OTP = readOTP();
        if (OTP.length() == 6) {
            FirebaseAuthHelper.verifyUsingCode(OTP, this, this);
        } else {
            Toast.makeText(this, "OTP should have 6 numbers", Toast.LENGTH_SHORT).show();
        }
    }

    private void ResendOTP() {
        Toast.makeText(this, "OTP Resent", Toast.LENGTH_SHORT).show();
        startResendCountDown();
        //TODO setup resending OTP
    }

    public void addPhone(View view) {
        phoneNum = loginBinding.txtLoginPhone.getText().toString().trim();
        if (!phoneNum.isEmpty()) {
            FirebaseAuthHelper.verifyUsingPhoneNumber(phoneNum, this, this);
            loginBinding.lblLoginPhoneNumber.setText(phoneNum);
            startResendCountDown();
        }else{
            Toast.makeText(this, "Enter your number", Toast.LENGTH_SHORT).show();
        }
    }

    private void startResendCountDown() {
        if(timer != null){
            timer.cancel();
        }
        loginBinding.btnLoginResend.setVisibility(View.GONE);
        loginBinding.lblLoginResend.setVisibility(View.VISIBLE);
        timer = new CountDownTimer(60000, 1000) {

            public void onTick(long millisUntilFinished) {
                loginBinding.lblLoginResend.setText("Resend OTP in " + millisUntilFinished / 1000 + " seconds");
            }

            public void onFinish() {
                loginBinding.lblLoginResend.setVisibility(View.GONE);
                loginBinding.btnLoginResend.setVisibility(View.VISIBLE);
            }

        };
        timer.start();
    }

    public void redirectPrime() {
        finish();
        Intent homePage = new Intent(this, PrimeActivity.class);
        startActivity(homePage);
    }

    @Override
    public void onAuthComplete(String phonenumber) {
        Toast.makeText(getApplicationContext(), " YOU SUCCESSFULLY COMPLETED THE AUTHENTICATION ", Toast.LENGTH_SHORT).show();

        String userId = generateHash(FirebaseAuthHelper.getCurrentUser().getPhoneNumber());
        System.out.println("This is the insert user ID :- " + FirebaseAuthHelper.getCurrentUser().getPhoneNumber());
        Context context = this;
        SqlLiteHelper.getInstance(context).insertUserId(userId);
        new FirebaseCRUDHelper().onCreteUser(userId);
        redirectPrime();
//        NotificationsFCMService.setFCMToken();
    }

    @Override
    public void onAuthFail(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


}