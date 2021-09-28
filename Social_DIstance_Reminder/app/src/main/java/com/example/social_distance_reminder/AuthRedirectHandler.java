package com.example.social_distance_reminder;

public interface AuthRedirectHandler {
  public void onAuthComplete();
  public void onAuthFail(String message);
  public void popupVerifyActivity();
}
