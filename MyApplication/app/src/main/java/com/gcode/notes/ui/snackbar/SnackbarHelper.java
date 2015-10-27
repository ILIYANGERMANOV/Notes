package com.gcode.notes.ui.snackbar;

import android.support.design.widget.Snackbar;
import android.view.View;

public class SnackbarHelper {
    public static Snackbar buildNoteDeletedSnackbar(View rootView, View.OnClickListener undoOnClickListener,
                                                    Snackbar.Callback callback) {

        Snackbar snackbar = Snackbar.make(rootView, SnackbarMessages.NOTE_DELETED_MESSAGE, Snackbar.LENGTH_LONG);
        snackbar.setAction(SnackbarMessages.SNACKBAR_ACTION_UNDO, undoOnClickListener).setCallback(callback);
        return snackbar;
    }

    public static Snackbar buildListItemDeletedSnackbar(View rootView, View.OnClickListener undoOnClickListener) {
        Snackbar snackbar = Snackbar.make(rootView, SnackbarMessages.LIST_ITEM_REMOVED_MESSAGE, Snackbar.LENGTH_LONG);
        snackbar.setAction(SnackbarMessages.SNACKBAR_ACTION_UNDO, undoOnClickListener);
        return snackbar;
    }


    public static void showShortSnackbar(View rooView, String message) {
        Snackbar.make(rooView, message, Snackbar.LENGTH_SHORT).show();
    }
}
