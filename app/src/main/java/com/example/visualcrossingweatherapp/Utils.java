package com.example.visualcrossingweatherapp;

import android.util.Log;

import java.lang.reflect.Field;

public class Utils {
    private static final String TAG = "Utils";

    public static int getId(String resourceName) {
        Class<?> c =  R.drawable.class;
        int iconId = 0;
        try {
            Field idField = c.getDeclaredField(resourceName);
            iconId = idField.getInt(idField);
        } catch (Exception e) {
            Log.e(TAG, "getId: ", e);
        }
        if (iconId == 0) {
            iconId = R.mipmap.ic_launcher;
        }
        return iconId;
    }
}
