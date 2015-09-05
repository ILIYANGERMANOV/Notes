package com.gcode.notes.extras;

import android.util.Log;

public class MyDebugger {
    public static void log(String info) {
        Log.d(Constants.DEBUG_TAG, info);
    }

    public static void log(String key, int info) {
        Log.d(Constants.DEBUG_TAG, key + ": " + Integer.toString(info));
    }

    public static void log(String key, float info) {
        Log.d(Constants.DEBUG_TAG, key + ": " + Float.toString(info));
    }
}
