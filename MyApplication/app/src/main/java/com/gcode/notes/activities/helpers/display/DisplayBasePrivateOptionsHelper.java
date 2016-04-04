package com.gcode.notes.activities.helpers.display;

import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.display.DisplayBaseActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.ui.ActionExecutor;

public class DisplayBasePrivateOptionsHelper {
    public static boolean optionItemSelected(DisplayBaseActivity displayBaseActivity, MenuItem item,
                                             ContentBase contentBase) {
        switch (item.getItemId()) {
            case R.id.action_unlock_note:
                ActionExecutor.unlockNote(displayBaseActivity, contentBase);
                return true;
            case R.id.action_delete_private_note:
                ActionExecutor.deletePrivateNoteFromDisplay(displayBaseActivity, contentBase);
                return true;
        }
        return false;
    }
}
