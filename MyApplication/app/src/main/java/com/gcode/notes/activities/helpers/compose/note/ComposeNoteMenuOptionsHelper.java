package com.gcode.notes.activities.helpers.compose.note;

import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.ui.ActionExecutor;

public class ComposeNoteMenuOptionsHelper {
    public static boolean optionsItemSelected(ComposeNoteActivity composeNoteActivity, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                new ComposeNoteSaveHelper(composeNoteActivity).saveNote();
                return true;
            case R.id.action_add_image:
                ActionExecutor.addPhotoToNote(composeNoteActivity);
                return true;
        }
        return false;
    }
}
