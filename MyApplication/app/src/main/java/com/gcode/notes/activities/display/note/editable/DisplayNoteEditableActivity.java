package com.gcode.notes.activities.display.note.editable;

import android.content.Intent;
import android.view.MenuItem;

import com.gcode.notes.activities.display.note.DisplayNoteBaseActivity;
import com.gcode.notes.activities.helpers.display.note.editable.DisplayNoteEditableMenuOptionsHelper;
import com.gcode.notes.activities.helpers.display.note.editable.DisplayNoteEditableResultHandler;

public class DisplayNoteEditableActivity extends DisplayNoteBaseActivity {

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DisplayNoteEditableResultHandler.handleResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) || DisplayNoteEditableMenuOptionsHelper.optionItemSelected(this, item);
    }
}
