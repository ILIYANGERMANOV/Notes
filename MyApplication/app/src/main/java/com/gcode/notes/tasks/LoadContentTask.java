package com.gcode.notes.tasks;


import android.os.AsyncTask;

import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;

import java.util.ArrayList;

public class LoadContentTask extends AsyncTask<Void, Void, ArrayList<ContentBase>> {

    BaseController mController;

    public LoadContentTask(BaseController controller) {
        mController = controller;
    }

    @Override
    protected ArrayList<ContentBase> doInBackground(Void... params) {
        switch (mController.getControllerId()) {
            case Constants.CONTROLLER_ALL_NOTES:
                return MyApplication.getWritableDatabase().getAllVisibleNotes();
            case Constants.CONTROLLER_IMPORTANT:
                return MyApplication.getWritableDatabase().getAllImportantNotes();
            case Constants.CONTROLLER_PRIVATE:
                return MyApplication.getWritableDatabase().getAllPrivateNotes();
            case Constants.CONTROLLER_BIN:
                return MyApplication.getWritableDatabase().getAllDeletedNotes();
            default:
                MyDebugger.log("LoadContentTask invalid controller id");
                return null;
        }
    }

    @Override
    protected void onPostExecute(ArrayList<ContentBase> contentList) {
        if (contentList != null) {
            mController.setNewContent(contentList);
        }
    }
}
