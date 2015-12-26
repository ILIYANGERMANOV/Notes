package com.gcode.notes.tasks.async;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.note.base.ContentBase;

public class UndoNoteDeletionTask extends AsyncTask<ContentBase, Void, ContentBase> {
    MainAdapter mAdapter;
    RecyclerView mRecyclerView;
    int mPosition;

    public UndoNoteDeletionTask(int position) {
        mPosition = position;
    }

    @Override
    protected ContentBase doInBackground(ContentBase... params) {
        ContentBase item = params[0];
        BaseController baseController = BaseController.getInstance();
        mAdapter = baseController.getMainAdapter();

        if (!mAdapter.itemExists(item)) {
            //item not exits to list proceed adding it
            mRecyclerView = baseController.getRecyclerView();
            return item;
        } else {
            //item already exists, don't add it cuz will duplicate
            return null;
        }
    }

    @Override
    protected void onPostExecute(ContentBase item) {
        if (item != null) {
            //item not exists in list, add it
            if(mAdapter.addItem(mPosition, item)) {
                mRecyclerView.smoothScrollToPosition(mPosition);
            }
        }
    }
}
