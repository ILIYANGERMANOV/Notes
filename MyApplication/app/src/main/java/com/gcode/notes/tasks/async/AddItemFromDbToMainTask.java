package com.gcode.notes.tasks.async;


import android.os.AsyncTask;

import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;

public class AddItemFromDbToMainTask extends AsyncTask<Void, Void, ContentBase> {
    BaseController mController;

    @Override
    protected ContentBase doInBackground(Void... params) {
        ContentBase item;
        mController = BaseController.getInstance();
        switch (mController.getControllerId()) {
            case Constants.CONTROLLER_ALL_NOTES:
                item = MyApplication.getWritableDatabase().getLastVisibleNote();
                break;
            case Constants.CONTROLLER_IMPORTANT:
                item = MyApplication.getWritableDatabase().getLastImportantNote();
                break;
            case Constants.CONTROLLER_PRIVATE:
                item = MyApplication.getWritableDatabase().getLastPrivateNote();
                break;
            case Constants.CONTROLLER_BIN:
                item = MyApplication.getWritableDatabase().getLastDeletedNote();
                break;
            default:
                MyDebugger.log("AddItemTask invalid controller id.");
                return null;
        }
        return item;
    }


    @Override
    protected void onPostExecute(ContentBase item) {
        if (item != null) {
            mController.addItem(item);
        } else {
            MyDebugger.log("AddItemTask: item to add is null.");
        }
    }
}
