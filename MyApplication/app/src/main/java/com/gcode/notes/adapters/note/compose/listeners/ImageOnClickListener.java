package com.gcode.notes.adapters.note.compose.listeners;


import android.view.View;

import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.extras.utils.PhotoUtils;
import com.gcode.notes.ui.helpers.DialogBuilder;

public class ImageOnClickListener implements View.OnClickListener {
    ComposeNoteActivity mComposeNoteActivity;
    String mPhotoPath;

    public ImageOnClickListener(ComposeNoteActivity composeNoteActivity, String photoPath) {
        mComposeNoteActivity = composeNoteActivity;
        mPhotoPath = photoPath;
    }

    @Override
    public void onClick(View v) {
        //show opening image in gallery progress dialog
        mComposeNoteActivity.mOpenImageInGalleryProgressDialog =
                DialogBuilder.buildOpenImageProgressDialog(mComposeNoteActivity);

        PhotoUtils.openPhotoInGallery(mComposeNoteActivity, mPhotoPath); //launch open photo in gallery intent
    }
}
