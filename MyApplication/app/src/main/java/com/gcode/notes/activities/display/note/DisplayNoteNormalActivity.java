package com.gcode.notes.activities.display.note;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.data.main.NoteData;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;

import butterknife.OnClick;

public class DisplayNoteNormalActivity extends DisplayNoteBaseActivity {
    boolean mIsStarred;

    @Override
    protected void displayNoteData() {
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
        mNoteData.setImportant(mIsStarred);
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
        //TODO: REFACTOR
        switch (requestCode) {
            case Constants.COMPOSE_NOTE_REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    if (data.getBooleanExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, false)) {
                        String serializedNoteData = data.getStringExtra(Constants.EXTRA_NOTE_DATA);
                        if (serializedNoteData != null) {
                            NoteData noteData = Serializer.parseNoteData(serializedNoteData);
                            if (noteData != null) {
                                mNoteModeChanged = data.getBooleanExtra(Constants.EXTRA_NOTE_MODE_CHANGED, false);
                                mNoteData = noteData;
                                displayNoteData();
                            }
                        }
                    }
                    if (data.getBooleanExtra(Constants.EXTRA_DELETED_AUDIO, false)) {
                        mAudioLayout.setVisibility(View.GONE);
                        mNoteData.setAttachedAudioPath(Constants.NO_AUDIO);
                    }
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_note_normal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, ComposeNoteActivity.class);
            intent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(mNoteData));
            startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
