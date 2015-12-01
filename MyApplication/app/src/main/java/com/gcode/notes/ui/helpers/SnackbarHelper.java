package com.gcode.notes.ui.helpers;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.gcode.notes.R;

public class SnackbarHelper {
    public static Snackbar buildNoteDeletedSnackbar(View rootView, View.OnClickListener undoOnClickListener,
                                                    Snackbar.Callback callback) {

        Snackbar snackbar = Snackbar.make(rootView, R.string.note_deleted_snackbar_message, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snackbar_action_undo, undoOnClickListener).setCallback(callback);
        return snackbar;
    }

    public static Snackbar buildListItemDeletedSnackbar(View rootView, View.OnClickListener undoOnClickListener) {
        Snackbar snackbar = Snackbar.make(rootView, R.string.list_item_removed_snackbar_message, Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snackbar_action_undo, undoOnClickListener);
        return snackbar;
    }


    public static void showShortSnackbar(View rooView, int resId) {
        Snackbar.make(rooView, resId, Snackbar.LENGTH_SHORT).show();
    }
}
