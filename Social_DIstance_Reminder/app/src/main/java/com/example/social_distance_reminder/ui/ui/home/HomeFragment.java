package com.example.social_distance_reminder.ui.ui.home;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.databinding.FragmentHomeBinding;
import com.example.social_distance_reminder.databinding.PopupDeclarationBinding;
import com.example.social_distance_reminder.db.crudhelper.FirebaseCRUDHelper;
import com.example.social_distance_reminder.exceptions.BluetoothNotSupportException;
import com.example.social_distance_reminder.helper.BluetoothHelper;
import com.example.social_distance_reminder.helper.ServiceHelper;
import com.example.social_distance_reminder.models.Notification;
import com.example.social_distance_reminder.notifications.MySingleton;
import com.example.social_distance_reminder.services.CustomBluetoothService;
import com.example.social_distance_reminder.services.LocationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.content.ContentValues.TAG;
import static com.example.social_distance_reminder.helper.BluetoothHelper.getBroadcastReciever;
import static com.example.social_distance_reminder.helper.BluetoothHelper.getIntentFilter;
import static com.example.social_distance_reminder.helper.ServiceHelper.isMyServiceRunning;
import static com.example.social_distance_reminder.helper.ServiceHelper.setGeoCoder;

public class HomeFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    String[] requiredPermissions = ServiceHelper.getPermissions();
    private static final int REQUEST_CODE = 1;
    private LocationService gpsTracker;

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=AAAA6NYYD9E:APA91bHkC_6fqKP8rh3_GaCK6mRRSKqBcZRRI7Nb_jzkjuFGgzjMJr38jn3khw7CHFKuJJNXF0l5qAK8jLBxsEO7X4YUTA53dJi4j9GshqPMRHdsKAC7HZKLMO1UuEPTXg8X4bjanAk9\t\n";
    final private String contentType = "application/json";
    private FragmentHomeBinding homeBinding;
    private PopupDeclarationBinding popupBinding;
    private Dialog declarePopup;
    int[] images = {R.drawable.covid_1, R.drawable.covid_2, R.drawable.covid_3, R.drawable.covid_4, R.drawable.covid_5, R.drawable.covid_6};
    private String TAG = "Testing";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = homeBinding.getRoot();

        declarePopup = new Dialog(getActivity());
        popupBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popup_declaration, null, false);
        declarePopup.setContentView(popupBinding.getRoot());

        homeBinding.btnHomeDeclare.setOnClickListener(v2 -> showDeclarationPopup());
        homeBinding.btnHomeOnoff.setOnClickListener(v2-> setBluetoothOnButton());

        return root;
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void setBluetoothOnButton() {
        try {
            if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            requiredPermissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
        }
        //TODO: HANDLE_THIS
//        stopService( new Intent(this, CustomBluetoothService.class));

        if (!EasyPermissions.hasPermissions(requireActivity(), requiredPermissions)) {
            EasyPermissions.requestPermissions(this, "This app need Location Access to continue ",
                    REQUEST_CODE, requiredPermissions);
        }
        setGeoCoder(requireActivity());
        requireActivity().registerReceiver(getBroadcastReciever(), getIntentFilter());
        try {
            BluetoothHelper.requestBluetooth(requireActivity());
        } catch (BluetoothNotSupportException e) {
            System.out.println("\n\n\n" + e.getMessage()+"\n\n\n");
        }

    }

    private void setBluetoothOffButton() {

    }


    public void locationFinder(View view){
        gpsTracker = new LocationService(requireActivity().getApplicationContext());
        if(gpsTracker.canGetLocation()){
            double latitude = gpsTracker.getLatitude();
            double longitude = gpsTracker.getLongitude();
            Location location = gpsTracker.getLocation();

            Geocoder geocoder = new Geocoder(requireActivity(), Locale.getDefault());
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
    public void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        requireActivity().unregisterReceiver(getBroadcastReciever());
    }

    public void startBluetoothDiscovery(View view) {
        try {
            BluetoothHelper.getBluetoothAdapter().startDiscovery();
        } catch (BluetoothNotSupportException e) {
            System.out.println("\n\nError:- " + e.getMessage() + "\n\n");
            Toast.makeText(requireActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isMyServiceRunning(requireActivity(), CustomBluetoothService.class)) {
            Log.d(TAG, "onResume: Scaning is not running, Starting it Now!");
            Intent serviceIntent = new Intent(requireActivity(), CustomBluetoothService.class);
            ContextCompat.startForegroundService(requireActivity(), serviceIntent);
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
            requireActivity().finish();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    public void showDeclarationPopup() {

//        new FirebaseCRUDHelper().getSample();
        new FirebaseCRUDHelper().getTheCloseDevices("ffe79751-e3a1-4f06-ba7b-53ea0b774cc9");
        popupBinding.btnDeclareClose.setOnClickListener(v2 -> declarePopup.dismiss());
        popupBinding.lblDeclareRandom.setText(getRandomString(8));
        popupBinding.txtDeclareRandom.setText("");
        popupBinding.btnDeclareConfirm.setOnClickListener(v2 -> {
            if (popupBinding.lblDeclareRandom.getText().toString().equals(popupBinding.txtDeclareRandom.getText().toString())) {
                Toast.makeText(getActivity(), "Declaration Succeeded", Toast.LENGTH_SHORT).show();
                Notification declaration = new Notification("WARNING", new Date(),"A person who came near you has declared as a COVID patient",true);
                sendDeclarationNotification(createNotificationObject(declaration));
            } else {
                Toast.makeText(getActivity(), "Wrong", Toast.LENGTH_SHORT).show();
            }
        });
        declarePopup.show();
    }

    public String getRandomString(int n) {
        String AlphaNumericString = "ABCDEFGHJKLMNPQRSTUVWXYZ"
                + "123456789"
                + "abcdefghijkmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        Random random = new Random();

        for (int i = 0; i < n; i++) {

            int index = random.nextInt(AlphaNumericString.length());
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private JSONObject createNotificationObject(Notification notification){
        JSONObject notificationObject = new JSONObject();
        JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("title",notification.getTitle());
            notificationBody.put("message",notification.getDescription());
            notificationBody.put("date",notification.getDate());
            notificationBody.put("importance",notification.isImportant());
//                notification.put("to", "cJBFXY1FQzCcqHjmepWc_t:APA91bEaSjUd5wzC0dUc-NOBTTaFIDKv1fBW2ZrN3o5uayfzqRl4RJbkhpo4IEGOOEtZlCF5ZwjmHuXIRYQaz5BtPK1s1l_AAiiqiG1yWWq2KCjS7Oi20Ad2ddTWJC7rM2XtjnP3kYs_");
            notificationObject.put("to", "/topics/All");
            notificationObject.put("data", notificationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
        return notificationObject;
    }

    private void sendDeclarationNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> Log.i(TAG, "onResponse: " + response.toString()),
                error -> {
                    Toast.makeText(getContext(), "Request error", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onErrorResponse: Didn't work");
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeBinding = null;
    }
}