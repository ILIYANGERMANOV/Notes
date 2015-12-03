package com.gcode.notes.activities.helpers.display.note.normal;

import android.content.Intent;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.activities.display.note.DisplayNoteNormalActivity;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class DisplayNoteNormalMenuOptionsHelper {
    public static boolean optionItemSelected(DisplayNoteNormalActivity displayNoteNormalActivity, MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(displayNoteNormalActivity, ComposeNoteActivity.class);
            intent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(displayNoteNormalActivity.mNoteData));
            intent.putExtra(Constants.EXTRA_SETUP_FROM, Constants.SETUP_FROM_EDIT_MODE);
            displayNoteNormalActivity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
            return true;
        }
        return false;
    }
}
