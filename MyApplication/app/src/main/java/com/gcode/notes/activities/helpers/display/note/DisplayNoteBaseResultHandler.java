package com.gcode.notes.activities.helpers.display.note;

import android.app.Activity;
import android.content.Intent;

import com.gcode.notes.activities.display.note.DisplayNoteBaseActivity;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class DisplayNoteBaseResultHandler {
    public static void handleResult(DisplayNoteBaseActivity displayNoteBaseActivity, int requestCode) {
        if (requestCode == Constants.OPEN_PHOTO_IN_GALLERY_REQ_CODE) {
            //result from gallery app (image has been opened and now is closed), dismiss open progress dialog
            if (displayNoteBaseActivity.mOpenInGalleryProgressDialog != null) {
                //dialog isn't null, dismiss it
                displayNoteBaseActivity.mOpenInGalleryProgressDialog.dismiss();
            }
        }
    }

    /**
     * Sets EXTRA_NOTE_DATA and EXTRA_NOTE_MODE_CHANGED.
     *
     * @param displayNoteBaseActivity - activity which result's will be set
     */
    public static void setResult(DisplayNoteBaseActivity displayNoteBaseActivity) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(Constants.EXTRA_NOTE_DATA, Serializer.serializeNoteData(displayNoteBaseActivity.mNoteData));
        resultIntent.putExtra(Constants.EXTRA_NOTE_MODE_CHANGED, displayNoteBaseActivity.mNoteModeChanged);
        displayNoteBaseActivity.setResult(Activity.RESULT_OK, resultIntent);
    }
}
