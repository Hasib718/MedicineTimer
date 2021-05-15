package com.example.medicinetimer.listeners;

public interface OnDaysDurationPickerEvents {
    void onDaysPickerCancel(String invokedClassName);

    void onDaysPickerSet(String days, String invokedClassName);
}
