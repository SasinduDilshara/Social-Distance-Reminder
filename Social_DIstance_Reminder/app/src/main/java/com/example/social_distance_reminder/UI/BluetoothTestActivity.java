package com.example.social_distance_reminder.UI;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.exceptions.BluetoothNotSupportException;
import com.example.social_distance_reminder.helper.BluetoothHelper;
import com.example.social_distance_reminder.helper.ServiceHelper;
import com.example.social_distance_reminder.services.CustomBluetoothService;
import com.example.social_distance_reminder.services.LocationService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.ContentValues.TAG;
import static com.example.social_distance_reminder.helper.BluetoothHelper.getBroadcastReciever;
import static com.example.social_distance_reminder.helper.BluetoothHelper.getIntentFilter;
import static com.example.social_distance_reminder.helper.ServiceHelper.isMyServiceRunning;
import static com.example.social_distance_reminder.helper.ServiceHelper.setGeoCoder;

public class BluetoothTestActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    String[] requiredPermissions = ServiceHelper.getPermissions();
    private static final int REQUEST_CODE = 1;
    private LocationService gpsTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_test);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            requiredPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        }
        //TODO: HANDLE_THIS
//        stopService( new Intent(this, CustomBluetoothService.class));

        if (!EasyPermissions.hasPermissions(this, requiredPermissions)) {
            EasyPermissions.requestPermissions(this, "This app need Location Access to continue ",
                    REQUEST_CODE, requiredPermissions);
//            Button btn=findViewById(R.id.final_btn);
//            Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);
//            btn.startAnimation(animFadeIn);
        }
        setGeoCoder(this);
        registerReceiver(getBroadcastReciever(), getIntentFilter());
        try {
            BluetoothHelper.requestBluetooth(this);
        } catch (BluetoothNotSupportException e) {
            System.out.println("\n\n\n" + e.getMessage()+"\n\n\n");
        }
    }


    public void locationFinder(View view){
        gpsTracker = new LocationService(getApplicationContext());
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            Location location = gpsTracker.getLocation();

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = null;
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                String cityName = addresses.get(0).getAddressLine(0);
                String stateName = addresses.get(0).getAddressLine(1);
                String countryName = addresses.get(0).getAddressLine(2);


                System.out.println("cityName:- " + cityName);
                System.out.println("stateName:- " + stateName);
                System.out.println("countryName:- " + countryName);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println("lan:- " + latitude);
            System.out.println("lon:- " + longitude);
        }else{
            gpsTracker.showSettingsAlert();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(getBroadcastReciever());
    }

    public void startBluetoothDiscovery(View view) {
        try {
            BluetoothHelper.getBluetoothAdapter().startDiscovery();
        } catch (BluetoothNotSupportException e) {
            System.out.println("\n\nError:- " + e.getMessage() + "\n\n");
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isMyServiceRunning(this, CustomBluetoothService.class)) {
            Log.d(TAG, "onResume: Scaning is not running, Starting it Now!");
            Intent serviceIntent = new Intent(this, CustomBluetoothService.class);
            ContextCompat.startForegroundService(this, serviceIntent);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        System.out.println("\n\nPermission Granted\n\n");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}