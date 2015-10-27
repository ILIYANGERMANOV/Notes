package com.gcode.notes.ui;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.listeners.main.UndoOnClickListener;
import com.gcode.notes.ui.callbacks.DeleteNoteFromBinCallback;
import com.gcode.notes.ui.callbacks.DeleteNoteFromDisplayCallback;
import com.gcode.notes.ui.callbacks.EmptyRecyclerBinCallback;
import com.gcode.notes.ui.callbacks.PopUndoSnackbarCallback;
import com.gcode.notes.ui.callbacks.RestoreNoteFromDisplayCallback;
import com.gcode.notes.ui.snackbar.SnackbarHelper;

public class ActionExecutor {
    public static void popUndoSnackbar(View rootView, MainAdapter adapter,
                                       int position, ContentBase note) {

        final UndoOnClickListener undoOnClickListener = new UndoOnClickListener(position, note);
        Snackbar.Callback snackbarCallback = new PopUndoSnackbarCallback(adapter, note, position, undoOnClickListener);
        SnackbarHelper.buildUndoSnackbar(rootView, undoOnClickListener, snackbarCallback).show();
    }


    public static void emptyRecyclerBin(Context context) {
        final MaterialDialog.ButtonCallback emptyRecyclerBinCallback = new EmptyRecyclerBinCallback();
        DialogHelper.buildEmptyBinDialog(context, emptyRecyclerBinCallback);
    }

    public static void restoreDeletedNote(Activity activity, ContentBase note) {
        MaterialDialog.ButtonCallback restoreNoteFromDisplayCallback = new RestoreNoteFromDisplayCallback(activity, note);
        DialogHelper.buildRestoreNoteFromDisplayDialog(activity, restoreNoteFromDisplayCallback);
    }

    public static void deleteNoteFromDisplayBin(Activity activity, ContentBase note) {
        MaterialDialog.ButtonCallback deleteNoteFromDisplayCallback = new DeleteNoteFromDisplayCallback(activity, note);
        DialogHelper.buildDeleteNoteFromBinDialog(activity, deleteNoteFromDisplayCallback, true);
    }

    public static void deleteNoteFromBin(Context context, MainAdapter adapter, ContentBase note, int position) {
        MaterialDialog.ButtonCallback deleteNoteFromBinCallback = new DeleteNoteFromBinCallback(adapter, position, note);
        DialogHelper.buildDeleteNoteFromBinDialog(context, deleteNoteFromBinCallback, false);
    }
}
