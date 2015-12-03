package com.gcode.notes.activities.helpers.display.note.base;

import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.display.note.DisplayNoteBaseActivity;

public class DisplayNoteBaseMenuOptionsHelper {
    public static boolean optionItemSelected(DisplayNoteBaseActivity displayNoteBaseActivity, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                DisplayNoteBaseResultHandler.setResult(displayNoteBaseActivity);
                displayNoteBaseActivity.finish();
                return true;
        }
        return false;
    }
}
