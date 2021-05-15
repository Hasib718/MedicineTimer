package com.example.medicinetimer.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.medicinetimer.R;
import com.example.medicinetimer.listeners.OnDaysDurationPickerEvents;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;

public class DaysIntervalFragment extends AppCompatDialogFragment {
    private static final String TAG = "DaysIntervalFragment";

    private Context mContext;
    private String days;

    private TextInputEditText daysCount;
    private MaterialButton cancelButton, setButton;
    private AppCompatImageButton minusButton, plusButton;
    private MaterialTextView title;

    private OnDaysDurationPickerEvents onDaysDurationPickerEvents;

    public DaysIntervalFragment() { }

    public DaysIntervalFragment(Context mContext, String days) {
        this.mContext = mContext;
        this.days = days;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_duration_days, null);

        initViews(view);
        setDoseCounter();
        setActionButtons();

        title.setText("Set days interval");
        daysCount.setTextSize(16f);
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) daysCount.getLayoutParams();
        lp.width = 280;
        lp.topMargin = 45;
        daysCount.setLayoutParams(lp);
        ConstraintLayout.LayoutParams lp1 = (ConstraintLayout.LayoutParams) minusButton.getLayoutParams();
        lp1.leftMargin = 170;
        minusButton.setLayoutParams(lp1);

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .setCancelable(true)
                .create();

        return dialog;
    }

    public void setOnDaysDurationPickerEvents(OnDaysDurationPickerEvents onDaysDurationPickerEvents) {
        this.onDaysDurationPickerEvents = onDaysDurationPickerEvents;
    }

    private void setActionButtons() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDaysDurationPickerEvents.onDaysPickerCancel(DaysIntervalFragment.this.getClass().getSimpleName());
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDaysDurationPickerEvents.onDaysPickerSet(daysCount.getText().toString(), DaysIntervalFragment.this.getClass().getSimpleName());
            }
        });
    }

    private void setDoseCounter() {
        daysCount.setText(days);

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer days = Integer.parseInt(daysCount.getText().toString().replaceAll("[^0-9]", ""));
                days -= 1;

                if (days < 0) {
                    days = 0;
                    daysCount.setText(String.format("Every %d days", days));
                    Toast.makeText(mContext, "Days can't be less then "+days, Toast.LENGTH_SHORT).show();
                } else {
                    daysCount.setText(String.format("Every %d days", days));
                }
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer days = Integer.parseInt(daysCount.getText().toString().replaceAll("[^0-9]", ""));
                days += 1;

                if (days > 365) {
                    days = 365;
                    daysCount.setText(String.format("Every %d days", days));
                    Toast.makeText(mContext, "Dose can't be greater then "+days, Toast.LENGTH_SHORT).show();
                } else {
                    daysCount.setText(String.format("Every %d days", days));
                }
            }
        });
    }


    private void initViews(View view) {
        daysCount = view.findViewById(R.id.daysCount);
        minusButton = view.findViewById(R.id.minusButton);
        plusButton = view.findViewById(R.id.plusButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        setButton = view.findViewById(R.id.setButton);
        title = view.findViewById(R.id.title);
    }
}
