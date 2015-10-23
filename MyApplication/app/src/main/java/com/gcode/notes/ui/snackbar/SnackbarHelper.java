package com.gcode.notes.ui.snackbar;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarHelper {
    public static Snackbar buildUndoSnackbar(View rootView, View.OnClickListener mUndoOnClickListener,
                                             Snackbar.Callback mCallback) {

        Snackbar snackbar = Snackbar.make(rootView, SnackbarMessages.NOTE_DELETED_MESSAGE, Snackbar.LENGTH_LONG);
        snackbar.setAction(SnackbarMessages.UNDO_SNACKBAR_ACTION_MESSAGE, mUndoOnClickListener).setCallback(mCallback);
        return snackbar;
    }

    public static void showShortSnackbar(View rooView, String message) {
        Snackbar.make(rooView, message, Snackbar.LENGTH_SHORT).show();
    }
}
