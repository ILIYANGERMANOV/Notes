package com.gcode.notes.tasks.async.compose;

import android.app.Activity;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.EncryptionUtils;
import com.gcode.notes.tasks.async.callbacks.CryptographyTaskCompletedCallback;
import com.gcode.notes.ui.helpers.DialogHelper;

public class EncryptNoteTask extends AsyncTask<ContentBase, Void, ContentBase> {
    private Activity mActivity;
    private CryptographyTaskCompletedCallback mTaskCompletedCallback;
    private MaterialDialog mProgressDialog;

    public EncryptNoteTask(Activity activity, CryptographyTaskCompletedCallback taskCompletedCallback) {
        mActivity = activity;
        mTaskCompletedCallback = taskCompletedCallback;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = DialogHelper.buildEncryptionProgressDialog(mActivity);
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
            mTaskCompletedCallback.onTaskCompletedSuccessfully(contentBase);
        }
    }
}
