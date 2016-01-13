package com.gcode.notes.controllers.other;


import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.tasks.async.encryption.DecryptAllNotesTask;
import com.gcode.notes.tasks.async.encryption.DecryptNoteTask;
import com.gcode.notes.tasks.async.encryption.callbacks.CryptTaskCallbacks;
import com.gcode.notes.tasks.async.encryption.callbacks.DecryptNotesTaskCallbacks;
import com.gcode.notes.tasks.async.main.AddItemFromDbToMainTask;

import java.util.ArrayList;

public class PrivateController extends VisibleController implements DecryptNotesTaskCallbacks, CryptTaskCallbacks {

    public PrivateController(MainActivity mainActivity) {
        super(mainActivity);
    }

    @Override
    public void setContent(boolean scrollToTop) {
        super.setContent(scrollToTop);
        mToolbar.setTitle(mMainActivity.getString(R.string.private_label));
    }

    @Override
    public void setNewContent(ArrayList<ContentBase> newContent, boolean scrollToTop) {
        //it is called by LoadNewContentTask() when it is ready, override to prevent default behaviour
        new DecryptAllNotesTask(mMainActivity, this, newContent, scrollToTop).execute();
    }

    @Override
    public void addItem(ContentBase item) {
        //it is called by AddItemFromDbToMainTask() when it is ready, override to prevent default behaviour
        new DecryptNoteTask(mMainActivity, this).execute(item);
    }

    @Override
    public void onItemModeChanged(ContentBase item) {
        //TODO: implement method
    }

    @Override
    public void onItemChanged(ContentBase item) {
        int mode = item.getMode();
        if (mode == Constants.MODE_PRIVATE) {
            updateItem(item);
        }
    }

    @Override
    public void onItemAdded(int mode) {
        if (mode == Constants.MODE_PRIVATE) {
            new AddItemFromDbToMainTask().execute();
        }
    }

    @Override
    public void onNotesDecryptedSuccessfully(ArrayList<ContentBase> decryptedNotes, boolean scrollToTop) {
        super.setNewContent(decryptedNotes, scrollToTop);
    }

    @Override
    public void onTaskCompletedSuccessfully(ContentBase contentBase) {
        super.addItem(contentBase);
    }
}
