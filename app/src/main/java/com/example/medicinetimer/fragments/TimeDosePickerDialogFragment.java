package com.example.medicinetimer.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatImageButton;

import com.example.medicinetimer.R;
import com.example.medicinetimer.container.MedicineDose;
import com.example.medicinetimer.listeners.OnTimeDosePickerDialogActionButtonEvents;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;

import java.text.ParseException;

public class TimeDosePickerDialogFragment extends AppCompatDialogFragment {

    private static final String TAG = "TimeDosePickerDialogFragment";

    private Context mContext;
    private MedicineDose medicineDose;

    private TimePicker timePicker;
    private AppCompatImageButton minusButton, plusButton;
    private MaterialTextView doseCount;
    private MaterialButton cancelButton, setButton;

    private OnTimeDosePickerDialogActionButtonEvents onTimeDosePickerDialogActionButtonEvents;

    public TimeDosePickerDialogFragment(Context mContext, MedicineDose medicineDose) {
        this.mContext = mContext;
        this.medicineDose = medicineDose;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_time_dose_picker, null);

        initViews(view);
        setTimePicker();
        setDoseCounter();
        setActionButtons();

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setView(view)
                .setCancelable(true)
                .create();

        return dialog;
    }

    public void setOnTimeDosePickerDialogActionButtonEvents(OnTimeDosePickerDialogActionButtonEvents onTimeDosePickerDialogActionButtonEvents) {
        this.onTimeDosePickerDialogActionButtonEvents = onTimeDosePickerDialogActionButtonEvents;
    }

    private void setActionButtons() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeDosePickerDialogActionButtonEvents.onTimeDosePickerCancel();
            }
        });

        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTimeDosePickerDialogActionButtonEvents.onTimeDosePickerSet(doseCount.getText().toString());
            }
        });
    }

    private void setDoseCounter() {
        doseCount.setText(String.format("%.2f", medicineDose.getDoseCount()));

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double dose = Double.parseDouble(doseCount.getText().toString());
                dose -= 0.25;

                if (dose < 0) {
                    dose = 0.00;
                    doseCount.setText(String.format("%.2f", dose));
                    Toast.makeText(mContext, "Dose can't be less then "+dose, Toast.LENGTH_SHORT).show();
                } else {
                    doseCount.setText(String.format("%.2f", dose));
                }
            }
        });
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Double dose = Double.parseDouble(doseCount.getText().toString());
                dose += 0.25;

                if (dose > 100) {
                    dose = 100.00;
                    doseCount.setText(String.format("%.2f", dose));
                    Toast.makeText(mContext, "Dose can't be greater then "+dose, Toast.LENGTH_SHORT).show();
                } else {
                    doseCount.setText(String.format("%.2f", dose));
                }
            }
        });
    }

    private void setTimePicker() {
        timePicker.setIs24HourView(false);
        timePicker.setEnabled(true);
        timePicker.setOnTimeChangedListener((TimePicker.OnTimeChangedListener) mContext);

        SimpleDateFormat format = new SimpleDateFormat("hh:mm aa");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(medicineDose.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timePicker.setHour(calendar.getTime().getHours());
        timePicker.setMinute(calendar.getTime().getMinutes());
    }

    private void initViews(View view) {
        timePicker = view.findViewById(R.id.timePicker);
        doseCount = view.findViewById(R.id.doseCount);
        minusButton = view.findViewById(R.id.minusButton);
        plusButton = view.findViewById(R.id.plusButton);
        cancelButton = view.findViewById(R.id.cancelButton);
        setButton = view.findViewById(R.id.setButton);
    }
}
