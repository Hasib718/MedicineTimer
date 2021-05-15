package com.example.medicinetimer.support;

import android.app.Application;
import android.content.SharedPreferences;

public class MedicineTimer extends Application {

    private static final String TAG = "MedicineTimer";

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
