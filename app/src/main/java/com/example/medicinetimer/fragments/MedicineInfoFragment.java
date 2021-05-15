package com.example.medicinetimer.fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinetimer.MedicineAddingActivity;
import com.example.medicinetimer.R;
import com.example.medicinetimer.adapters.TimeDoseRecyclerViewAdapter;
import com.example.medicinetimer.constants.Constants;
import com.example.medicinetimer.container.Medicine;
import com.google.android.material.textview.MaterialTextView;

public class MedicineInfoFragment extends AppCompatDialogFragment {

    private static final String TAG = "MedicineInfoFragment";

    private Context mContext;
    private Medicine mMedicine;

    private RecyclerView recyclerView;
    private TimeDoseRecyclerViewAdapter adapter;
    private MaterialTextView medicineStartDate, medicineDuration, medicineDays;

    public MedicineInfoFragment(Context mContext, Medicine mMedicine) {
        this.mContext = mContext;
        this.mMedicine = mMedicine;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_medicine_info, null);

        initViews(view);

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle(mMedicine.getName())
                .setView(view)
                .setCancelable(true)
                .setPositiveButton("Edit", (dialog1, which) -> {
                    Intent intent = new Intent(mContext, MedicineAddingActivity.class);
                    intent.putExtra(Constants.UPDATE_MEDICINE_INFO, mMedicine);
                    mContext.startActivity(intent);
                })
                .setNegativeButton("Cancel", (dialog1, which) -> {

                })
                .create();

        return dialog;
    }

    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.timeDoseRecyclerViewTable);
        adapter = new TimeDoseRecyclerViewAdapter(mContext, mMedicine.getMedicineDoses());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        medicineStartDate = view.findViewById(R.id.medicineStartDateInput);
        medicineDuration = view.findViewById(R.id.durationText);
        medicineDays = view.findViewById(R.id.daysInterval);

        medicineStartDate.setText(mMedicine.getStartingDay());

        if (mMedicine.isContinuous()) {
            medicineDuration.setText("Continuous");
        } else {
            medicineDuration.setText("Number of days : " + mMedicine.getDaysCount());
        }

        if (mMedicine.isEveryday()) {
            medicineDays.setText("Everyday");
        } else if (mMedicine.isDaysInterval()) {
            medicineDays.setText("Interval every " + mMedicine.getDaysIntervalCount() + " days");
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Specific day/s of week : ");
            mMedicine.getSpecifiedDays().forEach((k, v) -> {
                if (v) {
                    stringBuilder.append(k.substring(0, 3))
                            .append(", ");
                }
            });
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1);

            medicineDays.setText(stringBuilder.toString());
        }
    }
}
