package com.gcode.notes.tasks;

import android.os.AsyncTask;

import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.extras.MyDebugger;

public class RemoveItemFromMainTask extends AsyncTask<ContentBase, Void, Boolean> {
    MainAdapter mAdapter;
    int mItemPosition;

    @Override
    protected Boolean doInBackground(ContentBase... params) {
        ContentBase item = params[0];
        mAdapter = BaseController.getInstance().getMainAdapter();

        if (mAdapter != null) {
            mItemPosition = mAdapter.getIndexOfItem(item);
            if (mItemPosition != -1) {
                //item exits proceed deleting it
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
        }
    }
}
