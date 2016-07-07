package com.gcode.notes.ui.callbacks.display;

import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.activities.display.DisplayBaseActivity;
import com.gcode.notes.activities.display.list.editable.DisplayListNormalActivity;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.async.encryption.EncryptNoteTask;
import com.gcode.notes.tasks.async.encryption.callbacks.EncryptTaskCallbacks;

public class LockNoteCallback implements MaterialDialog.SingleButtonCallback, EncryptTaskCallbacks {
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
        mDisplayBaseActivity.mNoteModeChanged = true; //main activity onItemModeChanged() should be called
        mContentBase.setMode(Constants.MODE_PRIVATE); //changes note mode from normal to private
        //!NOTE: Must be used with update creation date flag, so will work
        if (!MyApplication.getWritableDatabase().updateNote(mContentBase, true)) {
            //failed to save to db decrypted note, log it and prevent further execution
            MyLogger.log("LockNoteCallback failed to update note.");
            return;
        }
        if (mDisplayBaseActivity instanceof DisplayListNormalActivity) {
            //its normal list activity, prevent on stop save,
            //so encrypted items will be saved in db
            ((DisplayListNormalActivity) mDisplayBaseActivity).mPreventOnStopSave = true;
        }
        mDisplayBaseActivity.finish();
    }
}