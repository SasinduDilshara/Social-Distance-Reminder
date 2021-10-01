package com.example.social_distance_reminder.auth;

public interface AuthRedirectHandler {
    public void onAuthComplete(String phonenumber);
    public void onAuthFail(String message);
    public void popupVerifyActivity();
}
