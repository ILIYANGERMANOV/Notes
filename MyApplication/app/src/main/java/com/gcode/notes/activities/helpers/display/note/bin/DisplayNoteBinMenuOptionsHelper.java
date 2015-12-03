package com.gcode.notes.activities.helpers.display.note.bin;


import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.display.note.DisplayNoteBinActivity;
import com.gcode.notes.ui.ActionExecutor;

public class DisplayNoteBinMenuOptionsHelper {
    public static boolean optionItemSelected(DisplayNoteBinActivity displayNoteBinActivity, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_forever:
                ActionExecutor.deleteNoteFromDisplayBin(displayNoteBinActivity, displayNoteBinActivity.mNoteData);
                return true;
            case R.id.action_restore_deleted:
                ActionExecutor.restoreDeletedNote(displayNoteBinActivity, displayNoteBinActivity.mNoteData);
                return true;
        }
        return false;
    }
}
