package com.gcode.notes.ui.callbacks.display;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.R;
import com.gcode.notes.activities.display.DisplayBaseActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.async.encryption.EncryptNoteTask;
import com.gcode.notes.tasks.async.encryption.callbacks.EncryptTaskCallbacks;
import com.gcode.notes.tasks.async.main.RemoveItemFromMainTask;

public class LockNoteCallback implements MaterialDialog.SingleButtonCallback, EncryptTaskCallbacks {
    //TODO: fix issues with locking note from reminder notification (happens when you are in private when open it)
    //TODO: the problem is that note is not added and wrong updateItem is called mark
    DisplayBaseActivity mDisplayBaseActivity;
    ContentBase mContentBase;

    public LockNoteCallback(DisplayBaseActivity displayBaseActivity, ContentBase contentBase) {
        mDisplayBaseActivity = displayBaseActivity;
        mContentBase = contentBase;
    }

    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        //note should be moved from normal to private mode
        new EncryptNoteTask(mDisplayBaseActivity, this).execute(mContentBase);
    }

    @Override
    public void onEncryptedSuccessfully(ContentBase contentBase) {
        //note encrypted successfully, ready to proceed
        mDisplayBaseActivity.mNoteModeChanged = false; //main activity onItemModeChanged() shouldn't be called
        mContentBase.setMode(Constants.MODE_PRIVATE); //changes note mode from normal to private
        //!NOTE: Must be used with update creation date flag, so will work
        if (!MyApplication.getWritableDatabase().updateNote(mContentBase, true)) {
            //failed to save to db decrypted note, log it and prevent further execution
            MyDebugger.log("LockNoteCallback failed to update note.");
            return;
        }
        mDisplayBaseActivity.finish();
        new RemoveItemFromMainTask(mDisplayBaseActivity.getString(
                R.string.note_moved_to_private)).execute(mContentBase);
    }
}