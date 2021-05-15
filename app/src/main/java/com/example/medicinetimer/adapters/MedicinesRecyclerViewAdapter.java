package com.example.medicinetimer.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinetimer.R;
import com.example.medicinetimer.container.Medicine;
import com.example.medicinetimer.database.MedicineDatabase;
import com.example.medicinetimer.listeners.OnMedicineListClickEvents;
import com.example.medicinetimer.support.AppExecutors;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

public class MedicinesRecyclerViewAdapter extends RecyclerView.Adapter<MedicinesRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    private ArrayList<Medicine> mMedicines;
    private Context mContext;

    private OnMedicineListClickEvents medicineListClickEvents;

    public MedicinesRecyclerViewAdapter(Context mContext, ArrayList<Medicine> mMedicines) {
        this.mMedicines = mMedicines;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_medicine, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.medicineName.setText(mMedicines.get(position).getName());
        holder.medicineTime.setText(mMedicines.get(position).getTimes());
        holder.activityState.setChecked(mMedicines.get(position).isActivityState());

        holder.nameTimeLayout.setOnClickListener(v -> {
            Log.d(TAG, "onClick: " + position);

            medicineListClickEvents.onMedicineClickListener(position, mMedicines.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return mMedicines.size();
    }

    public void setMedicineListClickEvents(OnMedicineListClickEvents medicineListClickEvents) {
        this.medicineListClickEvents = medicineListClickEvents;
    }

    public void updateList(ArrayList<Medicine> medicines) {
        mMedicines = medicines;
        notifyDataSetChanged();
    }

    public ArrayList<Medicine> getList() {
        return mMedicines;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView medicineName, medicineTime;
        SwitchMaterial activityState;
        MaterialCardView medicineLayout;
        ConstraintLayout nameTimeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            medicineName = itemView.findViewById(R.id.medicineName);
            medicineTime = itemView.findViewById(R.id.medicineTime);
            activityState = itemView.findViewById(R.id.medicineActivityState);
            medicineLayout = itemView.findViewById(R.id.medicineLayout);
            nameTimeLayout = itemView.findViewById(R.id.nameTimeLayout);
            activityState.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    mMedicines.get(getAdapterPosition()).setActivityState(isChecked);
                } else {
                    mMedicines.get(getAdapterPosition()).setActivityState(isChecked);
                }

                AppExecutors.getInstance().diskIO().execute(() -> {
                    MedicineDatabase.getInstance(mContext).medicineDao().updateMedicine(mMedicines.get(getAdapterPosition()));
                });
            });
        }
    }
}
