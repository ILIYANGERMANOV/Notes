package com.gcode.notes.ui;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.gcode.notes.R;

public class SnackbarHelper {
    public static Snackbar buildUndoSnackbar(View mRoot, View.OnClickListener mUndoOnClickListener,
                                             Snackbar.Callback mCallback) {

        Snackbar snackbar = Snackbar.make(mRoot, "Note deleted", Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.snackbar_action_undo, mUndoOnClickListener).setCallback(mCallback);
        return snackbar;
    }
}
