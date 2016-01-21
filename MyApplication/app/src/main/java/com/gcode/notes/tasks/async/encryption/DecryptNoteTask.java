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
import com.gcode.notes.tasks.async.encryption.callbacks.CryptTaskCallbacks;
import com.gcode.notes.ui.helpers.DialogBuilder;

public class DecryptNoteTask extends AsyncTask<ContentBase, Void, ContentBase> {
    private Activity mActivity;
    private CryptTaskCallbacks mCryptTaskCallbacks;
    private MaterialDialog mProgressDialog;

    public DecryptNoteTask(Activity activity, CryptTaskCallbacks taskCompletedCallback) {
        mActivity = activity;
        mCryptTaskCallbacks = taskCompletedCallback;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = DialogBuilder.buildDecryptNoteProgressDialog(mActivity);
    }

    @Override
    protected ContentBase doInBackground(ContentBase... params) {
        ContentBase contentBase = params[0];
        String password = AuthenticationUtils.getInstance(mActivity, null).getPassword();
        EncryptionUtils encryptionUtils = EncryptionUtils.getInstance(password);
        try {
            if (contentBase instanceof NoteData) {
                //its note
               encryptionUtils.decryptNoteData(((NoteData) contentBase));
            } else if (contentBase instanceof ListData) {
                //its list
                encryptionUtils.decryptListData(((ListData) contentBase));
            } else {
                MyDebugger.log("DecryptNoteTask unknown TYPE.");
                return null;
            }
        } catch (Exception e) {
            //TODO: handle exception and refactor
            MyDebugger.log("DecryptNoteTask exception", e.getMessage());
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
            MyDebugger.log("note decrypted successfully");
            mCryptTaskCallbacks.onTaskCompletedSuccessfully(contentBase);
        }
    }
}
