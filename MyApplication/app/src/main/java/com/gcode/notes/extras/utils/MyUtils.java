package com.gcode.notes.extras.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.inputmethod.InputMethodManager;

import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;

import java.util.Locale;

public class MyUtils {
    public static boolean isPreApi19() {
        return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT;
    }

    public static void saveToPreferences(String preferenceName, String value) {
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.putString(preferenceName, value);
        editor.apply();
    }

    public static String readFromPreferences(String preferenceName, String defaultValue) {
        return getSharedPref().getString(preferenceName, defaultValue);
    }

    public static void saveToPreferences(String preferenceName, Integer value) {
        SharedPreferences.Editor editor = getSharedPref().edit();
        editor.putInt(preferenceName, value);
        editor.apply();
    }

    public static Integer readFromPreferences(String preferenceName, Integer defaultValue) {
        return getSharedPref().getInt(preferenceName, defaultValue);
    }

    private static SharedPreferences getSharedPref() {
        return MyApplication.getAppContext().
                getSharedPreferences(Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
    }

    public static boolean isLollipop() {
        return Build.VERSION.SDK_INT >= 21;
    }

    public static boolean isRTL() {
        final int directionality = Character.getDirectionality(Locale.getDefault().getDisplayName().charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT ||
                directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    /**
     * Hides keyboard if shown.
     *
     * @param activity Instance of Activity
     */
    public static void hideSoftInput(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}
