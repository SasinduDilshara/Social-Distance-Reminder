package com.example.social_distance_reminder.db.crudhelper;

import android.util.Log;

import com.example.social_distance_reminder.auth.FirebaseAuthHelper;
import com.example.social_distance_reminder.db.crudhelper.model.DeviceModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
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
import static com.example.social_distance_reminder.helper.ServiceHelper.generateHash;

public class FirebaseCRUDHelper {
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

//    public void getFCMToken() {
//        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(this, instanceIdResult -> {
//            String newToken = instanceIdResult.getToken();
//            Log.e("newToken", newToken);
//            getActivity().getPreferences(Context.MODE_PRIVATE).edit().putString("fb", newToken).apply();
//        });
//    }

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
            data.put("userid", uid);

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

    public void updateMessageToken(String token) {

        Map<String, Object> data = new HashMap<>();
        data.put("message-token", token);
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

    public String getUserMessageTokenFromUserId(String id) {
        String uid = firebaseUser.getUid();
//        System.out.println("ID to find :- " + id);
        final String[] msgToken = {null};
        Task<DocumentSnapshot> task1 = FirebaseFirestore.getInstance()
                .collection("users")
                .document(id).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
//                                System.out.println("Keys in the document "+ document);
                                msgToken[0] =  document.get("message-token").toString();
                                System.out.println("mt:- " + msgToken[0]);
                            } else {
                                System.out.println("No such document :- " + id);
                            }
                        } else {
                            System.out.println( "get failed with "+ task.getException());
                        }
                    }
                });
        return msgToken[0];
    }

    public List<String> getTheCloseDevices() {
        System.out.println("Phone Number:- " + FirebaseAuthHelper.getCurrentUser().getPhoneNumber());
        String bluetoothid = generateHash(FirebaseAuthHelper.getCurrentUser().getPhoneNumber());
//        String bluetoothid = generateHash("+1 650-555-2234");
        System.out.println("the bluetooth id to find:- " + bluetoothid);

        List<String> closeDevices = new ArrayList<>();
        Task<QuerySnapshot> task1 = db.collectionGroup(bluetoothid).get().addOnSuccessListener(queryDocumentSnapshots -> {
            //Iterate to get the products out of the queryDocumentSnapshots object
            List<DocumentSnapshot> subdocuments = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot doc: subdocuments
                 ) {
//                   System.out.println("This is the data");
//                System.out.println(doc.getData());
//                   System.out.println("This is the data");
//                String t = getUserMessageTokenFromUserId(doc.get("userid").toString());
//                System.out.println("Results got from mt:- " + t);
                String id = doc.get("userid").toString();
                FirebaseFirestore.getInstance()
                        .collection("users")
                        .document(id).get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
//                                System.out.println("Keys in the document "+ document);
                                        String msgtoken =  document.get("message-token").toString();
                                        //TODO:Put the send token here
                                        System.out.println("mt123:- " + msgtoken +"\nid:- " + id + " for " + bluetoothid);
                                    } else {
                                        System.out.println("No such document :- " + id);
                                    }
                                } else {
                                    System.out.println( "get failed with "+ task.getException());
                                }
                            }
                        });
            }
        });
        return closeDevices;
    }

    // public void getSample() {
    //     System.out.println("Here is the getsample method");
    //     db.collectionGroup("ffe79751-e3a1-4f06-ba7b-53ea0b774cc9").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
    //         @Override
    //         public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
    //             //Iterate to get the products out of the queryDocumentSnapshots object
    //             List<DocumentSnapshot> subdocuments = queryDocumentSnapshots.getDocuments();
    //             for (DocumentSnapshot doc: subdocuments
    //                  ) {
    //                 System.out.println("This is the data");
    //                 System.out.println(doc.getData());
    //                 System.out.println("This is the data");
    //             }
    //         }
    //     });
    //     System.out.println("Here is the getsample method");
    // }


}

