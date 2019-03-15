package com.openclassrooms.realestatemanager.utils;

import android.arch.persistence.room.TypeConverter;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.List;


public class ListTypeConverters {
    @TypeConverter
    public static List<String> stringToJson(String json) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    @TypeConverter
    public static String jsonToString(List<String> list) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return gson.toJson(list, type);
    }
}
