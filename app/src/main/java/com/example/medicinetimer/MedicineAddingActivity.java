package com.example.medicinetimer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicinetimer.adapters.TimeDoseRecyclerViewAdapter;
import com.example.medicinetimer.constants.Constants;
import com.example.medicinetimer.container.Medicine;
import com.example.medicinetimer.container.MedicineDose;
import com.example.medicinetimer.database.MedicineDatabase;
import com.example.medicinetimer.fragments.DaysDurationFragment;
import com.example.medicinetimer.fragments.DaysIntervalFragment;
import com.example.medicinetimer.fragments.ReminderDialogFragment;
import com.example.medicinetimer.fragments.TimeDosePickerDialogFragment;
import com.example.medicinetimer.listeners.OnDaysDurationPickerEvents;
import com.example.medicinetimer.listeners.OnTimeDosePickerDialogActionButtonEvents;
import com.example.medicinetimer.listeners.OnTimeListClickEvents;
import com.example.medicinetimer.listeners.OnTimeListItemSelectionEvents;
import com.example.medicinetimer.support.AppExecutors;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class MedicineAddingActivity extends AppCompatActivity implements OnTimeListClickEvents,
        TimePicker.OnTimeChangedListener, OnTimeDosePickerDialogActionButtonEvents, OnDaysDurationPickerEvents {

    private static final String TAG = "MedicineAddingActivity";

    private TextInputLayout medicineNameInputLayout;
    private TextInputEditText medicineNameInput;
    private MaterialTextView medicineTimeInput, medicineStartDateInput, medicineDaysDuration,
            medicineSpecificDays, medicineDaysInterval;

    private RecyclerView medicineTimeDoseRecyclerView;
    private TimeDoseRecyclerViewAdapter adapter;
    private ArrayList<MedicineDose> selectedMedication = new ArrayList<>(Arrays.asList(new MedicineDose("8:00 AM", 1.00)));
    private ArrayList<ArrayList<String>> timeTable = new ArrayList<>();

    private RadioGroup durationRadioGroup, daysRadioGroup;

    private MenuItem saveOption;

    private Medicine medicine = new Medicine();
    private MedicineDose medicineDose;
    private int position;

    private ReminderDialogFragment dialogFragment;
    private TimeDosePickerDialogFragment timeDialogFragment;
    private DaysDurationFragment daysDurationFragment = new DaysDurationFragment();
    private DaysIntervalFragment daysIntervalFragment = new DaysIntervalFragment();

    private OnTimeListItemSelectionEvents onTimeListItemSelectionEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_adding);

        initViews();
        setStartDate();
        setReminderTime();
        setDuration();
        setDays();

        if (getIntent() != null && getIntent().hasExtra(Constants.UPDATE_MEDICINE_INFO)) {
            medicine = getIntent().getExtras().getParcelable(Constants.UPDATE_MEDICINE_INFO);
            Log.d(TAG, "onCreate: " + medicine);

            updateInfo();
        }
    }

    private void updateInfo() {
        medicineNameInput.setText(medicine.getName());
        medicineTimeInput.setText(medicine.getMedicineDosesType());
        adapter.updateTimeDoseList(medicine.getMedicineDoses());
        medicineStartDateInput.setText(medicine.getStartingDay());
        if (medicine.isNumberOfDays()) {
            ((MaterialRadioButton) durationRadioGroup.getChildAt(1)).setChecked(medicine.isNumberOfDays());
            medicineDaysDuration.setVisibility(View.VISIBLE);
            medicineDaysDuration.setText(medicine.getDaysCount());
        } else {
            ((MaterialRadioButton) durationRadioGroup.getChildAt(0)).setChecked(medicine.isContinuous());
        }
        if (medicine.isSpecificDay()) {
            ((MaterialRadioButton) daysRadioGroup.getChildAt(1)).setChecked(medicine.isSpecificDay());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(": ");
            medicine.getSpecifiedDays().forEach((k, v) -> {
                if (v) {
                    stringBuilder.append(k.substring(0, 3))
                            .append(", ");
                }
            });
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1);
            medicineSpecificDays.setVisibility(View.VISIBLE);
            medicineSpecificDays.setText(stringBuilder);
        } else if (medicine.isDaysInterval()) {
            ((MaterialRadioButton) daysRadioGroup.getChildAt(2)).setChecked(medicine.isDaysInterval());
            medicineDaysInterval.setVisibility(View.VISIBLE);
            medicineDaysInterval.setText(medicine.getDaysIntervalCount());
        } else {
            ((MaterialRadioButton) daysRadioGroup.getChildAt(0)).setChecked(medicine.isEveryday());
        }
    }

    private void setDays() {
        medicine.setEveryday(true);
        ((RadioButton) daysRadioGroup.getChildAt(0)).setChecked(true);

        daysRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.everydayRadioButton:
                        medicineDaysInterval.setVisibility(View.INVISIBLE);
                        medicineDaysInterval.setText("Every 2 days");
                        medicineSpecificDays.setVisibility(View.INVISIBLE);
                        break;

                    case R.id.specificRadioButton:
                        medicineSpecificDays.setVisibility(View.VISIBLE);
                        medicineDaysInterval.setVisibility(View.INVISIBLE);
                        medicineDaysInterval.setText("Every 2 days");

                        findViewById(checkedId).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                daysSelectionDialog();
                            }
                        });
                        break;

                    case R.id.intervalRadioButton:
                        medicineDaysInterval.setVisibility(View.VISIBLE);
                        medicineSpecificDays.setVisibility(View.INVISIBLE);

                        findViewById(checkedId).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                daysIntervalFragment = new DaysIntervalFragment(MedicineAddingActivity.this,
                                        medicineDaysInterval.getText().toString());
                                daysIntervalFragment.show(getSupportFragmentManager(), "Days Interval");
                                daysIntervalFragment.setOnDaysDurationPickerEvents(MedicineAddingActivity.this);
                            }
                        });
                        break;
                }

                medicine.setEveryday(((RadioButton) daysRadioGroup.getChildAt(0)).isChecked());
                medicine.setSpecificDay(((RadioButton) daysRadioGroup.getChildAt(1)).isChecked());
                medicine.setDaysInterval(((RadioButton) daysRadioGroup.getChildAt(2)).isChecked());
            }
        });
    }

    private void daysSelectionDialog() {
        LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.fragment_specified_days_selection, null);
        final GridLayout daysGrid = view.findViewById(R.id.daysGrid);
        final HashMap<String, Boolean> days = new HashMap<>(medicine.getSpecifiedDays());

        if (getIntent().hasExtra(Constants.UPDATE_MEDICINE_INFO)) {
            for (int i = 0; i < daysGrid.getChildCount(); i++) {
                ((MaterialCheckBox) daysGrid.getChildAt(i)).setChecked(medicine.getSpecifiedDays().get(((MaterialCheckBox) daysGrid.getChildAt(i)).getText().toString()));
            }
        } else {
            for (int i = 0; i < daysGrid.getChildCount(); i++) {
                ((MaterialCheckBox) daysGrid.getChildAt(i)).setChecked(days.get(((MaterialCheckBox) daysGrid.getChildAt(i)).getText().toString()));
            }
        }

        for (int i = 0; i < daysGrid.getChildCount(); i++) {
            daysGrid.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    days.put(((MaterialCheckBox) v).getText().toString(), ((MaterialCheckBox) v).isChecked());
                }
            });
        }

        new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        medicine.setSpecifiedDays(days);
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(": ");
                        days.forEach((k, v) -> {
                            if (v) {
                                stringBuilder.append(k.substring(0, 3))
                                        .append(", ");
                            }
                        });
                        stringBuilder.delete(stringBuilder.length()-2, stringBuilder.length()-1);
                        Log.d(TAG, "onClick: "+stringBuilder);
                        medicineSpecificDays.setText(stringBuilder);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();
    }

    private void setDuration() {
        medicine.setContinuous(true);
        ((RadioButton) durationRadioGroup.getChildAt(0)).setChecked(true);
        durationRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.numberDaysRadioButton:
                        medicineDaysDuration.setVisibility(View.VISIBLE);

                        findViewById(checkedId).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                daysDurationFragment = new DaysDurationFragment(
                                        MedicineAddingActivity.this, medicineDaysDuration.getText().toString());
                                daysDurationFragment.show(getSupportFragmentManager(), "Days Duration");
                                daysDurationFragment.setOnDaysDurationPickerEvents(MedicineAddingActivity.this);
                            }
                        });
                        break;
                    case R.id.continuousRadioButton:
                        medicineDaysDuration.setVisibility(View.INVISIBLE);
                        medicineDaysDuration.setText("7");
                        break;
                }

                medicine.setContinuous(((RadioButton) durationRadioGroup.getChildAt(0)).isChecked());
                medicine.setNumberOfDays(((RadioButton) durationRadioGroup.getChildAt(1)).isChecked());
            }
        });
    }

    private void setReminderTime() {
        medicineTimeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeTable.clear();
                retrieveTimeTableData();

                dialogFragment = new ReminderDialogFragment(MedicineAddingActivity.this, timeTable.get(0));
                dialogFragment.show(getSupportFragmentManager(), "Reminder Time");
            }
        });
    }

    private void setStartDate() {
        String date = LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT));
        medicineStartDateInput.setText(date);
        medicine.setStartingDay(date);
    }

    private void retrieveTimeTableData() {
        try (InputStream is = getAssets().open("Time Table.json")) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject object = new JSONObject(json);

            for (int i = 0; i < object.getJSONArray("iterator").length(); i++) {
                String[] array = object.getJSONArray(object.getJSONArray("iterator").getString(i)).join(",").split("\",\"");
                array[0] = array[0].replace("\"", "");
                array[array.length - 1] = array[array.length - 1].replace("\"", "");
                timeTable.add(new ArrayList<String>(Arrays.asList(array)));
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void initViews() {
        medicineNameInputLayout = findViewById(R.id.nameTextInputLayout);
        medicineNameInput = findViewById(R.id.medicineNameInput);
        medicineTimeInput = findViewById(R.id.reminderTimeInput);
        medicineStartDateInput = findViewById(R.id.medicineStartDateInput);

        initTimeDoseRecyclerView();

        durationRadioGroup = findViewById(R.id.radioGroup1);
        medicineDaysDuration = findViewById(R.id.numberOfDaysDuration);
        daysRadioGroup = findViewById(R.id.radioGroup2);
        medicineSpecificDays = findViewById(R.id.specificDays);
        medicineDaysInterval = findViewById(R.id.daysInterval);
    }

    private void initTimeDoseRecyclerView() {
        medicineTimeDoseRecyclerView = findViewById(R.id.timeDoseRecyclerViewTable);
        adapter = new TimeDoseRecyclerViewAdapter(this, selectedMedication);
        medicineTimeDoseRecyclerView.setAdapter(adapter);
        medicineTimeDoseRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnTimeListClickEvents(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items_medicine_adding, menu);

        saveOption = menu.findItem(R.id.item_save);

        if (getIntent().hasExtra(Constants.UPDATE_MEDICINE_INFO)) {
            saveOption.setTitle("UPDATE");
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_save:
                if (medicineNameInput.getText().toString().isEmpty()) {
                    medicineNameInputLayout.setError("Must give medicine name");
                    medicineNameInputLayout.requestFocus();
                    break;
                } else {
                    medicineNameInputLayout.setError(null);
                    medicine.setMedicineDoses(selectedMedication);
                    medicine.setName(medicineNameInput.getText().toString());
                    if (!getIntent().hasExtra(Constants.UPDATE_MEDICINE_INFO)) {
                        medicine.setActivityState(true);
                    }
                    medicine.setMedicineDosesType(medicineTimeInput.getText().toString());
                }
                Log.d(TAG, "onOptionsItemSelected: " + medicine.toString());

                if (getIntent().hasExtra(Constants.UPDATE_MEDICINE_INFO)) {
                    AppExecutors
                            .getInstance()
                            .diskIO()
                            .execute(() -> {
                                MedicineDatabase
                                        .getInstance(MedicineAddingActivity.this)
                                        .medicineDao()
                                        .updateMedicine(medicine);
                            });
                } else {
                    AppExecutors
                            .getInstance()
                            .diskIO()
                            .execute(() -> {
                                MedicineDatabase
                                        .getInstance(MedicineAddingActivity.this)
                                        .medicineDao()
                                        .insertMedicine(medicine);
                            });
                }

                startActivity(new Intent(MedicineAddingActivity.this, MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeClickListener(int position, String selected) {
        Log.d(TAG, "onTimeClickListener: " + selected);

        if (position == 4 && selected.equals("......")) {
            onTimeListItemSelectionEvents.onItemSelectedListener(timeTable.get(1));
        } else if (position == 9 && selected.equals("......")) {
            onTimeListItemSelectionEvents.onItemSelectedListener(timeTable.get(2));
        } else if (!selected.equals("Frequency")) {
            if (!selected.equals("Intervals")) {
                medicineTimeInput.setText(selected);
                dialogFragment.dismiss();

                selectedMedication = retrieveTimeDoseTableData(selected);
                adapter.updateTimeDoseList(selectedMedication);
            }
        }
    }

    @Override
    public void onTimeDoseClickListener(int position, MedicineDose medicineDose) {
        Log.d(TAG, "onTimeDoseClickListener: " + medicineDose.toString());

        this.position = position;
        this.medicineDose = medicineDose;
        timeDialogFragment = new TimeDosePickerDialogFragment(this, medicineDose);
        timeDialogFragment.show(getSupportFragmentManager(), "Picker");

        timeDialogFragment.setOnTimeDosePickerDialogActionButtonEvents(MedicineAddingActivity.this);
    }

    private ArrayList<MedicineDose> retrieveTimeDoseTableData(@NotNull String selected) {
        ArrayList<MedicineDose> data = new ArrayList<>();
        try (InputStream is = getAssets().open("Time Dose Table.json")) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject object = new JSONObject(json);
            JSONArray array;

            if (selected.matches("(.*)day(.*)")) {
                array = object.getJSONArray("frequency");
            } else {
                array = object.getJSONArray("intervals");
            }

            for (int j = 0; j < array.length(); j++) {
                if (array.getJSONObject(j).has(selected)) {
                    for (int i = 0; i < array.getJSONObject(j).getJSONArray(selected).length(); i++) {
                        data.add(new MedicineDose(array.getJSONObject(j).getJSONArray(selected).getJSONObject(i).getString("time"),
                                array.getJSONObject(j).getJSONArray(selected).getJSONObject(i).getDouble("dose")));
                    }
                }
            }
            Log.d(TAG, "retrieveTimeDoseTableData: " + data.toString());
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void setOnTimeListItemSelectionEvents(OnTimeListItemSelectionEvents onTimeListItemSelectionEvents) {
        this.onTimeListItemSelectionEvents = onTimeListItemSelectionEvents;
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        medicineDose.setTime(new StringBuilder()
                .append(typeCastedHour(hourOfDay))
                .append(":")
                .append(precedingZero(minute))
                .append(" ")
                .append(getConvention(hourOfDay))
                .toString());
    }

    @NotNull
    private String precedingZero(int minute) {
        if (minute > 10) {
            return Integer.toString(minute);
        } else {
            return "0" + minute;
        }
    }

    @NotNull
    @Contract(pure = true)
    private String getConvention(int hourOfDay) {
        if (hourOfDay >= 12) {
            return " PM";
        } else if (hourOfDay == 0) {
            return " AM";
        } else {
            return " AM";
        }
    }

    @NotNull
    private String typeCastedHour(int hourOfDay) {
        if (hourOfDay > 12) {
            return Integer.toString(hourOfDay - 12);
        } else if (hourOfDay == 0) {
            return Integer.toString(12);
        } else {
            return Integer.toString(hourOfDay);
        }
    }

    @Override
    public void onTimeDosePickerCancel() {
        timeDialogFragment.dismiss();
    }

    @Override
    public void onTimeDosePickerSet(String dose) {
        medicineDose.setDose(dose);
        selectedMedication.set(position, medicineDose);
        adapter.notifyDataSetChanged();
        timeDialogFragment.dismiss();
    }


    @Override
    public void onDaysPickerCancel(String invokedClassName) {
        if (invokedClassName.equals(DaysDurationFragment.class.getSimpleName())) {
            daysDurationFragment.dismiss();
        } else if (invokedClassName.equals(DaysIntervalFragment.class.getSimpleName())) {
            daysIntervalFragment.dismiss();
        }
    }

    @Override
    public void onDaysPickerSet(String days, String invokedClassName) {
        Log.d(TAG, "onDaysPickerSet: " + medicine.isContinuous());
        if (invokedClassName.equals(DaysDurationFragment.class.getSimpleName())) {
            if (medicine.isNumberOfDays()) {
                medicine.setDaysCount(days);
            }

            medicineDaysDuration.setText(days);

            daysDurationFragment.dismiss();
        } else if (invokedClassName.equals(DaysIntervalFragment.class.getSimpleName())) {
            Log.d(TAG, "onDaysPickerSet: "+invokedClassName+"   "+days);
            medicineDaysInterval.setText(days);

            medicine.setDaysIntervalCount(days);

            daysIntervalFragment.dismiss();
        }
    }
}
