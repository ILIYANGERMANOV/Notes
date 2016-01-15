package com.gcode.notes.activities.helpers.display.note.editable;

import android.content.Intent;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.activities.display.note.editable.DisplayNoteEditableActivity;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class DisplayNoteEditableMenuOptionsHelper {
    public static boolean optionItemSelected(DisplayNoteEditableActivity displayNoteEditableActivity, MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(displayNoteEditableActivity, ComposeNoteActivity.class);
            intent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(displayNoteEditableActivity.mNoteData));
            intent.putExtra(Constants.EXTRA_SETUP_FROM, Constants.SETUP_FROM_EDIT_MODE);
            displayNoteEditableActivity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
            return true;
        }
        return false;
    }
}
