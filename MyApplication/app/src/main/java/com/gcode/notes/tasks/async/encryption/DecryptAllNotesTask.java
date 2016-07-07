package com.gcode.notes.tasks.async.encryption;

import android.app.Activity;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.extras.utils.AuthenticationUtils;
import com.gcode.notes.extras.utils.EncryptionUtils;
import com.gcode.notes.tasks.async.encryption.callbacks.DecryptAllNotesTaskCallbacks;
import com.gcode.notes.ui.helpers.DialogBuilder;

import java.util.ArrayList;

public class DecryptAllNotesTask extends AsyncTask<Void, Integer, ArrayList<ContentBase>> {
    private Activity mActivity;
    private DecryptAllNotesTaskCallbacks mDecryptNotesTaskCallback;
    private MaterialDialog mProgressDialog;
    private ArrayList<ContentBase> mNotesList;
    private boolean mScrollToTop;

    public DecryptAllNotesTask(Activity activity, DecryptAllNotesTaskCallbacks decryptAllNotesTaskCallbacks,
                               ArrayList<ContentBase> notesList, boolean scrollToTop) {
        mActivity = activity;
        mDecryptNotesTaskCallback = decryptAllNotesTaskCallbacks;
        mNotesList = notesList;
        mScrollToTop = scrollToTop;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = DialogBuilder.buildDecryptAllNotesProgressDialog(mActivity);
    }

    @Override
    protected ArrayList<ContentBase> doInBackground(Void... params) {
        int size = mNotesList.size();
        publishProgress(1);
        String password = AuthenticationUtils.getInstance(mActivity, null).getPassword();
        EncryptionUtils encryptionUtils = EncryptionUtils.getInstance(password);
        for (int i = 0; i < size; ++i) {
            ContentBase contentBase = mNotesList.get(i);
            try {
                if (contentBase instanceof NoteData) {
                    //its note
                    encryptionUtils.decryptNoteData(((NoteData) contentBase));
                } else if (contentBase instanceof ListData) {
                    //its list
                    encryptionUtils.decryptListData(((ListData) contentBase));
                } else {
                    MyLogger.log("DecryptNoteTask unknown TYPE.");
                    return null;
                }
            } catch (Exception e) {
                //TODO: handle exception and refactor
                MyLogger.log("DecryptAllNoteTask exception", e.getMessage());
                return null;
            }
            float progress = (i + 1) / (float) size; //i + 1 cuz i starts from 0
            publishProgress((int) (progress * 100));
        }
        publishProgress(100);
        return mNotesList;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int percent = values[0];
        mProgressDialog.setProgress(percent);
    }

    @Override
    protected void onPostExecute(ArrayList<ContentBase> decryptedNotes) {
        if (!mActivity.isFinishing()) {
            //if activity is finishing, mProgressDialog#dismiss() will lead to crash
            mProgressDialog.dismiss();
        }
        if (decryptedNotes != null) {
            mDecryptNotesTaskCallback.onNotesDecryptedSuccessfully(decryptedNotes, mScrollToTop);
        }
    }
}
