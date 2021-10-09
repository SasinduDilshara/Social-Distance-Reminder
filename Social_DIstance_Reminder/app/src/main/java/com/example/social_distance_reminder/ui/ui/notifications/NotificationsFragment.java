package com.example.social_distance_reminder.ui.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.social_distance_reminder.databinding.FragmentNotificationsBinding;
import com.example.social_distance_reminder.helper.NotificationsViewAdapter;
import com.example.social_distance_reminder.models.Notification;

import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NotificationsFragment extends Fragment {

    ArrayList<Notification> notifications;
    FragmentNotificationsBinding binding;
    NotificationsViewAdapter notificationsViewAdapter;

    private String TAG = "Test";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        notifications = new ArrayList<Notification>();
        notifications.add(new Notification("Test1", Calendar.getInstance().getTime(), "qwertyudfghjcvbnfghjvbn", true));
        notifications.add(new Notification("Test2", Calendar.getInstance().getTime(), "sdhsdhsdhsdfhsrhqshaha", false));
        notifications.add(new Notification("Test3", Calendar.getInstance().getTime(), "qwertyudfghjcvbnfdhahrhahadheghjvbn", false));
        notifications.add(new Notification("Test4", Calendar.getInstance().getTime(), "qwertyudfghjsdhserhsehwehrsehsdhfnstrjsrtjsjhsrjrtjstrjsryjcvbnfghjvbn", false));
        notifications.add(new Notification("Test1", Calendar.getInstance().getTime(), "qwertyudfghjcvbnfghjvbn", true));
        notifications.add(new Notification("Test2", Calendar.getInstance().getTime(), "sdhsdhsdhsdfhsrhqshaha", false));
        notifications.add(new Notification("Test3", Calendar.getInstance().getTime(), "qwertyudfghjcvbnfdhahrhahadheghjvbn", false));
        notifications.add(new Notification("Test4", Calendar.getInstance().getTime(), "qwertyudfghjsdhserhsehwehrsehsdhfnstrjsrtjsjhsrjrtjstrjsryjcvbnfghjvbn", false));
        bookListGenerate();
        return root;
    }

    private void bookListGenerate() {
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        notifications_recyclerView.setLayoutManager(layoutManager);
//        notificationsViewAdapter = new NotificationsViewAdapter(getActivity(), notifications);
//        notifications_recyclerView.setAdapter(notificationsViewAdapter);

        notificationsViewAdapter = new NotificationsViewAdapter(getActivity(), notifications);
        binding.setNotificationAdapter(notificationsViewAdapter);
    }

}