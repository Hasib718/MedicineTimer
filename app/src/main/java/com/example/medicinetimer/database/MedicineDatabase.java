package com.example.medicinetimer.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.medicinetimer.container.Medicine;

@Database(entities = {Medicine.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class MedicineDatabase extends RoomDatabase {
    private static final String TAG = MedicineDatabase.class.getSimpleName();
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "medicineList_db";

    private static MedicineDatabase sInstance;

    public static MedicineDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(), MedicineDatabase.class, MedicineDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }

        return sInstance;
    }

    public abstract MedicineDao medicineDao();
}
