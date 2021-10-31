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

import com.example.social_distance_reminder.databinding.FragmentDashboardBinding;
import com.example.social_distance_reminder.models.DashboardElement;
import com.example.social_distance_reminder.ui.TestActivity;

public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.buttonDashboard.setOnClickListener(this::redirectToHome);
        DashboardElement de = new DashboardElement(4,9,3,6,2,5);
        binding.setElement(de);
        return root;
    }

    public void redirectToHome(View view) {
        Intent homePage = new Intent(getActivity(), TestActivity.class);
        startActivity(homePage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}