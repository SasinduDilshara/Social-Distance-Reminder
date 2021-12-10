package com.example.social_distance_reminder.ui.ui.home;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.AnimationDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.databinding.FragmentHomeBinding;
import com.example.social_distance_reminder.databinding.PopupBlacklistBinding;
import com.example.social_distance_reminder.databinding.PopupDeclarationBinding;
import com.example.social_distance_reminder.databinding.PopupSettingsBinding;
import com.example.social_distance_reminder.db.crudhelper.FirebaseCRUDHelper;
import com.example.social_distance_reminder.db.crudhelper.SqlLiteHelper;
import com.example.social_distance_reminder.exceptions.BluetoothNotSupportException;
import com.example.social_distance_reminder.helper.BlacklistViewAdapter;
import com.example.social_distance_reminder.helper.BluetoothHelper;
import com.example.social_distance_reminder.helper.ServiceHelper;
import com.example.social_distance_reminder.models.BlacklistItem;
import com.example.social_distance_reminder.services.CustomBluetoothService;
import com.example.social_distance_reminder.services.LocationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static com.example.social_distance_reminder.helper.BluetoothHelper.getBroadcastReciever;
import static com.example.social_distance_reminder.helper.BluetoothHelper.getIntentFilter;
import static com.example.social_distance_reminder.helper.ServiceHelper.isMyServiceRunning;
import static com.example.social_distance_reminder.helper.ServiceHelper.setGeoCoder;

public class HomeFragment extends Fragment implements EasyPermissions.PermissionCallbacks {


    String[] requiredPermissions = ServiceHelper.getPermissions();
    private static final int REQUEST_CODE = 1;
    private LocationService gpsTracker;
    private FragmentHomeBinding homeBinding;
    private PopupDeclarationBinding popupBinding;
    private PopupBlacklistBinding blacklistBinding;
    private Dialog declarePopup, blacklistPopup, settingsPopup;
    private PopupSettingsBinding settingsBinding;
    private ArrayList<BlacklistItem> blacklistItems;
    private String TAG = "Testing";
    private Boolean isServiceActive;
    private BlacklistViewAdapter blacklistViewAdapter;
    Intent serviceIntent = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = homeBinding.getRoot();

        isServiceActive = false;

