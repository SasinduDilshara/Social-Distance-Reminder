package com.example.social_distance_reminder.helper;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.databinding.CardBlacklistBinding;
import com.example.social_distance_reminder.databinding.CardNotificationsBinding;
import com.example.social_distance_reminder.db.crudhelper.SqlLiteHelper;
import com.example.social_distance_reminder.models.BlacklistItem;
import com.example.social_distance_reminder.models.Notification;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class BlacklistViewAdapter extends RecyclerView.Adapter<BlacklistViewAdapter.ViewHolder> {

    private ArrayList<BlacklistItem> blackListDetails;
    private Context context;
    String TAG = "Testing";

    public BlacklistViewAdapter(Context context, ArrayList<BlacklistItem> objects) {
        this.blackListDetails = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public BlacklistViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardBlacklistBinding cardBlacklistBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.card_blacklist, parent, false);

        return new BlacklistViewAdapter.ViewHolder(cardBlacklistBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BlacklistViewAdapter.ViewHolder holder, int position) {
        BlacklistItem bl = blackListDetails.get(position);
        holder.bind(bl);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: "+String.valueOf(blackListDetails.size()));
        return blackListDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CardBlacklistBinding cardBlacklistBinding;

        public ViewHolder(@NonNull CardBlacklistBinding binding) {
            super(binding.getRoot());
            this.cardBlacklistBinding = binding;
        }

        public void bind(BlacklistItem blacklistItem) {
            cardBlacklistBinding.setBlacklistItem(blacklistItem);
            cardBlacklistBinding.executePendingBindings();
        }
    }
}
