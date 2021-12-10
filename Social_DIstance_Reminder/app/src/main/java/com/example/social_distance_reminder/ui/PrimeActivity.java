package com.example.social_distance_reminder.ui;

import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.databinding.ActivityPrimeBinding;
import com.example.social_distance_reminder.databinding.PopupAboutBinding;
import com.example.social_distance_reminder.databinding.PopupSettingsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static android.content.ContentValues.TAG;

public class PrimeActivity extends AppCompatActivity {

    private ActivityPrimeBinding binding;
    private PopupSettingsBinding settingsBinding;
    private PopupAboutBinding aboutBinding;
    private Dialog settingsPopup, aboutPopup;
    private String TAG = "Prime";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrimeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        createNotificationChannel();

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
            settingsBinding.minDistance.setText("");
            settingsPopup.show();
        } else if (id == R.id.mnu_About) {
            aboutBinding.btnAboutClose.setOnClickListener(v2 -> aboutPopup.dismiss());
            aboutPopup.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(getString(R.string.default_notification_channel_id), name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }
}