package com.gcode.notes.activities.helpers.display;


import android.app.Activity;
import android.view.MenuItem;

import com.gcode.notes.R;

public class DisplayBaseMenuOptionsHelper {
    public static boolean optionItemSelected(Activity activity, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                activity.onBackPressed();
                return true;
        }
        return false;
    }
}
