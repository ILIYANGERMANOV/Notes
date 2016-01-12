package com.gcode.notes.tasks.async.display;


import android.os.AsyncTask;

import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;

public class UpdateListAttributesTask extends AsyncTask<ListData, Void, Void> {
    @Override
    protected Void doInBackground(ListData... params) {
        if (!MyApplication.getWritableDatabase().updateListAttributes(params[0])) {
            MyDebugger.log("Failed to update list attributes.");
        }
        return null;
    }
}
