package com.example.social_distance_reminder.UI;

        import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;
        import android.view.View;
        import android.widget.Toast;

        import com.example.social_distance_reminder.R;
        import com.example.social_distance_reminder.exceptions.BluetoothNotSupportException;
        import com.example.social_distance_reminder.services.BluetoothHelperService;

        import static com.example.social_distance_reminder.services.BluetoothHelperService.getBroadcastReciever;
        import static com.example.social_distance_reminder.services.BluetoothHelperService.getIntentFilter;

public class BluetoothTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_test);
        registerReceiver(getBroadcastReciever(), getIntentFilter());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Don't forget to unregister the ACTION_FOUND receiver.
        unregisterReceiver(getBroadcastReciever());
    }

    public void startBluetoothDiscovery(View view) {
        try {
            BluetoothHelperService.getBluetoothAdapter().startDiscovery();
        } catch (BluetoothNotSupportException e) {
            System.out.println("\n\n" + e.getMessage() + "\n\n");
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}