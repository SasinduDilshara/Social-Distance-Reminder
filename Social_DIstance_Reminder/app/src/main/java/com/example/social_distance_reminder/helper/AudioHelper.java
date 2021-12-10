package com.example.social_distance_reminder.helper;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;

import com.example.social_distance_reminder.db.crudhelper.SqlLiteHelper;

public class AudioHelper {
    private static MediaPlayer mp = null;
    private static boolean isContinue = true;
    private final static ToneGenerator toneGen1 = new ToneGenerator(AudioManager.STREAM_MUSIC, 400);

    public static boolean isIsContinue() {
        return isContinue;
    }

    public static void setIsContinue(boolean isContinue) {
        AudioHelper.isContinue = isContinue;
    }

    public static MediaPlayer getInstance() {
        if (mp == null) {
            final MediaPlayer mp= new MediaPlayer();
            try{
                //you can change the path, here path is external directory(e.g. sdcard) /Music/maine.mp3
                mp.setDataSource(System.getProperty("user.dir") + "/danger-short.mp3");

                mp.prepare();
            }catch(Exception e){
                System.out.println("Media Player Error:- " + e.getMessage());
                e.printStackTrace();}
        }

        return mp;
    }

    public static void play(Context context) {
        if ( SqlLiteHelper.getInstance(context).getStats().getIsSoundOn() == 1) {
//            getInstance().start();
            AudioHelper.toneGen1.startTone(ToneGenerator.TONE_CDMA_PIP,10000);
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
