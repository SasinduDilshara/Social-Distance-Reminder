package com.example.social_distance_reminder.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.social_distance_reminder.databinding.PopupAboutBinding;
import com.example.social_distance_reminder.databinding.PopupSettingsBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.databinding.ActivityPrimeBinding;

public class PrimeActivity extends AppCompatActivity {

    private ActivityPrimeBinding binding;
    private PopupSettingsBinding settingsBinding;
    private PopupAboutBinding aboutBinding;
    private Dialog settingsPopup, aboutPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        settingsPopup = new Dialog(this);
        settingsBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.popup_settings, null, false);
        settingsPopup.setContentView(settingsBinding.getRoot());

        aboutPopup = new Dialog(this);
        aboutBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.popup_about, null, false);
        aboutPopup.setContentView(aboutBinding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_prime);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mnu_Settings) {
            settingsBinding.btnSettingsClose.setOnClickListener(v2 -> settingsPopup.dismiss());
            settingsPopup.show();
        } else if (id == R.id.mnu_About) {
            aboutBinding.btnAboutClose.setOnClickListener(v2 -> aboutPopup.dismiss());
            aboutPopup.show();
        }
        return super.onOptionsItemSelected(item);
    }

}