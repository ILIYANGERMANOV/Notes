package com.gcode.notes.ui.callbacks;

import android.support.design.widget.Snackbar;

import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.listeners.main.NoteDeletedUndoOnClickListener;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.RemoveItemFromMainTask;

public class NoteDeletedSnackbarCallback extends Snackbar.Callback {
    MainAdapter mAdapter;
    ContentBase mNote;
    int mPosition;
    NoteDeletedUndoOnClickListener mNoteDeletedUndoOnClickListener;

    boolean mOnDismissedCalled;

    public NoteDeletedSnackbarCallback(MainAdapter adapter, ContentBase note,
                                       int position, NoteDeletedUndoOnClickListener noteDeletedUndoOnClickListener) {

        mAdapter = adapter;
        mNote = note;
        mPosition = position;
        mNoteDeletedUndoOnClickListener = noteDeletedUndoOnClickListener;
    }

    @Override
    public void onDismissed(Snackbar snackbar, int event) {
        if (!mOnDismissedCalled) {
            //onDismissed is called multiple times
            mOnDismissedCalled = true;
            if (!mNoteDeletedUndoOnClickListener.undoTriggered()) {
                //undo not triggered ready to delete
                if (MyApplication.getWritableDatabase().deleteNote(mNote)) {
                    //note deleted successfully
                    BaseController baseController = BaseController.getInstance();
                    if (baseController.getControllerId() == Constants.CONTROLLER_BIN) {
                        //if in bin controller add item
                        baseController.onItemAdded(mNote.setAndReturnDeletedMode());
                    } else {
                        //if item still on view remove it
                        new RemoveItemFromMainTask("Note moved to Bin.").execute(mNote);
                    }
                } else {
                    //failed to delete note, revert it back
                    MyDebugger.log("Failed to send note to recycler bin!");
                    mAdapter.addItem(mPosition, mNote);
                }
            }
        }
    }
}
