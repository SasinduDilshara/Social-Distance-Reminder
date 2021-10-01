package com.example.social_distance_reminder.db.crudhelper;

import android.util.Log;

import com.example.social_distance_reminder.db.crudhelper.model.DeviceModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;

import static android.content.ContentValues.TAG;

public class FirebaseCRUDHelper {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public void update(final DeviceModel deviceModel, final SqlLiteHelper sqlLiteHelper) {

        //timestamp format --> "2020-05-18 07:42:24"
        String uid = firebaseUser.getUid();
        String owner_id =  sqlLiteHelper.getUserId();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            Date date = dateFormat.parse(deviceModel.getTimeStamp());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //TODO: HANDLE THIS
            calendar.add(Calendar.HOUR_OF_DAY,5);
            calendar.add(Calendar.MINUTE,30);
            date = calendar.getTime();
//            System.out.println(date);
//            GeoPoint location = new GeoPoint(deviceModel.getLatitude(),deviceModel.getLongitude());

            Map<String, Object> data = new HashMap<>();
            data.put("timestamp", date);
//            data.put("location",location);

            db.collection("users")
                    .document(uid)
                    .collection(deviceModel.getUserID())
                    .document()
                    .set(data)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "onSuccess: " + deviceModel.getUserID());
                            sqlLiteHelper.deleteDevice(deviceModel.getID());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: failed to upload id " + deviceModel.getUserID() +"" +
                                    "\nException - " + e.getMessage());
                        }
                    });
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void onCreteUser(String id){
        Map<String, Object> data = new HashMap<>();
        data.put("bluetooth identifier",id);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        db.collection("users")
                .document(firebaseUser.getUid())
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        System.out.println("Success");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        System.out.println("fail");
                    }
                });
    }


}

