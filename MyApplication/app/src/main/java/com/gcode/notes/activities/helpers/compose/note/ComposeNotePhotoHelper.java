package com.gcode.notes.activities.helpers.compose.note;

import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.adapters.note.compose.ComposeNoteImagesAdapter;
import com.gcode.notes.data.main.NoteData;

public class ComposeNotePhotoHelper {
    public static void setupFromPhoto(ComposeNoteActivity composeNoteActivity, String photoPath) {
        composeNoteActivity.mNoteData = new NoteData();
        setupImagesAdapter(composeNoteActivity);
        composeNoteActivity.mImagesAdapter.add(photoPath);
    }

    public static void addPhoto(ComposeNoteActivity composeNoteActivity, String photoPath) {
        ComposeNotePhotoHelper.setupImagesAdapter(composeNoteActivity); //creates ComposeNoteImagesAdapter if not created
        composeNoteActivity.mImagesAdapter.add(photoPath); //adding item to adapter will add it to mNoteData, too
    }

    /**
     * Creates ComposeNoteImagesAdapter if not created, using the same list as mNoteData
     *
     * @param composeNoteActivity - ComposeNoteActivity
     */
    public static void setupImagesAdapter(ComposeNoteActivity composeNoteActivity) {
        if (composeNoteActivity.mImagesAdapter == null) {
            composeNoteActivity.mImagesAdapter = new ComposeNoteImagesAdapter(composeNoteActivity,
                    composeNoteActivity.mNoteData.getAttachedImagesPaths(), composeNoteActivity.getImagesLinearListView());

            composeNoteActivity.getImagesLinearListView().setAdapter(composeNoteActivity.mImagesAdapter);
        }
    }
}
