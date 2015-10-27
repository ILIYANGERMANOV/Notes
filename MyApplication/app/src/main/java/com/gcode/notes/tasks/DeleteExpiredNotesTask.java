package com.gcode.notes.tasks;

import android.os.AsyncTask;

import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;

public class DeleteExpiredNotesTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        if (!MyApplication.getWritableDatabase().deleteExpiredNotes()) {
            MyDebugger.log("Failed to delete expired notes");
        }
        return null;
    }
}
