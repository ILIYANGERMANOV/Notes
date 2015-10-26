package com.gcode.notes.ui.snackbar;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarHelper {
    public static Snackbar buildUndoSnackbar(View rootView, View.OnClickListener undoOnClickListener,
                                             Snackbar.Callback callback) {

        Snackbar snackbar = Snackbar.make(rootView, SnackbarMessages.NOTE_DELETED_MESSAGE, Snackbar.LENGTH_LONG);
        snackbar.setAction(SnackbarMessages.UNDO_SNACKBAR_ACTION_MESSAGE, undoOnClickListener).setCallback(callback);
        return snackbar;
    }

    public static void showShortSnackbar(View rooView, String message) {
        Snackbar.make(rooView, message, Snackbar.LENGTH_SHORT).show();
    }
}
