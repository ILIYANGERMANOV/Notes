package com.gcode.notes.extras;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;

import com.gcode.notes.R;
import com.gcode.notes.notes.MyApplication;

public class Utils {
    public static int getToolbarHeight(Context context) {
        final TypedArray styledAttributes = context.getTheme().obtainStyledAttributes(
                new int[]{R.attr.actionBarSize});
        int toolbarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        return toolbarHeight;
    }

    public static void saveToPreferences(String preferenceName, String value) {
        SharedPreferences sharedPref = MyApplication.getAppContext().getSharedPreferences(Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(preferenceName, value);
        editor.apply();
    }

    public static String readFromPreferences(String preferenceName, String defaultValue) {
        SharedPreferences sharedPref = MyApplication.getAppContext().getSharedPreferences(Constants.PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPref.getString(preferenceName, defaultValue);
    }
}
