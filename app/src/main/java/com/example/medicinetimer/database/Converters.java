package com.example.medicinetimer.database;

import androidx.room.TypeConverter;

import com.example.medicinetimer.container.MedicineDose;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class Converters {
    @TypeConverter
    public static HashMap<String, Boolean> fromStringToHashMap(String json) {
        Type mapType = new TypeToken<HashMap<String, Boolean>>() {
        }.getType();
        return new Gson().fromJson(json, mapType);
    }

    @TypeConverter
    public static String fromHashMapToString(HashMap<String, Boolean> map) {
        return new Gson().toJson(map);
    }

    @TypeConverter
    public static ArrayList<MedicineDose> fromStringToArrayList(String json) {
        Type listType = new TypeToken<ArrayList<MedicineDose>>() {
        }.getType();
        return new Gson().fromJson(json, listType);
    }

    @TypeConverter
    public static String fromArrayListToString(ArrayList<MedicineDose> list) {
        return new Gson().toJson(list);
    }
}
