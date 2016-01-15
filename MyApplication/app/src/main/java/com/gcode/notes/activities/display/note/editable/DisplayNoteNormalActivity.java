package com.gcode.notes.activities.display.note.editable;

import android.view.Menu;

import com.gcode.notes.R;
import com.gcode.notes.notes.MyApplication;

import butterknife.OnClick;

public class DisplayNoteNormalActivity extends DisplayNoteEditableActivity {

    @Override
    public void displayNoteData() {
        super.displayNoteData();
        if (mNoteData.isImportant()) {
            setStarredState();
        } else {
            setNotStarredState();
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.display_action_image_button)
    public void starImageButtonClicked() {
        if (mIsStarred) {
            setNotStarredState();
        } else {
            setStarredState();
        }
        mNoteData.setModeImportant(mIsStarred);
        mNoteModeChanged = !mNoteModeChanged;
        MyApplication.getWritableDatabase().updateNoteMode(mNoteData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_note_normal, menu);
        return true;
    }
}
