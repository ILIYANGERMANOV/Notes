package com.gcode.notes.tasks;


import android.os.AsyncTask;

import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.notes.MyApplication;

import java.util.ArrayList;

public class LoadContentTask extends AsyncTask<Integer, Void, ArrayList<ContentBase>> {

    BaseController mController;

    public LoadContentTask(BaseController controller) {
        mController = controller;
    }

    @Override
    protected ArrayList<ContentBase> doInBackground(Integer... params) {
        int mode = params[0];
        switch (mode) {
            case Constants.CONTROLLER_ALL_NOTES:
                return MyApplication.getWritableDatabase().getAllVisibleNotes();
            case Constants.CONTROLLER_IMPORTANT:
                return MyApplication.getWritableDatabase().getAllImportantNotes();
            case Constants.CONTROLLER_PRIVATE:
                return MyApplication.getWritableDatabase().getAllPrivateNotes();
            case Constants.CONTROLLER_BIN:
                return MyApplication.getWritableDatabase().getAllDeletedNotes();
        }
        return null;
    }

    @Override
    protected void onPostExecute(ArrayList<ContentBase> contentList) {
        if(contentList != null) {
            mController.setNewContent(contentList);
        }
    }
}
