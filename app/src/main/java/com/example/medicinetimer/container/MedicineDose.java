package com.example.medicinetimer.container;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MedicineDose implements Parcelable {
    private String time;
    private String dose;

    public MedicineDose() {
    }

    public MedicineDose(String time, String dose) {
        this.time = time;
        this.dose = dose;
    }

    public MedicineDose(String time, Double dose) {
        this.time = time;
        this.dose = Double.toString(dose);
    }

    protected MedicineDose(Parcel in) {
        time = in.readString();
        dose = in.readString();
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDose() {
        return "(Take : "+dose+")";
    }

    public Double getDoseCount() {
        return Double.parseDouble(dose);
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    @NonNull
    @Override
    public String toString() {
        return "MedicineDose{" +
                "time='" + time + '\'' +
                ", dose='" + dose + '\'' +
                '}';
    }

    public static final Creator<MedicineDose> CREATOR = new Creator<MedicineDose>() {
        @Override
        public MedicineDose createFromParcel(Parcel in) {
            return new MedicineDose(in);
        }

        @Override
        public MedicineDose[] newArray(int size) {
            return new MedicineDose[size];
        }
    };

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(time);
        dest.writeString(dose);
    }
}
