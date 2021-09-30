package com.example.social_distance_reminder.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.exceptions.BluetoothNotSupportException;
import com.example.social_distance_reminder.services.BackgroundServiceHandler;
import com.example.social_distance_reminder.auth.FirebaseAuthHelper;
import com.example.social_distance_reminder.helper.NotificationHelper;

import static com.example.social_distance_reminder.services.MacAddressService.getBluetoothMacAddress;
import static com.example.social_distance_reminder.services.MacAddressService.getMacAddress;

public class HomeActivity extends AppCompatActivity {
    private static boolean closed = true;
    private int notificatoionId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if(!closed) startService(new Intent(this, BackgroundServiceHandler.class));
        closed = true;
    }

    public void sendTestNotification(View view) {
        NotificationHelper.sendNormalNotification("Title", "Description", this);
    }

    public void sendTestIdentifiedNotification(View view) {
        NotificationHelper.sendIdentifiedNotification("ID Title", "ID Description", this);
    }

    public void redirectToNotificationPage(View view) {
        Intent viewNotificationPage = new Intent(this, ViewNotificationsActivity.class);
        startActivity(viewNotificationPage);
    }

    public void sendTestBackgroundNotification(View view) {
        this.notificatoionId = NotificationHelper.createBackgroundNotification("Background Title", "Background Description", this);
        try {
            System.out.println("\n\nThis is the Mac address : - " + getMacAddress(this) + "\n\n");
            System.out.println("\n\n" + "Bluetooth MAC is :-" + getBluetoothMacAddress() + "\n\n");
        } catch (BluetoothNotSupportException e) {
            e.printStackTrace();
        }
    }

    public void RemoveTestBackgroundNotification(View view) {
        NotificationHelper.removeBackgroundNotification(this.notificatoionId, this);
    }

    public void logout(View view) {
        try {
            FirebaseAuthHelper.logout();
            Intent landingPage = new Intent(this, LandingActivity.class);
            landingPage.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(landingPage);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    public void redirectToBluetoothTestPage(View view) {
        Intent bluetoothTestPage = new Intent(this, BluetoothTestActivity.class);
        startActivity(bluetoothTestPage);
    }
}