package com.gcode.notes.activities.display.note.bin;

import android.view.Menu;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.display.note.DisplayNoteBaseActivity;
import com.gcode.notes.activities.helpers.display.note.bin.DisplayNoteBinMenuOptionsHelper;
import com.gcode.notes.ui.ActionExecutor;

import butterknife.OnClick;

public class DisplayNoteBinActivity extends DisplayNoteBaseActivity {

    @Override
    public void displayNoteData() {
        super.displayNoteData();
        getActionImageButton().setImageResource(R.drawable.ic_restore_deleted_24dp);
    }

    @OnClick(R.id.display_action_image_button)
    public void restoreNote() {
        ActionExecutor.restoreDeletedNote(this, mNoteData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_note_bin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) || DisplayNoteBinMenuOptionsHelper.optionItemSelected(this, item);
    }
}
