package com.gcode.notes.activities.helpers.display.note.editable.mprivate;

import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.display.note.editable.DisplayNoteEditableActivity;
import com.gcode.notes.ui.ActionExecutor;

public class DisplayNotePrivateMenuOptionsHelper {
    public static boolean optionItemSelected(DisplayNoteEditableActivity displayNoteEditableActivity, MenuItem item) {
        if (item.getItemId() == R.id.action_unlock_note) {
            ActionExecutor.unlockNote(displayNoteEditableActivity, displayNoteEditableActivity.mNoteData);
            return true;
        }
        return false;
    }
}
