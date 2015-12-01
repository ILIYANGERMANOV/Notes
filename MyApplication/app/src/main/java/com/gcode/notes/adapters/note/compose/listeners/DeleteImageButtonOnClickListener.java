package com.gcode.notes.adapters.note.compose.listeners;


import android.view.View;

import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.adapters.note.compose.ComposeNoteImagesAdapter;
import com.gcode.notes.ui.ActionExecutor;

public class DeleteImageButtonOnClickListener implements View.OnClickListener {
    ComposeNoteActivity mComposeNoteActivity;
    ComposeNoteImagesAdapter mComposeNoteImagesAdapter;
    String mPhotoPath;

    public DeleteImageButtonOnClickListener(ComposeNoteActivity composeNoteActivity,
                                            ComposeNoteImagesAdapter composeNoteImagesAdapter, String photoPath) {
        mComposeNoteActivity = composeNoteActivity;
        mComposeNoteImagesAdapter = composeNoteImagesAdapter;
        mPhotoPath = photoPath;
    }

    @Override
    public void onClick(View v) {
        ActionExecutor.removePhotoFromNote(mComposeNoteActivity, mComposeNoteImagesAdapter, mPhotoPath);
    }
}
