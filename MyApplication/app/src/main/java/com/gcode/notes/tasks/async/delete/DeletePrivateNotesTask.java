package com.gcode.notes.tasks.async.delete;

import android.os.AsyncTask;

import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;

public class DeletePrivateNotesTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        if (!MyApplication.getWritableDatabase().deleteAllPrivateNotes()) {
            MyDebugger.log("DeletePrivateNotesTask error.");
        }
        return null;
    }
}
