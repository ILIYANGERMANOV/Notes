package com.gcode.notes.extras;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class MyDebugger {
    public static final String DEBUG_TAG = "ddq";

    public static void log(String info) {
        Log.d(DEBUG_TAG, info);
    }

    public static void log(String key, String info) {
        Log.d(DEBUG_TAG, key + ": " + info);
    }

    public static void log(String key, int info) {
        Log.d(DEBUG_TAG, key + ": " + Integer.toString(info));
    }

    public static void log(String key, boolean info) {
        Log.d(DEBUG_TAG, key + ": " + Boolean.toString(info));
    }

    public static void log(String key, float info) {
        Log.d(DEBUG_TAG, key + ": " + Float.toString(info));
    }

    public static void log(String key, long info) {
        Log.d(DEBUG_TAG, key + ": " + Long.toString(info));
    }

    public static void log(String key, double info) {
        Log.d(DEBUG_TAG, key + ": " + Double.toString(info));
    }

    public static void toast(Context context, String info) {
        Toast.makeText(context, info, Toast.LENGTH_SHORT).show();
    }
}
