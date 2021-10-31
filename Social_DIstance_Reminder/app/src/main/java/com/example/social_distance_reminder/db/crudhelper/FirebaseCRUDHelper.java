package com.example.social_distance_reminder.db.crudhelper;

import android.util.Log;

import com.example.social_distance_reminder.db.crudhelper.model.DeviceModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
            GeoPoint location = new GeoPoint(deviceModel.getLatitude(),deviceModel.getLongitude());

            Map<String, Object> data = new HashMap<>();
            data.put("timestamp", date);
            data.put("location",location);

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

    public void get() {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();//firebaseUser.getUid()
//        Task<DocumentSnapshot> nearDevices =  db.collection("users").document(firebaseUser.getUid())
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    System.out.println("task.getResult() :- " + task.getResult().get + " , ");
//                    List<String> list = new ArrayList<>();
////                    for (DocumentSnapshot document : task.getResult()) {
////                        System.out.println("document.getData() :- " + document.getData() + " , ");
//////                        System.out.println("document.getData() :- " + document.getDocumentReference() + " , ");
////                        list.add(document.getId());
////                    }
////                    Log.d(TAG, list.toString());
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//            }
//        });

        db.collection("users").document("Document").getCollections().then((querySnapshot) => {
                querySnapshot.forEach((collection) => {
                        console.log("collection: " + collection.id);
        });
    });

    }

//    Task<QuerySnapshot> collection =
//            db.collection("users").document(firebaseUser.getUid()).collection(firebaseUser.getUid()).get();


}

