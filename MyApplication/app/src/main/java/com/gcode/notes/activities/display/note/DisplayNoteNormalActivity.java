package com.gcode.notes.activities.display.note;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.display.note.normal.DisplayNoteNormalMenuOptionsHelper;
import com.gcode.notes.activities.helpers.display.note.normal.DisplayNoteNormalResultHandler;
import com.gcode.notes.notes.MyApplication;

import butterknife.OnClick;

public class DisplayNoteNormalActivity extends DisplayNoteBaseActivity {
    boolean mIsStarred;

    @Override
    public void displayNoteData() {
        super.displayNoteData();
        if (mNoteData.isImportant()) {
            setStarredState();
        } else {
            setNotStarredState();
        }
    }

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

    private void setStarredState() {
        mIsStarred = true;
        mActionImageButton.setImageResource(R.drawable.ic_star_orange_36dp);
    }

    private void setNotStarredState() {
        mIsStarred = false;
        mActionImageButton.setImageResource(R.drawable.ic_star_border_black_36dp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DisplayNoteNormalResultHandler.handleResult(this, requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_note_normal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) || DisplayNoteNormalMenuOptionsHelper.optionItemSelected(this, item);
    }
}
