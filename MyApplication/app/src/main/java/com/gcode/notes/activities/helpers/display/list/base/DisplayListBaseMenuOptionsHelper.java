package com.gcode.notes.activities.helpers.display.list.base;

import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.display.list.DisplayListBaseActivity;

public class DisplayListBaseMenuOptionsHelper {
    public static boolean optionItemSelected(DisplayListBaseActivity displayListBaseActivity,MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                displayListBaseActivity.finish();
                return true;
        }
        return false;
    }
}
