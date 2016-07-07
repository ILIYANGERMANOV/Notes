package com.gcode.notes.extras;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class MyLogger {
    public static final String DEBUG_TAG = "ddq";

    private static final String COLON_DIVIDER = ": ";
    private static final String COMMA = ", ";

    public static void log(String info) {
        Log.d(DEBUG_TAG, info);
    }

    public static void log(String key, String info) {
        Log.d(DEBUG_TAG, key + COLON_DIVIDER + info);
    }

    public static void log(String key, int info) {
        Log.d(DEBUG_TAG, key + COLON_DIVIDER + Integer.toString(info));
    }

    public static void log(String key, int... values) {
        String valuesString = "";
        boolean firstValuePassed = false;
        for (int value : values) {
            if (firstValuePassed) {
                valuesString += COMMA;
            }
            valuesString += Integer.toString(value);
            firstValuePassed = true;
        }
        Log.d(DEBUG_TAG, key + COLON_DIVIDER + valuesString);
    }

    public static void log(String key, boolean info) {
        Log.d(DEBUG_TAG, key + COLON_DIVIDER + Boolean.toString(info));
    }

    public static void log(String key, float info) {
        Log.d(DEBUG_TAG, key + COLON_DIVIDER + Float.toString(info));
    }

    public static void log(String key, long info) {
        Log.d(DEBUG_TAG, key + COLON_DIVIDER + Long.toString(info));
    }

    public static void log(String key, double info) {
        Log.d(DEBUG_TAG, key + COLON_DIVIDER + Double.toString(info));
    }

    public static void log(String key, CharSequence charSequence) {
        Log.d(DEBUG_TAG, key + COLON_DIVIDER + charSequence.toString());
    }

    public static void toast(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}
