package com.example.medicinetimer.listeners;

import com.example.medicinetimer.container.MedicineDose;

public interface OnTimeListClickEvents {
    void onTimeClickListener(int position, String selected);

    void onTimeDoseClickListener(int position, MedicineDose medicineDose);
}
