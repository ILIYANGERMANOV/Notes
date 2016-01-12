package com.gcode.notes.tasks.async.compose;

import android.os.AsyncTask;

import com.gcode.notes.data.base.ContentBase;

public class EncryptNoteTask extends AsyncTask<ContentBase, Void, ContentBase> {
    @Override
    protected ContentBase doInBackground(ContentBase... params) {
        ContentBase contentBase = params[0];

        return null;
    }

    @Override
    protected void onPostExecute(ContentBase contentBase) {

    }
}
