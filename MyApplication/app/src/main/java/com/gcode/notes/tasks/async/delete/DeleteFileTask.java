package com.gcode.notes.tasks.async.delete;

import android.os.AsyncTask;

import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.extras.utils.FileUtils;

public class DeleteFileTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        if(!FileUtils.deleteFile(params[0])) {
            MyLogger.log("Failed to delete file!");
        }
        return null;
    }
}
