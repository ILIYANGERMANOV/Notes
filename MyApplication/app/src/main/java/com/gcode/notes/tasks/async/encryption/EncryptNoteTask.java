package com.gcode.notes.tasks.async.encryption;

import android.app.Activity;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.EncryptionUtils;
import com.gcode.notes.tasks.async.encryption.callbacks.CryptTaskCallbacks;
import com.gcode.notes.ui.helpers.DialogHelper;

public class EncryptNoteTask extends AsyncTask<ContentBase, Void, ContentBase> {
    private Activity mActivity;
    private CryptTaskCallbacks mCryptTaskCallbacks;
    private MaterialDialog mProgressDialog;

    public EncryptNoteTask(Activity activity, CryptTaskCallbacks taskCompletedCallback) {
        mActivity = activity;
        mCryptTaskCallbacks = taskCompletedCallback;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = DialogHelper.buildEncryptNoteProgressDialog(mActivity);
    }

    @Override
    protected ContentBase doInBackground(ContentBase... params) {
        ContentBase contentBase = params[0];
        try {
            if (contentBase instanceof NoteData) {
                //its note
                EncryptionUtils.getInstance("1312").encryptNoteData(((NoteData) contentBase));
            } else if (contentBase instanceof ListData) {
                //its list
                EncryptionUtils.getInstance("1312").encryptListData(((ListData) contentBase));
            } else {
                MyDebugger.log("EncryptNoteTask unknown TYPE.");
                return null;
            }
        } catch (Exception e) {
            MyDebugger.log("EncryptNoteTask exception", e.getMessage());
            return null;
        }
        return contentBase;
    }

    @Override
    protected void onPostExecute(ContentBase contentBase) {
        if (!mActivity.isFinishing()) {
            //if activity is finishing, mProgressDialog#dismiss() will lead to crash
            mProgressDialog.dismiss();
        }
        if (contentBase != null) {
            MyDebugger.log("note encrypted successfully");
            mCryptTaskCallbacks.onTaskCompletedSuccessfully(contentBase);
        }
    }
}
