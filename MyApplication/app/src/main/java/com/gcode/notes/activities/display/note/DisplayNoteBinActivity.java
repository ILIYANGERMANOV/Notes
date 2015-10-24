package com.gcode.notes.activities.display.note;

import android.view.Menu;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.ui.ActionExecutor;

import butterknife.OnClick;

public class DisplayNoteBinActivity extends DisplayNoteBaseActivity {

    @Override
    protected void displayNoteData() {
        super.displayNoteData();
        mActionImageButton.setImageResource(R.drawable.ic_restore_deleted_24dp);
    }

    @OnClick(R.id.display_action_image_button)
    public void restoreNote() {
        ActionExecutor.restoreDeletedNote(this, mNoteData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_note_bin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_forever:
                ActionExecutor.deleteNoteFromBin(this, mNoteData);
                return true;
            case R.id.action_restore_deleted:
                ActionExecutor.restoreDeletedNote(this, mNoteData);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
