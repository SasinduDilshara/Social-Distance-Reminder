package com.example.social_distance_reminder.helper;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.location.Geocoder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Locale;
import java.util.UUID;

public class ServiceHelper {
    private static Geocoder geocoder = null;

    public static void setGeoCoder(Context context) {
        if (geocoder == null) {
            geocoder = new Geocoder(context, Locale.getDefault());
        }
    }

    public static Geocoder getGeocoder() {
        if (geocoder != null)
            return geocoder;
        return null;
    }

    @SuppressWarnings("deprecation")
    public static  boolean isMyServiceRunning(Context context , Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        assert manager != null;
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String[] getPermissions(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION};

            return permissions;
        }
        else {
            String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION};
            return permissions;
        }
    }

    public static boolean isInternetAvailable() {
        try {
            int timeoutMs = 1500;
            Socket sock = new Socket();
            SocketAddress sockaddr = new InetSocketAddress("8.8.8.8", 53);

            sock.connect(sockaddr, timeoutMs);
            sock.close();

            return true;
        } catch (IOException e) { return false; }
    }

    public static String generateHash(String string,Context context){
        //TODO: Implement
        String s = new String(UUID.randomUUID().toString()); // refer string resource file in java_resource.xml
        return s;

    }
}
