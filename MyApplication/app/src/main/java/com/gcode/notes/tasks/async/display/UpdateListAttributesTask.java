package com.gcode.notes.tasks.async.display;


import android.os.AsyncTask;

import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.AuthenticationUtils;
import com.gcode.notes.extras.utils.EncryptionUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;

public class UpdateListAttributesTask extends AsyncTask<ListData, Void, Void> {
    @Override
    protected Void doInBackground(ListData... params) {
        ListData listData = params[0];
        if (listData.getMode() == Constants.MODE_PRIVATE) {
            //list data is private, its attributes need encryption
            listData = new ListData(listData); //reconstruct object,
            //so encryption won't change list data attributes in main activity
            try {
                String password = AuthenticationUtils.getInstance(null, null).getPassword();
                EncryptionUtils.getInstance(password).encryptListDataAttributes(listData);
            } catch (Exception e) {
                MyDebugger.log("UpdateListAttributesTask encryption exception", e.getMessage());
                return null;
            }
        }

        //save changes to db
        if (!MyApplication.getWritableDatabase().updateListAttributes(listData)) {
            MyDebugger.log("Failed to update list attributes.");
            MyDebugger.log("targetId", listData.getTargetId());
        }
        return null;
    }
}
