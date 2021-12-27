package com.example.social_distance_reminder.ui.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import com.example.social_distance_reminder.db.crudhelper.SqlLiteHelper;

import com.example.social_distance_reminder.databinding.FragmentDashboardBinding;
import com.example.social_distance_reminder.db.crudhelper.model.Stats;
import com.example.social_distance_reminder.models.DashboardElement;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private Stats stat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        stat = SqlLiteHelper.getInstance(getContext()).getStats();
        DashboardElement de =
                new DashboardElement(stat.getMinDistance(), stat.getCloseCount(), stat.getLastMeetupTime(), stat.getNumDeclaration());
        binding.setElement(de);
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}