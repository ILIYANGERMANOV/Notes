package com.gcode.notes.ui.callbacks.main;

import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;

import com.gcode.notes.R;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.async.main.RemoveItemFromMainTask;

public class NoteDeletedSnackbarCallback extends Snackbar.Callback {
    //TODO: REFACTOR AND OPTIMIZE (optimize FABBehaviour and translate to 0 animation used for security)

    MainActivity mMainActivity;
    ContentBase mNote;
    int mPosition;
    UndoOnClickListener mUndoOnClickListener;

    boolean mOnDismissedCalled; //onDismissed is called multiple times (bug in snackbar),
    //this flag is used to prevent this

    public NoteDeletedSnackbarCallback(MainActivity mainActivity, ContentBase note,
                                       int position, UndoOnClickListener undoOnClickListener) {

        mMainActivity = mainActivity;
        mNote = note;
        mPosition = position;
        mUndoOnClickListener = undoOnClickListener;
    }

    @Override
    public void onDismissed(Snackbar snackbar, int event) {
        if (!mOnDismissedCalled) {
            //onDismissed is called multiple times
            mOnDismissedCalled = true;

            //!NOTE: here to secure bug with FABBehaviour where for some unknown reason fab wasn't translated to original pos
            ViewCompat.animate(mMainActivity.getFabMenu()).
                    translationY(0);

            if (!mUndoOnClickListener.undoTriggered()) {
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
                    MyLogger.log("Failed to send note to recycler bin!");
                    mMainActivity.mMainAdapter.addItem(mPosition, mNote);
                }
            }
        }
    }
}
