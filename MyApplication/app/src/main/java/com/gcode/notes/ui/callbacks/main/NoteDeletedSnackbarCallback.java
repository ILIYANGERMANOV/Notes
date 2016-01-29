package com.gcode.notes.ui.callbacks.main;

import android.support.design.widget.Snackbar;

import com.gcode.notes.R;
import com.gcode.notes.adapters.main.MainAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.motions.MyAnimator;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.async.main.RemoveItemFromMainTask;

public class NoteDeletedSnackbarCallback extends Snackbar.Callback {
    //TODO: REFACTOR AND OPTIMIZE
    MainAdapter mAdapter;
    ContentBase mNote;
    int mPosition;
    NoteDeletedUndoOnClickListener mNoteDeletedUndoOnClickListener;

    boolean mOnDismissedCalled; //onDismissed is called multiple times (bug in snackbar),
    //this flag is used to prevent this

    public NoteDeletedSnackbarCallback(MainAdapter adapter, ContentBase note,
                                       int position, NoteDeletedUndoOnClickListener noteDeletedUndoOnClickListener) {

        mAdapter = adapter;
        mNote = note;
        mPosition = position;
        mNoteDeletedUndoOnClickListener = noteDeletedUndoOnClickListener;
    }

    @Override
    public void onShown(Snackbar snackbar) {
        MyAnimator.translateFabButtonUp(mAdapter.getMainActivity().getFabMenu(), snackbar.getView().getHeight());
    }

    @Override
    public void onDismissed(Snackbar snackbar, int event) {
        if (!mOnDismissedCalled) {
            //onDismissed is called multiple times

            MyAnimator.translateFabButtonDown(mAdapter.getMainActivity().getFabMenu());

            mOnDismissedCalled = true;
            if (!mNoteDeletedUndoOnClickListener.undoTriggered()) {
                //undo not triggered ready to delete
                if (MyApplication.getWritableDatabase().deleteNote(mNote)) {
                    //note deleted successfully
                    BaseController baseController = BaseController.getInstance();
                    if (baseController.getControllerId() != Constants.CONTROLLER_BIN) {
                        //item is still current controller, remove it
                        new RemoveItemFromMainTask(MyApplication.getAppContext().
                                getString(R.string.note_moved_to_bin)).execute(mNote);

                    } else {
                        //if in bin controller add newly deleted note
                        //!NOTE: delete mode is already set in db#deleteNote()
                        baseController.onNewNoteAdded(mNote.getMode());
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
