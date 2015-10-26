package com.gcode.notes.ui.callbacks;

import android.support.design.widget.Snackbar;

import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.listeners.main.UndoOnClickListener;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.RemoveItemFromMainTask;

public class PopUndoSnackbarCallback extends Snackbar.Callback {
    MainAdapter mAdapter;
    ContentBase mNote;
    int mPosition;
    UndoOnClickListener mUndoOnClickListener;

    public PopUndoSnackbarCallback(MainAdapter adapter, ContentBase note,
                                   int position, UndoOnClickListener undoOnClickListener) {

        mAdapter = adapter;
        mNote = note;
        mPosition = position;
        mUndoOnClickListener = undoOnClickListener;
    }

    @Override
    public void onDismissed(Snackbar snackbar, int event) {
        if (!mUndoOnClickListener.undoTriggered()) {
            //undo not triggered ready to delete
            if (MyApplication.getWritableDatabase().deleteNote(mNote)) {
                //note deleted successfully
                BaseController baseController = BaseController.getInstance();
                if (baseController.getControllerId() == Constants.CONTROLLER_BIN) {
                    //if in bin controller add item
                    //TODO: fix lastDeletedNote
                    baseController.onItemAdded(mNote.setAndReturnDeletedMode());
                } else {
                    //if item still on view remove it
                    new RemoveItemFromMainTask().execute(mNote);
                }
            } else {
                //failed to delete note, revert it back
                MyDebugger.log("Failed to send note to recycler bin!");
                mAdapter.addItem(mPosition, mNote);
            }
        }
        super.onDismissed(snackbar, event);
    }
}
