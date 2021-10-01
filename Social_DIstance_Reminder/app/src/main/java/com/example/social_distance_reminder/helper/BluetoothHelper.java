package com.example.social_distance_reminder.helper;

import android.Manifest;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.TextView;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.exceptions.BluetoothNotSupportException;

import java.util.ArrayList;
import java.util.Set;

import static androidx.core.app.ActivityCompat.startActivityForResult;

public class BluetoothHelper {

    private static BluetoothAdapter bluetoothAdapter = null;
    public static final int REQUEST_ENABLE_BT = 1;
    public static BroadcastReceiver broadcastReceiver = null;

    public BluetoothHelper() {
    }

    public static BluetoothAdapter getBluetoothAdapter() throws BluetoothNotSupportException {
        if (bluetoothAdapter == null) {
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                throw new BluetoothNotSupportException("This device doesn't support Bluetooth");
            }
        }
        return bluetoothAdapter;
    }

    public static void requestBluetooth(Context context) throws BluetoothNotSupportException {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            throw new BluetoothNotSupportException("Bluetooth Not Supported by this Device!!");
        }
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            enableBtIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(enableBtIntent);
        }
    }

    public static boolean setBluetooth(boolean enable) {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        boolean isEnabled = bluetoothAdapter.isEnabled();
        if (enable && !isEnabled) {
            return bluetoothAdapter.enable();
        } else if (!enable && isEnabled) {
            return bluetoothAdapter.disable();
        }
        // No need to change bluetooth state
        return true;
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

    public Set<BluetoothDevice> getBondedDevicesForDevice() {
        return bluetoothAdapter.getBondedDevices();
    }

    public static BroadcastReceiver getBroadcastReciever() {
        if (broadcastReceiver == null) {
            broadcastReceiver =  new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();

                    System.out.println("\n\n" + "Inside reciever" + "\n\n");
                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        // Discovery has found a device. Get the BluetoothDevice
                        // object and its info from the Intent.
                        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                        String deviceName = device.getName();
                        String deviceHardwareAddress = device.getAddress(); // MAC address

                        int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);
                        String bluetoothDeviceName = intent.getStringExtra(BluetoothDevice.EXTRA_NAME);

                        System.out.println("\ndeviceName - " + deviceName + "\n" +
                                "MAC - " + deviceHardwareAddress + "\n" +
                                "Bluetooth Device Name " + bluetoothDeviceName + "\n" +
                                "Rssi " + rssi + "dB\n"
                        );
                    }
                }
            };
        }
        return broadcastReceiver;
    }

    public static IntentFilter getIntentFilter() {
        return new IntentFilter(BluetoothDevice.ACTION_FOUND);
    }

    public static String getMacAddress() throws BluetoothNotSupportException {
        return getBluetoothAdapter().getAddress();
    }
}