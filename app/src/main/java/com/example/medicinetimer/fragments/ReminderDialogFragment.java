package com.example.medicinetimer.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinetimer.MedicineAddingActivity;
import com.example.medicinetimer.R;
import com.example.medicinetimer.adapters.TimeDoseRecyclerViewAdapter;
import com.example.medicinetimer.listeners.OnTimeListClickEvents;
import com.example.medicinetimer.listeners.OnTimeListItemSelectionEvents;

import java.util.ArrayList;

public class ReminderDialogFragment extends AppCompatDialogFragment {

    private static final String TAG = "ReminderDialogFragment";

    private Context mContext;
    private ArrayList<String> mTimeList;

    private RecyclerView timeTableRecyclerView;
    private TimeDoseRecyclerViewAdapter adapter;

    public ReminderDialogFragment(Context mContext, ArrayList<String> mTimeList) {
        this.mContext = mContext;
        this.mTimeList = mTimeList;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_medicine_time_selection_table, null);

        initViews(view);

        ((MedicineAddingActivity) getActivity()).setOnTimeListItemSelectionEvents(new OnTimeListItemSelectionEvents() {
            @Override
            public void onItemSelectedListener(ArrayList<String> timeTable) {
                mTimeList.clear();
                adapter.notifyDataSetChanged();

                mTimeList.addAll(timeTable);
                adapter.notifyDataSetChanged();
            }
        });

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .setCancelable(true)
                .create();

        return dialog;
    }

    private void initViews(View view) {
        timeTableRecyclerView = view.findViewById(R.id.timeSelectionRecyclerView);
        adapter = new TimeDoseRecyclerViewAdapter(mTimeList, mContext) {
            @Override
            public void onBindViewHolder(@NonNull TimeDoseRecyclerViewAdapter.ViewHolder holder, final int position) {
                holder.dose.setVisibility(View.GONE);
                holder.underlineView.setVisibility(View.GONE);
                holder.time.setText(getmTimeList().get(position));

                holder.timeDoseLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onTimeListClickEvents.onTimeClickListener(position, getmTimeList().get(position));
                    }
                });
            }
        };
        timeTableRecyclerView.setAdapter(adapter);
        timeTableRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));

        adapter.setOnTimeListClickEvents((OnTimeListClickEvents) mContext);
    }
}
