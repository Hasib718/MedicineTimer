package com.example.medicinetimer.container;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.HashMap;

@Entity(tableName = "medicine")
public class Medicine implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "activeState")
    private boolean activityState;

    @ColumnInfo(name = "times")
    private String times;

    @ColumnInfo(name = "continuous")
    private boolean continuous;

    @ColumnInfo(name = "numberOfDays")
    private boolean numberOfDays;

    @ColumnInfo(name = "daysCount")
    private String daysCount;

    @ColumnInfo(name = "startingDay")
    private String startingDay;

    @ColumnInfo(name = "everyday")
    private boolean everyday;

    @ColumnInfo(name = "specificDay")
    private boolean specificDay;

    @ColumnInfo(name = "daysInterval")
    private boolean daysInterval;

    @ColumnInfo(name = "specifiedDays")
    private HashMap<String, Boolean> specifiedDays = new HashMap<>();

    @ColumnInfo(name = "daysIntervalCount")
    private String daysIntervalCount;

    @ColumnInfo(name = "medicineDosesType")
    private String medicineDosesType;

    @ColumnInfo(name = "medicineDoses")
    private ArrayList<MedicineDose> medicineDoses;

    @Ignore
    public Medicine() {
        this.specifiedDays.put("Sunday", false);
        this.specifiedDays.put("Monday", false);
        this.specifiedDays.put("Tuesday", false);
        this.specifiedDays.put("Wednesday", false);
        this.specifiedDays.put("Thursday", false);
        this.specifiedDays.put("Friday", false);
        this.specifiedDays.put("Saturday", false);
    }

    public Medicine(int id, String name, boolean activityState, String times, boolean continuous, boolean numberOfDays, String daysCount,
                    String startingDay, boolean everyday, boolean specificDay, boolean daysInterval, HashMap<String, Boolean> specifiedDays,
                    String daysIntervalCount, String medicineDosesType, ArrayList<MedicineDose> medicineDoses) {
        this.id = id;
        this.name = name;
        this.activityState = activityState;
        this.times = times;
        this.continuous = continuous;
        this.numberOfDays = numberOfDays;
        this.daysCount = daysCount;
        this.startingDay = startingDay;
        this.everyday = everyday;
        this.specificDay = specificDay;
        this.daysInterval = daysInterval;
        this.specifiedDays = specifiedDays;
        this.daysIntervalCount = daysIntervalCount;
        this.medicineDosesType = medicineDosesType;
        this.medicineDoses = medicineDoses;
    }

    @Ignore
    public Medicine(String name, boolean activityState, String times, String daysCount, String startingDay) {
        this.name = name;
        this.activityState = activityState;
        this.times = times;
        this.daysCount = daysCount;
        this.startingDay = startingDay;

        this.specifiedDays.put("Sunday", false);
        this.specifiedDays.put("Monday", false);
        this.specifiedDays.put("Tuesday", false);
        this.specifiedDays.put("Wednesday", false);
        this.specifiedDays.put("Thursday", false);
        this.specifiedDays.put("Friday", false);
        this.specifiedDays.put("Saturday", false);
    }

    @Ignore
    protected Medicine(Parcel in) {
        id = in.readInt();
        name = in.readString();
        activityState = in.readBoolean();
        times = in.readString();
        continuous = in.readBoolean();
        numberOfDays = in.readBoolean();
        daysCount = in.readString();
        startingDay = in.readString();
        everyday = in.readBoolean();
        specificDay = in.readBoolean();
        daysInterval = in.readBoolean();
        specifiedDays = in.readHashMap(HashMap.class.getClassLoader());
        daysIntervalCount = in.readString();
        medicineDosesType = in.readString();
        medicineDoses = in.readArrayList(MedicineDose.class.getClassLoader());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActivityState() {
        return activityState;
    }

    public void setActivityState(boolean activityState) {
        this.activityState = activityState;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public boolean isContinuous() {
        return continuous;
    }

    public void setContinuous(boolean continuous) {
        this.continuous = continuous;
    }

    public boolean isNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(boolean numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public boolean isEveryday() {
        return everyday;
    }

    public void setEveryday(boolean everyday) {
        this.everyday = everyday;
    }

    public boolean isSpecificDay() {
        return specificDay;
    }

    public void setSpecificDay(boolean specificDay) {
        this.specificDay = specificDay;
    }

    public boolean isDaysInterval() {
        return daysInterval;
    }

    public void setDaysInterval(boolean daysInterval) {
        this.daysInterval = daysInterval;
    }

    public HashMap<String, Boolean> getSpecifiedDays() {
        return specifiedDays;
    }

    public void setSpecifiedDays(HashMap<String, Boolean> specifiedDays) {
        this.specifiedDays = specifiedDays;
    }

    public String getDaysIntervalCount() {
        return daysIntervalCount;
    }

    public void setDaysIntervalCount(String daysIntervalCount) {
        this.daysIntervalCount = daysIntervalCount;
    }

    public String getDaysCount() {
        return daysCount;
    }

    public void setDaysCount(String daysCount) {
        this.daysCount = daysCount;
    }

    public String getStartingDay() {
        return startingDay;
    }

    public void setStartingDay(String startingDay) {
        this.startingDay = startingDay;
    }

    public String getMedicineDosesType() {
        return medicineDosesType;
    }

    public void setMedicineDosesType(String medicineDosesType) {
        this.medicineDosesType = medicineDosesType;
    }

    public ArrayList<MedicineDose> getMedicineDoses() {
        return medicineDoses;
    }

    public void setMedicineDoses(ArrayList<MedicineDose> medicineDoses) {
        this.medicineDoses = medicineDoses;

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < this.medicineDoses.size(); i++) {
            stringBuilder.append(this.medicineDoses.get(i).getTime())
                    .append(", ");
        }
        stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length() - 1);
        times = stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", activityState=" + activityState +
                ", times='" + times + '\'' +
                ", continuous=" + continuous +
                ", numberOfDays=" + numberOfDays +
                ", daysCount='" + daysCount + '\'' +
                ", startingDay='" + startingDay + '\'' +
                ", everyday=" + everyday +
                ", specificDay=" + specificDay +
                ", daysInterval=" + daysInterval +
                ", specifiedDays=" + specifiedDays +
                ", daysIntervalCount='" + daysIntervalCount + '\'' +
                ", medicineDosesType='" + medicineDosesType + '\'' +
                ", medicineDoses=" + medicineDoses +
                '}';
    }

    public static final Creator<Medicine> CREATOR = new Creator<Medicine>() {
        @Override
        public Medicine createFromParcel(Parcel in) {
            return new Medicine(in);
        }

        @Override
        public Medicine[] newArray(int size) {
            return new Medicine[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeBoolean(activityState);
        dest.writeString(times);
        dest.writeBoolean(continuous);
        dest.writeBoolean(numberOfDays);
        dest.writeString(daysCount);
        dest.writeString(startingDay);
        dest.writeBoolean(everyday);
        dest.writeBoolean(specificDay);
        dest.writeBoolean(daysInterval);
        dest.writeMap(specifiedDays);
        dest.writeString(daysIntervalCount);
        dest.writeString(medicineDosesType);
        dest.writeList(medicineDoses);
    }
}
