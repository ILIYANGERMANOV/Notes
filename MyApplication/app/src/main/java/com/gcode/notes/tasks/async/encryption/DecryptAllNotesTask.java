package com.gcode.notes.tasks.async.encryption;

import android.app.Activity;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.EncryptionUtils;
import com.gcode.notes.tasks.async.encryption.callbacks.DecryptNotesTaskCallbacks;
import com.gcode.notes.ui.helpers.DialogHelper;

import java.util.ArrayList;

public class DecryptAllNotesTask extends AsyncTask<Void, Integer, ArrayList<ContentBase>> {
    private Activity mActivity;
    private DecryptNotesTaskCallbacks mDecryptNotesTaskCallback;
    private MaterialDialog mProgressDialog;
    private ArrayList<ContentBase> mNotesList;
    private boolean mScrollToTop;

    public DecryptAllNotesTask(Activity activity, DecryptNotesTaskCallbacks decryptNotesTaskCallbacks,
                               ArrayList<ContentBase> notesList, boolean scrollToTop) {
        mActivity = activity;
        mDecryptNotesTaskCallback = decryptNotesTaskCallbacks;
        mNotesList = notesList;
        mScrollToTop = scrollToTop;
    }

    @Override
    protected void onPreExecute() {
        mProgressDialog = DialogHelper.buildDecryptAllNotesProgressDialog(mActivity);
    }

    @Override
    protected ArrayList<ContentBase> doInBackground(Void... params) {
        int size = mNotesList.size();
        publishProgress(1);
        for (int i = 0; i < size; ++i) {
            ContentBase contentBase = mNotesList.get(i);
            try {
                if (contentBase instanceof NoteData) {
                    //its note
                    EncryptionUtils.getInstance("1312").decryptNoteData(((NoteData) contentBase));
                } else if (contentBase instanceof ListData) {
                    //its list
                    EncryptionUtils.getInstance("1312").decryptListData(((ListData) contentBase));
                } else {
                    MyDebugger.log("DecryptNoteTask unknown TYPE.");
                    return null;
                }
            } catch (Exception e) {
                MyDebugger.log("DecryptNoteTask exception", e.getMessage());
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
        MyDebugger.log("percent", percent);
        mProgressDialog.setProgress(percent);
    }

    @Override
    protected void onPostExecute(ArrayList<ContentBase> decryptedNotes) {
        if (!mActivity.isFinishing()) {
            //if activity is finishing, mProgressDialog#dismiss() will lead to crash
            mProgressDialog.dismiss();
        }
        if (decryptedNotes != null) {
            MyDebugger.log("note decrypted successfully");
            mDecryptNotesTaskCallback.onNotesDecryptedSuccessfully(decryptedNotes, mScrollToTop);
        }
    }
}
