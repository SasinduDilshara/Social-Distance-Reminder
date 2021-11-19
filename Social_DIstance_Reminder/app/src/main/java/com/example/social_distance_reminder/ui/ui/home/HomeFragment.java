package com.example.social_distance_reminder.ui.ui.home;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.social_distance_reminder.R;
import com.example.social_distance_reminder.databinding.FragmentHomeBinding;
import com.example.social_distance_reminder.databinding.PopupDeclarationBinding;
import com.example.social_distance_reminder.models.Notification;
import com.example.social_distance_reminder.notifications.MySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Carousel;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {

    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=AAAA6NYYD9E:APA91bHkC_6fqKP8rh3_GaCK6mRRSKqBcZRRI7Nb_jzkjuFGgzjMJr38jn3khw7CHFKuJJNXF0l5qAK8jLBxsEO7X4YUTA53dJi4j9GshqPMRHdsKAC7HZKLMO1UuEPTXg8X4bjanAk9\t\n";
    final private String contentType = "application/json";
    private FragmentHomeBinding homeBinding;
    private PopupDeclarationBinding popupBinding;
    private Dialog declarePopup;
    int[] images = {R.drawable.covid_1, R.drawable.covid_2, R.drawable.covid_3, R.drawable.covid_4, R.drawable.covid_5, R.drawable.covid_6};
    private String TAG = "Testing";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

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
        popupBinding.lblDeclareRandom.setText(getRandomString(8));
        popupBinding.txtDeclareRandom.setText("");
        popupBinding.btnDeclareConfirm.setOnClickListener(v2 -> {
            if (popupBinding.lblDeclareRandom.getText().toString().equals(popupBinding.txtDeclareRandom.getText().toString())) {
                Toast.makeText(getActivity(), "Declaration Succeeded", Toast.LENGTH_SHORT).show();
                Notification declaration = new Notification("WARNING", new Date(),"A person who came near you has declared as a COVID patient",true);
                sendDeclarationNotification(createNotificationObject(declaration));
            } else {
                Toast.makeText(getActivity(), "Wrong", Toast.LENGTH_SHORT).show();
            }
        });
        declarePopup.show();
    }

    public String getRandomString(int n) {
        String AlphaNumericString = "ABCDEFGHJKLMNPQRSTUVWXYZ"
                + "123456789"
                + "abcdefghijkmnopqrstuvxyz";

        StringBuilder sb = new StringBuilder(n);

        Random random = new Random();

        for (int i = 0; i < n; i++) {

            int index = random.nextInt(AlphaNumericString.length());
            sb.append(AlphaNumericString
                    .charAt(index));
        }

        return sb.toString();
    }

    private JSONObject createNotificationObject(Notification notification){
        JSONObject notificationObject = new JSONObject();
        JSONObject notificationBody = new JSONObject();
        try {
            notificationBody.put("title",notification.getTitle());
            notificationBody.put("message",notification.getDescription());
            notificationBody.put("date",notification.getDate());
            notificationBody.put("importance",notification.isImportant());
            notificationObject.put("to", "d5lLePz0SQu_r-4WAU3hDr:APA91bGTgSjQn5pthRDeqqy7jINhXT3JRB_KRWybHjoFBbzdkReQT1PwtfHg-gutSd7uXYGd88rjIWI8wFm0O-LJHPWyioDa2CsVm6WQMjpl6A2Ex4aRquiYr9Kop4JXrrl6NigEHGMT");
//                notification.put("to", "cJBFXY1FQzCcqHjmepWc_t:APA91bEaSjUd5wzC0dUc-NOBTTaFIDKv1fBW2ZrN3o5uayfzqRl4RJbkhpo4IEGOOEtZlCF5ZwjmHuXIRYQaz5BtPK1s1l_AAiiqiG1yWWq2KCjS7Oi20Ad2ddTWJC7rM2XtjnP3kYs_");
//            notificationObject.put("to", "/topics/All");
            notificationObject.put("data", notificationBody);
        } catch (JSONException e) {
            Log.e(TAG, "onCreate: " + e.getMessage());
        }
        return notificationObject;
    }

    private void sendDeclarationNotification(JSONObject notification) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(FCM_API, notification,
                response -> Log.i(TAG, "onResponse: " + response.toString()),
                error -> {
                    Toast.makeText(getContext(), "Request error", Toast.LENGTH_LONG).show();
                    Log.i(TAG, "onErrorResponse: Didn't work");
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        homeBinding = null;
    }
}