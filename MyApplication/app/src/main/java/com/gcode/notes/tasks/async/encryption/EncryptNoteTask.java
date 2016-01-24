package com.gcode.notes.tasks.async.encryption;

import android.app.Activity;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.AuthenticationUtils;
import com.gcode.notes.extras.utils.EncryptionUtils;
import com.gcode.notes.tasks.async.encryption.callbacks.EncryptTaskCallbacks;
import com.gcode.notes.ui.helpers.DialogBuilder;

public class EncryptNoteTask extends AsyncTask<ContentBase, Void, ContentBase> {
    private Activity mActivity;
    private EncryptTaskCallbacks mEncryptTaskCallbacks;
    private MaterialDialog mProgressDialog;

    public EncryptNoteTask(Activity activity, EncryptTaskCallbacks encryptTaskCallbacks) {
        mActivity = activity;
        mEncryptTaskCallbacks = encryptTaskCallbacks;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = DialogBuilder.buildEncryptNoteProgressDialog(mActivity);
    }

    @Override
    protected ContentBase doInBackground(ContentBase... params) {
        ContentBase contentBase = params[0];
        String password = AuthenticationUtils.getInstance(mActivity, null).getPassword();
        EncryptionUtils encryptionUtils = EncryptionUtils.getInstance(password);
        try {
            if (contentBase instanceof NoteData) {
                //its note
                encryptionUtils.encryptNoteData(((NoteData) contentBase));
            } else if (contentBase instanceof ListData) {
                //its list
                encryptionUtils.encryptListData(((ListData) contentBase));
            } else {
                MyDebugger.log("EncryptNoteTask unknown TYPE.");
                return null;
            }
        } catch (Exception e) {
            //TODO: handle exception and refactor
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
            mEncryptTaskCallbacks.onEncryptedSuccessfully(contentBase);
        }
    }
}
