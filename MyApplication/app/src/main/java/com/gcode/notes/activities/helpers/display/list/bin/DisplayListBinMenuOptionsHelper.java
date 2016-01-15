package com.gcode.notes.activities.helpers.display.list.bin;

import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.display.list.bin.DisplayListBinActivity;
import com.gcode.notes.ui.ActionExecutor;

public class DisplayListBinMenuOptionsHelper {
    public static boolean optionItemSelected(DisplayListBinActivity displayListBinActivity, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_forever:
                ActionExecutor.deleteNoteFromDisplayBin(displayListBinActivity, displayListBinActivity.mListData);
                return true;
            case R.id.action_restore_deleted:
                ActionExecutor.restoreDeletedNote(displayListBinActivity, displayListBinActivity.mListData);
                return true;
        }
        return false;
    }
}
