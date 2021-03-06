package com.gcode.notes.tasks.async.main;


import android.os.AsyncTask;
import android.os.Handler;

import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;

public class AddItemFromDbToMainTask extends AsyncTask<Integer, Void, ContentBase> {
    BaseController mController;

    @Override
    protected ContentBase doInBackground(Integer... params) {
        ContentBase item;

        if(params.length > 0) {
            //adds item with specific id, not last for mode
            item = MyApplication.getWritableDatabase().getNoteFromId(params[0]);
            return item;
        }

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
    protected void onPostExecute(final ContentBase item) {
        if (item != null) {
            //Delay so user can see remove animation
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mController.addItemAsFirst(item);
                }
            }, Constants.DELAY_SO_USER_CAN_SEE);
        } else {
            MyDebugger.log("AddItemTask: item to add is null.");
        }
    }
}
