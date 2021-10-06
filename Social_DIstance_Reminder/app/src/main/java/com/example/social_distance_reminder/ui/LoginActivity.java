package com.example.social_distance_reminder.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.auth.AuthRedirectHandler;
import com.example.social_distance_reminder.auth.FirebaseAuthHelper;
import com.example.social_distance_reminder.databinding.ActivityLoginBinding;
import com.example.social_distance_reminder.db.crudhelper.FirebaseCRUDHelper;
import com.example.social_distance_reminder.db.crudhelper.SqlLiteHelper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.helper.widget.Carousel;

import static com.example.social_distance_reminder.helper.ServiceHelper.generateHash;

public class LoginActivity extends AppCompatActivity implements AuthRedirectHandler {

    ActivityLoginBinding binding;
    String phoneNum = "";
    int[] images = {R.drawable.distanzia_cover_foreground_dark, R.drawable.distanzia_cover_foreground_light, R.drawable.distanzia_cover_foreground_original};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupOTPText();

        binding.lnrLoginPhone.setVisibility(View.VISIBLE);
        binding.lnrLoginCode.setVisibility(View.GONE);

        SpannableString termsConditionsString = new SpannableString("I agree on these terms and conditions");
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Toast.makeText(getApplicationContext(), "open", Toast.LENGTH_SHORT).show();
            }
        };
        termsConditionsString.setSpan(clickableSpan, 17, 37, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.btnLoginPhone.setEnabled(false);
        binding.chkLoginTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                binding.btnLoginPhone.setEnabled(b);
            }
        });
        binding.lblLoginTerms.setText(termsConditionsString);
//        binding.lblLoginTerms.setMovementMethod(LinkMovementMethod.getInstance());
        binding.txtLoginPhone.setText("+1 650-555-3434");
        binding.btnLoginPhone.setOnClickListener(this::addPhone);
        binding.btnLoginCode.setOnClickListener(this::verifyPhone);

        binding.carousel.setAdapter(new Carousel.Adapter() {
            @Override
            public int count() {
                return images.length;
            }

            @Override
            public void populate(View view, int index) {
                ((ImageView) view).setImageResource(images[index]);
            }

            @Override
            public void onNewItem(int index) {

            }
        });

    }


    private void setupOTPText() {
        binding.txtLoginCode1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    binding.txtLoginCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.txtLoginCode2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    binding.txtLoginCode3.requestFocus();
                } else {
                    binding.txtLoginCode1.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.txtLoginCode3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    binding.txtLoginCode4.requestFocus();
                } else {
                    binding.txtLoginCode2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.txtLoginCode4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    binding.txtLoginCode5.requestFocus();
                } else {
                    binding.txtLoginCode3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.txtLoginCode5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().trim().isEmpty()) {
                    binding.txtLoginCode6.requestFocus();
                } else {
                    binding.txtLoginCode4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        binding.txtLoginCode6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().isEmpty()) {
                    binding.txtLoginCode5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    String readOTP() {
        String OTP = "";
        String code1 = binding.txtLoginCode1.getText().toString().trim();
        String code2 = binding.txtLoginCode2.getText().toString().trim();
        String code3 = binding.txtLoginCode3.getText().toString().trim();
        String code4 = binding.txtLoginCode4.getText().toString().trim();
        String code5 = binding.txtLoginCode5.getText().toString().trim();
        String code6 = binding.txtLoginCode6.getText().toString().trim();
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
        binding.lnrLoginPhone.setVisibility(View.GONE);
        binding.lnrLoginCode.setVisibility(View.VISIBLE);
    }

    public void verifyPhone(View view) {
        String OTP = readOTP();
        if (OTP.length() == 6) {
            FirebaseAuthHelper.verifyUsingCode(OTP, this, this);
        } else {
            Toast.makeText(this, "OTP should have 6 numbers", Toast.LENGTH_SHORT).show();
        }
    }

    public void addPhone(View view) {
        phoneNum = binding.txtLoginPhone.getText().toString().trim();
        if (!phoneNum.isEmpty()) {
            FirebaseAuthHelper.verifyUsingPhoneNumber(phoneNum, this, this);
            binding.lblLoginPhoneNumber.setText(phoneNum);
        }
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
        Toast.makeText(getApplicationContext(), " YOU SUCCESSFULLY COMPLETED THE AUTHENTICATION ", Toast.LENGTH_SHORT).show();
        redirectPrime();
        String userId = generateHash(phonenumber, this);
        Context context = this;
        SqlLiteHelper.getInstance(context).insertUserId(userId);
        new FirebaseCRUDHelper().onCreteUser(userId);
    }

    @Override
    public void onAuthFail(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


}