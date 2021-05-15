package com.example.medicinetimer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinetimer.adapters.MedicinesRecyclerViewAdapter;
import com.example.medicinetimer.container.Medicine;
import com.example.medicinetimer.database.MedicineDatabase;
import com.example.medicinetimer.fragments.MedicineInfoFragment;
import com.example.medicinetimer.listeners.OnMedicineListClickEvents;
import com.example.medicinetimer.support.AppExecutors;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMedicineListClickEvents {

    private static final String TAG = "MainActivity";

    private FloatingActionButton medicineAddingButton;

    private RecyclerView medicineList;
    private MedicinesRecyclerViewAdapter adapter;
    private ArrayList<Medicine> medicines = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRecyclerView();

        medicineAddingButton = findViewById(R.id.medicineAddingButton);
        medicineAddingButton.setOnClickListener((v) -> {
            startActivity(new Intent(MainActivity.this, MedicineAddingActivity.class));
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveDataFromDB();
    }

    private void retrieveDataFromDB() {
        AppExecutors
                .getInstance()
                .diskIO()
                .execute(() -> {
                    final List<Medicine> fromDB = MedicineDatabase
                            .getInstance(MainActivity.this)
                            .medicineDao()
                            .loadAllMedicines();

                    runOnUiThread(() -> {
                        Log.d(TAG, "onResume: " + fromDB.toString());
                        adapter.updateList((ArrayList<Medicine>) fromDB);
                    });
                });
    }

    private void initRecyclerView() {
        medicineList = findViewById(R.id.medicineList);
        adapter = new MedicinesRecyclerViewAdapter(this, medicines);
        medicineList.setAdapter(adapter);
        medicineList.setLayoutManager(new LinearLayoutManager(this));

        adapter.setMedicineListClickEvents(this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                AppExecutors
                        .getInstance()
                        .diskIO()
                        .execute(() -> {
                            int position = viewHolder.getAdapterPosition();
                            MedicineDatabase
                                    .getInstance(MainActivity.this)
                                    .medicineDao()
                                    .deleteMedicine(adapter.getList().get(position));

                            retrieveDataFromDB();
                        });
            }
        }).attachToRecyclerView(medicineList);
    }

    @Override
    public void onMedicineClickListener(int position, Medicine medicine) {
        Log.d(TAG, "onMedicineClickListener: " + position);

        MedicineInfoFragment medicineInfoFragment = new MedicineInfoFragment(this, medicine);
        medicineInfoFragment.show(getSupportFragmentManager(), "Medicine Info");
    }
}
