package com.gcode.notes.activities.helpers.compose.note;

import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.ui.ActionExecutor;

import pl.tajchert.nammu.PermissionCallback;

public class ComposeNoteMenuOptionsHelper {
    public static boolean optionsItemSelected(final ComposeNoteActivity composeNoteActivity, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                new ComposeNoteSaveHelper(composeNoteActivity).saveNote();
                return true;
            case R.id.action_add_image:
                if (composeNoteActivity.mPermissionsUtils.hasStoragePermission()) {
                    //user has granted storage permission, add photo to note
                    ActionExecutor.addPhotoToNote(composeNoteActivity);
                } else {
                    //user has not granted storage permission, ask for it
                    composeNoteActivity.mPermissionsUtils.askForStoragePermission(new PermissionCallback() {
                        @Override
                        public void permissionGranted() {
                            //storage permission granted, add photo to note
                            ActionExecutor.addPhotoToNote(composeNoteActivity);
                        }

                        @Override
                        public void permissionRefused() {
                            //storage permission refused, do nothing
                            MyLogger.log("Compose note activity: storage permission refused.");
                        }
                    });
                }
                return true;
        }
        return false;
    }
}
