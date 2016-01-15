package com.gcode.notes.tasks.async.main;

import android.os.AsyncTask;
import android.widget.Toast;

import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.notes.MyApplication;

public class RemoveItemFromMainTask extends AsyncTask<ContentBase, Void, Boolean> {
    MainAdapter mAdapter;
    int mItemPosition;
    String mMessage;

    public RemoveItemFromMainTask(String message) {
        mMessage = message;
    }

    @Override
    protected Boolean doInBackground(ContentBase... params) {
        ContentBase item = params[0];
        mAdapter = BaseController.getInstance().getMainAdapter();

        if (mAdapter != null) {
            mItemPosition = mAdapter.getIndexOfItem(item);
            if (mItemPosition != -1) {
                //item exists proceed deleting it
                return true;
            }
        } else {
            MyDebugger.log("RemoveItemFromMainTask", "MainAdapter is null!");
            return false;
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean deleteItem) {
        if (deleteItem) {
            //delete item
            mAdapter.removeItem(mItemPosition);
            Toast.makeText(MyApplication.getAppContext(), mMessage, Toast.LENGTH_LONG).show();
        }
    }
}
