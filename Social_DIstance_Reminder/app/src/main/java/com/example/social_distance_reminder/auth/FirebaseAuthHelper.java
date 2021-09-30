package com.example.social_distance_reminder.auth;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class FirebaseAuthHelper {
    private static String mVerificationId = "";
    private static PhoneAuthProvider.ForceResendingToken mResendToken;

    private static PhoneAuthCredential getCredintial(String verificationId, String code) {
        return PhoneAuthProvider.getCredential(verificationId, code);
    }

    private static PhoneAuthProvider.ForceResendingToken getmResendToken() {
        return mResendToken;
    }

    private static void setmResendToken(PhoneAuthProvider.ForceResendingToken token) {
        mResendToken = token;
    }

    public static FirebaseAuth getmAuth() {
        return FirebaseAuth.getInstance();
    }

    public static void verifyUsingPhoneNumber(String phoneNumber, Activity activity, AuthRedirectHandler authRedirectHandler) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(getmAuth())
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(getCallbacks(activity, authRedirectHandler))          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public static void verifyUsingCode(String code, Activity activity, AuthRedirectHandler authRedirectHandler) {
        PhoneAuthCredential phoneAuthCredential = getCredintial(mVerificationId, code);
        signInWithPhoneAuthCredential(phoneAuthCredential, activity, authRedirectHandler);
    }

    private static PhoneAuthProvider.OnVerificationStateChangedCallbacks getCallbacks(Activity activity, AuthRedirectHandler authRedirectHandler) {
        return new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential, activity, authRedirectHandler);
                authRedirectHandler.onAuthComplete();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);


                authRedirectHandler.onAuthFail(handleAuthExceptions(e));
            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                setmResendToken(token);
                authRedirectHandler.popupVerifyActivity();
            }
        };
    }

    private static void signInWithPhoneAuthCredential(PhoneAuthCredential credential, Activity activity, AuthRedirectHandler authRedirectHandler) {
        getmAuth().signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update ui with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            authRedirectHandler.onAuthComplete();
                            // Update ui
                        } else {
                            authRedirectHandler.onAuthFail(handleAuthExceptions(task.getException()));
                        }
                    }
                });
    }

    private static String handleAuthExceptions(Exception ex) {
        if ( ex instanceof FirebaseAuthInvalidCredentialsException) {
            return "The verification Code is Invalid!";
        } else if (ex instanceof FirebaseAuthInvalidCredentialsException) {
            return "The verification Code is Invalid!";
        } else if (ex instanceof FirebaseTooManyRequestsException) {
            return "The SMS Quota for free firebase account has been exceed!";
        } else if (ex instanceof FirebaseException) {
            return ex.getMessage();
        } else {
            return ex.getMessage();
        }
    }

    public static FirebaseUser getCurrentUser() {
        return getmAuth().getCurrentUser();
    }

    public static void logout() throws Exception {
        if (getCurrentUser() != null) {
            getmAuth().signOut();
        } else {
            throw new Exception("Signout Fail!");
        }
    }
}
