package com.example.social_distance_reminder.helper;

import android.media.MediaPlayer;

public class AudioHelper {
    private static MediaPlayer mp = null;
    private static boolean isContinue = true;

    public static boolean isIsContinue() {
        return isContinue;
    }

    public static void setIsContinue(boolean isContinue) {
        AudioHelper.isContinue = isContinue;
    }

    private static MediaPlayer getInstance() {
        if (mp == null) {
            final MediaPlayer mp=new MediaPlayer();
            try{
                //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
                mp.setDataSource(System.getProperty("user.dir") + "/danger-short");

                mp.prepare();
            }catch(Exception e){e.printStackTrace();}
        }

        return mp;
    }

    public static void play() {
        if (isIsContinue()) {
            getInstance().start();
        }
    }

    public static void pause() {
        try {
            getInstance().pause();
        } catch (Exception ex) {

        }
    }

    public static void stop() {
        try {
            getInstance().stop();
        } catch (Exception ex) {

        }
    }
}
