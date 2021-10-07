package com.example.social_distance_reminder.UI;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.services.LocationService;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class TestActivity extends AppCompatActivity {

    private LocationService gpsTracker;
    private TextView tvLatitude,tvLongitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        tvLatitude = (TextView)findViewById(R.id.latitude);
        tvLongitude = (TextView)findViewById(R.id.longitude);

        try {
            if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void OnClick(View view){
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

            tvLatitude.setText(String.valueOf(latitude));
            tvLongitude.setText(String.valueOf(longitude));
            System.out.println("lan:- " + latitude);
            System.out.println("lon:- " + longitude);
        }else{
            gpsTracker.showSettingsAlert();
        }
    }
}
