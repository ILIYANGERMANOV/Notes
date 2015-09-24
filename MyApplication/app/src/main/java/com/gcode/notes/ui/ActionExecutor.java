package com.gcode.notes.ui;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.adapters.NotesAdapter;
import com.gcode.notes.controllers.BaseController;
import com.gcode.notes.controllers.BinController;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.listeners.UndoOnClickListener;
import com.gcode.notes.notes.MyApplication;

public class ActionExecutor {
    public static void popUndoSnackbar(View mRoot, RecyclerView mRecyclerView, final NotesAdapter mAdapter,
                                       final int position, final ContentBase mNote) {

        final UndoOnClickListener mUndoOnClickListener = new UndoOnClickListener(mRecyclerView, mAdapter, position, mNote);
        Snackbar.Callback mCallback = new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar snackbar, int event) {
                if (!mUndoOnClickListener.undoTriggered()) {
                    if (!MyApplication.getWritableDatabase().deleteNote(mNote)) {
                        mAdapter.addItem(position, mNote);
                        MyDebugger.log("Failed to delete note from recycler bin!");
                    } else {
                        BaseController mController = BaseController.getInstance();
                        if (mController.getControllerId() == Constants.CONTROLLER_BIN) {
                            mController.update(mNote.setAndReturnDeletedMode());
                        }

                    }
                }
                super.onDismissed(snackbar, event);
            }
        };

        SnackbarHelper.buildUndoSnackbar(mRoot, mUndoOnClickListener, mCallback).show();
    }


    public static void emptyRecyclerBin(Context mContext) {

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
        DialogHelper.buildEmptyBinDialog(mContext, mButtonCallback);
    }

    public static void deleteNoteFromBin(Context mContext, final NotesAdapter mAdapter,
                                         final ContentBase mNote, final int mPosition) {

        final MaterialDialog.ButtonCallback mButtonCallback = new MaterialDialog.ButtonCallback() {
            @Override
            public void onNegative(MaterialDialog dialog) {
                mAdapter.addItem(mPosition, mNote);
                dialog.cancel();
            }

            @Override
            public void onPositive(MaterialDialog dialog) {
                if (!MyApplication.getWritableDatabase().deleteNoteFromBin(mNote)) {
                    mAdapter.addItem(mPosition, mNote);
                    MyDebugger.log("Failed to delete note from recycler bin!");
                }
                dialog.cancel();
            }
        };
        DialogHelper.buildDeleteNoteFromBinDialog(mContext, mButtonCallback);
    }
}
