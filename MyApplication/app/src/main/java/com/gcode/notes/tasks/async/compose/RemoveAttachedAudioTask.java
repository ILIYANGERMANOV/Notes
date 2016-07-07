package com.gcode.notes.tasks.async.compose;

import android.os.AsyncTask;

import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.notes.MyApplication;

public class RemoveAttachedAudioTask extends AsyncTask<Integer, Void, Void> {
    @Override
    protected Void doInBackground(Integer... params) {
        if (!MyApplication.getWritableDatabase().removeAttachedAudioFromNote(params[0])) {
            MyLogger.log("Failed to remove audio from note");
        }
        return null;
    }
}
