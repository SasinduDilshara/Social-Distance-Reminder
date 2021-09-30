package com.example.social_distance_reminder.services;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.example.social_distance_reminder.exceptions.BluetoothNotSupportException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

public class MacAddressService {
    public static String getMacAddress(Context context) throws BluetoothNotSupportException {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }
                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(Integer.toHexString(b & 0xFF) + ":");
                }
                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
            System.out.println("\n\nError Occured\n\n");
        }
        return "";
    }

    public static String getBluetoothMacAddress() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        String bluetoothMacAddress = "";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            try {
                Field mServiceField = bluetoothAdapter.getClass().getDeclaredField("mService");
                mServiceField.setAccessible(true);

                Object btManagerService = mServiceField.get(bluetoothAdapter);

                if (btManagerService != null) {
                    bluetoothMacAddress = (String) btManagerService.getClass().getMethod("getAddress").invoke(btManagerService);
                }
            } catch (NoSuchFieldException e) {
                System.out.println("Error1 occured in getBluetoothMAC, Error is :- " + e.getMessage());

            } catch (NoSuchMethodException e) {
                System.out.println("Error2 occured in getBluetoothMAC, Error is :- " + e.getMessage());

            } catch (IllegalAccessException e) {
                System.out.println("Error3 occured in getBluetoothMAC, Error is :- " + e.getMessage());

            } catch (InvocationTargetException e) {
                System.out.println("Error4 occured in getBluetoothMAC, Error is :- " + e.getMessage());

            }
        } else {
            bluetoothMacAddress = bluetoothAdapter.getAddress();
        }
        return bluetoothMacAddress;
    }

//    public static String getMacAddress(Context context) throws BluetoothNotSupportException {
//        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        String address = info.getMacAddress();
//        return address;
//    }
}