        settingsPopup = new Dialog(getActivity());
        settingsBinding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.popup_settings, null, false);
        settingsPopup.setContentView(settingsBinding.getRoot());

        declarePopup = new Dialog(getActivity());
        popupBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popup_declaration, null, false);
        declarePopup.setContentView(popupBinding.getRoot());

        blacklistPopup = new Dialog(getActivity());
        blacklistBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popup_blacklist, null, false);
        blacklistPopup.setContentView(blacklistBinding.getRoot());

        homeBinding.btnHomeDeclare.setOnClickListener(v2 -> showDeclarationPopup());
        homeBinding.btnHomeBlacklist.setOnClickListener(v1 -> showBlacklistPopup());
        homeBinding.btnHomeSettings.setOnClickListener(v3 -> showSettingsPopup());
        homeBinding.btnHomeOnoff.setOnClickListener(v2 -> {
            if (isServiceActive) {
                isServiceActive = false;
                Toast.makeText(getContext(), "Distanzia now stopped", Toast.LENGTH_SHORT).show();
                homeBinding.btnHomeOnoff.setImageResource(R.drawable.button_on);
                deactivateService();
            } else {
                isServiceActive = true;
                Toast.makeText(getContext(), "Distanzia now running...", Toast.LENGTH_SHORT).show();
                homeBinding.btnHomeOnoff.setImageResource(R.drawable.button_off);
                activateService();
            }
        });

        AnimationDrawable anim_homeGraphic = new AnimationDrawable();
        anim_homeGraphic.addFrame(getResources().getDrawable(R.drawable.covid19_1), 4000);
        anim_homeGraphic.addFrame(getResources().getDrawable(R.drawable.covid19_2), 4000);
        anim_homeGraphic.addFrame(getResources().getDrawable(R.drawable.covid19_3), 4000);
        anim_homeGraphic.addFrame(getResources().getDrawable(R.drawable.covid19_4), 4000);
        anim_homeGraphic.addFrame(getResources().getDrawable(R.drawable.covid19_5), 4000);
        anim_homeGraphic.addFrame(getResources().getDrawable(R.drawable.covid19_6), 4000);
        anim_homeGraphic.setOneShot(false);

        homeBinding.imgHomeGraphic.setImageDrawable(anim_homeGraphic);
        anim_homeGraphic.start();

        return root;
    }

    private void activateService() {
        setBluetoothOnButton();
        onResume();
    }

    private void deactivateService() {
        onResume();
    }


    ///////////////////////////////////////////////////////////////////////////////////////////////////
    private void setBluetoothOnButton() {
        try {
            if (ContextCompat.checkSelfPermission(requireActivity().getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 101);
            }
        } catch (Exception e) {
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
            System.out.println("\n\n\n" + e.getMessage() + "\n\n\n");
        }

    }


    public void locationFinder(View view) {
        gpsTracker = new LocationService(requireActivity().getApplicationContext());
        if (gpsTracker.canGetLocation()) {
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
        } else {
            gpsTracker.showSettingsAlert();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        //requireActivity().unregisterReceiver(getBroadcastReciever());
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
        if (!isMyServiceRunning(requireActivity(), CustomBluetoothService.class) && isServiceActive) {
            serviceIntent = new Intent(requireActivity(), CustomBluetoothService.class);
            Log.d(TAG, "onResume: Scaning is not running, Starting it Now!");
            ContextCompat.startForegroundService(requireActivity(), serviceIntent);
        } else if (isMyServiceRunning(requireActivity(), CustomBluetoothService.class) && !isServiceActive) {
            if (serviceIntent != null) {
                getActivity().stopService(serviceIntent);
            }
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

    public void showBlacklistPopup() {

        blacklistBinding.btnBlacklistClose.setOnClickListener(v2 -> blacklistPopup.dismiss());
        blacklistItems = SqlLiteHelper.getInstance(getContext()).getBlackListPhoneNumbers();
        Log.d(TAG, "showBlacklistPopup: " + blacklistItems.size());
        blacklistViewAdapter = new BlacklistViewAdapter(getActivity(), blacklistItems);
        blacklistBinding.setBlacklistAdapter(blacklistViewAdapter);
        blacklistBinding.btnBlacklistAdd.setOnClickListener(v3 -> {
            String num = blacklistBinding.edtBlacklistAdd.getText().toString();
            if (!num.equals("")) {
                SqlLiteHelper.getInstance(getContext()).addBlacklistDevice(num);
                blacklistItems.add(new BlacklistItem(num));
                blacklistViewAdapter.notifyItemChanged(blacklistItems.size() - 1);
                blacklistBinding.edtBlacklistAdd.setText("");
                Toast.makeText(getContext(), num + " was added to the blacklist", Toast.LENGTH_SHORT).show();
            }
        });
        blacklistPopup.show();
    }

    public void showSettingsPopup() {
        settingsBinding.btnSettingsClose.setOnClickListener(v2 -> settingsPopup.dismiss());
        settingsBinding.minDistance.setText("");
        settingsPopup.show();
    }

    public void showDeclarationPopup() {

        popupBinding.btnDeclareClose.setOnClickListener(v2 -> declarePopup.dismiss());
        popupBinding.lblDeclareRandom.setText(getRandomString(8));
        popupBinding.txtDeclareRandom.setText("");
        popupBinding.btnDeclareConfirm.setOnClickListener(v2 -> {
            if (popupBinding.lblDeclareRandom.getText().toString().equals(popupBinding.txtDeclareRandom.getText().toString())) {
                Toast.makeText(getActivity(), "Declaration Succeeded", Toast.LENGTH_SHORT).show();
                new FirebaseCRUDHelper().getTheCloseDevices(getContext());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeBinding = null;
    }
}