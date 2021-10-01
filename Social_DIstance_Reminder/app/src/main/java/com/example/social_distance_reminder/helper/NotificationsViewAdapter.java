package com.example.social_distance_reminder.helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.models.Notification;
import com.example.social_distance_reminder.databinding.CardNotificationsBinding;


public class NotificationsViewAdapter extends RecyclerView.Adapter<NotificationsViewAdapter.ViewHolder> {

    private ArrayList<Notification> notifications;
    private Context context;

    public NotificationsViewAdapter(Context context, ArrayList<Notification> objects) {
        this.notifications = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationsViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardNotificationsBinding cardNotificationsBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.card_notifications, parent, false);

        return new ViewHolder(cardNotificationsBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notification notification = notifications.get(position);
        holder.bind(notification);
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardNotificationsBinding cardNotificationsBinding;

        public ViewHolder(@NonNull CardNotificationsBinding binding) {
            super(binding.getRoot());
            this.cardNotificationsBinding = binding;
        }

        public void bind(Notification notification) {
            cardNotificationsBinding.setNotification(notification);
            cardNotificationsBinding.executePendingBindings();
        }
    }
}
