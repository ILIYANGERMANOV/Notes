package com.gcode.notes.tasks.async.encryption;

import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.activities.compose.ComposeBaseActivity;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.encryption.EncryptionUtils;
import com.gcode.notes.tasks.async.encryption.callbacks.EncryptTaskCallbacks;
import com.gcode.notes.ui.helpers.DialogHelper;

public class EncryptNoteTask extends AsyncTask<ContentBase, Void, ContentBase> {
    private ComposeBaseActivity mComposeBaseActivity;
    private EncryptTaskCallbacks mEncryptTaskCallbacks;
    private MaterialDialog mProgressDialog;

    public EncryptNoteTask(ComposeBaseActivity composeBaseActivity, EncryptTaskCallbacks encryptTaskCallbacks) {
        mComposeBaseActivity = composeBaseActivity;
        mEncryptTaskCallbacks = encryptTaskCallbacks;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = DialogHelper.buildEncryptNoteProgressDialog(mComposeBaseActivity);
    }

    @Override
    protected ContentBase doInBackground(ContentBase... params) {
        ContentBase contentBase = params[0];
        try {
            if (contentBase instanceof NoteData) {
                //its note
                EncryptionUtils.getInstance("1312aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaadasdasdassdgsdg").encryptNoteData(((NoteData) contentBase));
            } else if (contentBase instanceof ListData) {
                //its list
                EncryptionUtils.getInstance("1312aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaadasdasdassdgsdg").encryptListData(((ListData) contentBase));
            } else {
                MyDebugger.log("EncryptNoteTask unknown TYPE.");
                return null;
            }
        } catch (Exception e) {
            //TODO: handle exception
            MyDebugger.log("EncryptNoteTask exception", e.getMessage());
            return null;
        }
        return contentBase;
    }

    @Override
    protected void onPostExecute(ContentBase contentBase) {
        if (!mComposeBaseActivity.isFinishing()) {
            //if activity is finishing, mProgressDialog#dismiss() will lead to crash
            mProgressDialog.dismiss();
        }
        if (contentBase != null) {
            MyDebugger.log("note encrypted successfully");
            mEncryptTaskCallbacks.onEncryptedSuccessfully(contentBase, mComposeBaseActivity.mIsOpenedInEditMode);
        }
    }
}
