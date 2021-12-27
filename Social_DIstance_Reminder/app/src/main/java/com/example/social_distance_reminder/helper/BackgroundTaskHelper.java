package com.example.social_distance_reminder.helper;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.social_distance_reminder.db.crudhelper.FirebaseCRUDHelper;
import com.example.social_distance_reminder.db.crudhelper.SqlLiteHelper;
import com.example.social_distance_reminder.db.crudhelper.model.DeviceModel;

import java.util.List;

import static android.content.ContentValues.TAG;

public class BackgroundTaskHelper {
    public static class UploadTask extends AsyncTask<Context, Void, Context> {
        List<DeviceModel> list;

        @Override
        protected Context doInBackground(Context... contexts) {
            System.out.println("Do in Background to update FB");
            Context context = contexts[0];
            SqlLiteHelper databaseHelper = SqlLiteHelper.getInstance(context);
            list = databaseHelper.getDevices();
            boolean isInternetConnectionAvailable = ServiceHelper.isInternetAvailable();
            if (isInternetConnectionAvailable) {
                return context;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Context context) {
            System.out.println("Do in onPostExecute to update FB");
            super.onPostExecute(context);
            if (context != null) {
                SqlLiteHelper databaseHelper = SqlLiteHelper.getInstance(context);
                FirebaseCRUDHelper firebaseHelper = new FirebaseCRUDHelper();
                if (list != null && list.size() != 0) {
                    Log.d(TAG, "onPostExecute: firebase update method called");
                    for (DeviceModel deviceModel : list) {
                        firebaseHelper.update(deviceModel, databaseHelper);
                    }
                }

            }
        }
    }
}
