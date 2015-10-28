package com.gcode.notes.listeners.main;

import android.view.View;

import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.tasks.UndoNoteDeletionTask;


public class NoteDeletedUndoOnClickListener implements View.OnClickListener {
    boolean mUndo;

    int mPosition;
    ContentBase mNote;

    public NoteDeletedUndoOnClickListener(int position, ContentBase note) {
        mPosition = position;
        mNote = note;
    }

    @Override
    public void onClick(View v) {
        mUndo = true;

        int controllerId = BaseController.getInstance().getControllerId();
        int noteMode = mNote.getMode();

        boolean addItem = false;

        switch (controllerId) {
            case Constants.CONTROLLER_ALL_NOTES:
                if (noteMode == Constants.MODE_NORMAL || noteMode == Constants.MODE_IMPORTANT) {
                    addItem = true;
                }
                break;
            case Constants.CONTROLLER_IMPORTANT:
                if (noteMode == Constants.MODE_IMPORTANT) {
                    addItem = true;
                }
                break;
            case Constants.CONTROLLER_PRIVATE:
                if (noteMode == Constants.MODE_PRIVATE) {
                    addItem = true;
                }
                break;
            case Constants.CONTROLLER_BIN:
                return;
            default:
                MyDebugger.log("UndoOnClickListener", "Unknown controller id.");
                return;
        }

        if (addItem) {
            new UndoNoteDeletionTask(mPosition).execute(mNote);
        }
    }


    public boolean undoTriggered() {
        return mUndo;
    }
}
