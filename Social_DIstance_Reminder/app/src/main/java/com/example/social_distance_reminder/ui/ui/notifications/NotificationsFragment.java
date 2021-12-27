package com.example.social_distance_reminder.ui.ui.notifications;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.social_distance_reminder.databinding.FragmentNotificationsBinding;
import com.example.social_distance_reminder.helper.NotificationsViewAdapter;
import com.example.social_distance_reminder.models.Notification;
import com.example.social_distance_reminder.db.crudhelper.SqlLiteHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class NotificationsFragment extends Fragment {

    ArrayList<Notification> notifications;
    FragmentNotificationsBinding binding;
    NotificationsViewAdapter notificationsViewAdapter;
    LocalBroadcastManager bm;
    SqlLiteHelper sqlLiteHelper;

    private String TAG = "Test";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        sqlLiteHelper = SqlLiteHelper.getInstance(getContext());

//        sqlLiteHelper.addDeclareNotification(new Notification("Test1", Calendar.getInstance().getTime(), "qwertyudfghjcvbnfghjvbn", true));
//        sqlLiteHelper.addDeclareNotification(new Notification("Test3", Calendar.getInstance().getTime(), "qwertyudfghjcvbnfdhahrhahadheghjvbn", false));
//        sqlLiteHelper.addDeclareNotification(new Notification("Test4", Calendar.getInstance().getTime(), "qwertyudfghjsdhserhsehwehrsehsdhfnstrjsrtjsjhsrjrtjstrjsryjcvbnfghjvbn", false));
//        sqlLiteHelper.addDeclareNotification(new Notification("Test2", Calendar.getInstance().getTime(), "sdhsdhsdhsdfhsrhqshaha", false));
//        sqlLiteHelper.addDeclareNotification(new Notification("Test1", Calendar.getInstance().getTime(), "qwertyudfghjcvbnfghjvbn", true));
//        sqlLiteHelper.addDeclareNotification(new Notification("Test2", Calendar.getInstance().getTime(), "sdhsdhsdhsdfhsrhqshaha", false));
//        sqlLiteHelper.addDeclareNotification(new Notification("Test3", Calendar.getInstance().getTime(), "qwertyudfghjcvbnfdhahrhahadheghjvbn", false));
//        sqlLiteHelper.addDeclareNotification(new Notification("Test4", Calendar.getInstance().getTime(), "qwertyudfghjsdhserhsehwehrsehsdhfnstrjsrtjsjhsrjrtjstrjsryjcvbnfghjvbn", false));

        notifications = sqlLiteHelper.getNotifications();
        generateNotifications();
        return root;
    }

    private void generateNotifications() {
        notificationsViewAdapter = new NotificationsViewAdapter(getActivity(), notifications);
        binding.setNotificationAdapter(notificationsViewAdapter);
    }

//    public void addNotificationToView(Notification newNotification){
//        notifications.add(newNotification);
//        notificationsViewAdapter.notifyItemInserted(notifications.size()-1);
//    }


//    public BroadcastReceiver onNotificationReceived = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Log.d(TAG, "onReceive: grhtjngbfddfhgm");
//            if (intent != null) {
//                String title = intent.getStringExtra("title");
//                String message = intent.getStringExtra("message");
//                String importance = intent.getStringExtra("importance");
//                String Date = intent.getStringExtra("date");
//                Log.d(TAG, "onReceive: "+title+importance);
//            }
//        }
//    };


    @Override
    public void onDestroy() {
//        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(onNotificationReceived);
        super.onDestroy();
    }
}