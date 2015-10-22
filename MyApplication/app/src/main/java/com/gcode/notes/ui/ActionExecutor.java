package com.gcode.notes.ui;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.controllers.BinController;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.listeners.main.UndoOnClickListener;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.RemoveItemFromMainTask;

public class ActionExecutor {
    //TODO: REFACTOR
    public static void popUndoSnackbar(View rootView, final MainAdapter adapter,
                                       final int position, final ContentBase note) {

        final UndoOnClickListener mUndoOnClickListener = new UndoOnClickListener(position, note);

        Snackbar.Callback mCallback = new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (!mUndoOnClickListener.undoTriggered()) {
                    //undo not triggered ready to delete
                    if (MyApplication.getWritableDatabase().deleteNote(note)) {
                        //note deleted successfully
                        BaseController baseController = BaseController.getInstance();
                        if (baseController.getControllerId() == Constants.CONTROLLER_BIN) {
                            //if in bin controller add item
                            baseController.onItemAdded(note.setAndReturnDeletedMode());
                        } else {
                            //if item still on view remove it
                            new RemoveItemFromMainTask().execute(note);

                        }
                    } else {
                        //failed to delete note, revert it back
                        MyDebugger.log("Failed to send note to recycler bin!");
                        adapter.addItem(position, note);
                    }
                }
                super.onDismissed(snackbar, event);
            }
        };

        SnackbarHelper.buildUndoSnackbar(rootView, mUndoOnClickListener, mCallback).show();
    }


    public static void emptyRecyclerBin(Context context) {

        final MaterialDialog.ButtonCallback mButtonCallback = new MaterialDialog.ButtonCallback() {
            @Override
            public void onNegative(MaterialDialog dialog) {
                dialog.cancel();
            }

            @Override
            public void onPositive(MaterialDialog dialog) {
                if (MyApplication.getWritableDatabase().emptyRecyclerBin()) {
                    BaseController mController = BaseController.getInstance();
                    if (mController.getControllerId() == Constants.CONTROLLER_BIN) {
                        BinController mBinController = (BinController) mController;
                        mBinController.setContent();
                    }
                } else {
                    MyDebugger.log("Failed to empty recycler bin");
                }
                dialog.cancel();
            }
        };
        DialogHelper.buildEmptyBinDialog(context, mButtonCallback);
    }

    public static void deleteNoteFromBin(Context context, final MainAdapter adapter,
                                         final ContentBase note, final int position) {

        final MaterialDialog.ButtonCallback mButtonCallback = new MaterialDialog.ButtonCallback() {
            @Override
            public void onNegative(MaterialDialog dialog) {
                adapter.addItem(position, note);
                adapter.getRecyclerView().smoothScrollToPosition(position);
                dialog.cancel();
            }

            @Override
            public void onPositive(MaterialDialog dialog) {
                if (!MyApplication.getWritableDatabase().deleteNoteFromBin(note)) {
                    adapter.addItem(position, note);
                    adapter.getRecyclerView().smoothScrollToPosition(position);
                    MyDebugger.log("Failed to delete note from recycler bin!");
                }
                dialog.cancel();
            }
        };
        DialogHelper.buildDeleteNoteFromBinDialog(context, mButtonCallback);
    }
}
