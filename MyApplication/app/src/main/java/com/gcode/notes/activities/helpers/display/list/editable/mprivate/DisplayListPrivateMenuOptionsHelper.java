package com.gcode.notes.activities.helpers.display.list.editable.mprivate;

import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.display.list.editable.DisplayListEditableActivity;
import com.gcode.notes.ui.ActionExecutor;

public class DisplayListPrivateMenuOptionsHelper {
    public static boolean optionItemSelected(DisplayListEditableActivity displayListEditableActivity, MenuItem item) {
        if (item.getItemId() == R.id.action_unlock_note) {
            ActionExecutor.unlockNote(displayListEditableActivity, displayListEditableActivity.mListData);
            return true;
        }
        return false;
    }
}
