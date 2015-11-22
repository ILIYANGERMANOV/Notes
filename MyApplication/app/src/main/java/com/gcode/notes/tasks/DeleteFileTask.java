package com.gcode.notes.tasks;

import android.net.Uri;
import android.os.AsyncTask;

import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.FileUtils;

public class DeleteFileTask extends AsyncTask<String, Void, Void> {
    @Override
    protected Void doInBackground(String... params) {
        if(!FileUtils.deleteFile(params[0])) {
            MyDebugger.log("Failed to delete file!");
        }
        return null;
    }
}
