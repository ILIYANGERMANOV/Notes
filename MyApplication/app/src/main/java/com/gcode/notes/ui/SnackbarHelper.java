package com.gcode.notes.ui;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.gcode.notes.R;

public class SnackbarHelper {
    public static Snackbar buildUndoSnackbar(View rootView, View.OnClickListener mUndoOnClickListener,
                                             Snackbar.Callback mCallback) {

        Snackbar snackbar = Snackbar.make(rootView, "Note deleted", Snackbar.LENGTH_LONG);
        snackbar.setAction(R.string.snackbar_action_undo, mUndoOnClickListener).setCallback(mCallback);
        return snackbar;
    }

    public static void showShortSnackbar(View rooView, String message) {
        Snackbar.make(rooView, message, Snackbar.LENGTH_SHORT).show();
    }
}
