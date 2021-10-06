package com.example.social_distance_reminder.ui.ui.home;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.databinding.FragmentHomeBinding;
import com.example.social_distance_reminder.databinding.PopupDeclarationBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding homeBinding;
    private PopupDeclarationBinding popupBinding;
    private Dialog declarePopup;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        homeBinding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = homeBinding.getRoot();

        declarePopup = new Dialog(getActivity());
        popupBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.popup_declaration, null, false);
        declarePopup.setContentView(popupBinding.getRoot());

        homeBinding.btnHomeDeclare.setOnClickListener(v2 -> showDeclarationPopup());
        return root;
    }

    public void showDeclarationPopup() {
        popupBinding.btnDeclareClose.setOnClickListener(v2 -> declarePopup.dismiss());
        popupBinding.lblDeclareRandom.setText("Random1234");
        popupBinding.txtDeclareRandom.setText("");
        popupBinding.btnDeclareConfirm.setOnClickListener(v2 -> {
            if (popupBinding.lblDeclareRandom.getText().toString().equals(popupBinding.txtDeclareRandom.getText().toString())) {
                Toast.makeText(getActivity(), "Declaration Succeeded", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Wrong", Toast.LENGTH_SHORT).show();
            }
        });
        declarePopup.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeBinding = null;
    }
}