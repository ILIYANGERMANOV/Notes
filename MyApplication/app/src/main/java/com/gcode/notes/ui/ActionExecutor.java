package com.gcode.notes.ui;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.adapters.custom.BaseInputContainerAdapter;
import com.gcode.notes.data.ContentBase;
import com.gcode.notes.listeners.list.ListItemDeletedUndoOnClickListener;
import com.gcode.notes.listeners.main.NoteDeletedUndoOnClickListener;
import com.gcode.notes.ui.callbacks.DeleteNoteFromBinCallback;
import com.gcode.notes.ui.callbacks.DeleteNoteFromDisplayCallback;
import com.gcode.notes.ui.callbacks.EmptyRecyclerBinCallback;
import com.gcode.notes.ui.callbacks.PopUndoSnackbarCallback;
import com.gcode.notes.ui.callbacks.RestoreNoteFromDisplayCallback;
import com.gcode.notes.ui.snackbar.SnackbarHelper;

public class ActionExecutor {
    public static void popNoteDeletedSnackbar(View rootView, MainAdapter adapter,
                                              int position, ContentBase note) {

        NoteDeletedUndoOnClickListener undoOnClickListener = new NoteDeletedUndoOnClickListener(position, note);
        Snackbar.Callback snackbarCallback = new PopUndoSnackbarCallback(adapter, note, position, undoOnClickListener);
        SnackbarHelper.buildNoteDeletedSnackbar(rootView, undoOnClickListener, snackbarCallback).show();
    }

    public static void popListItemDeletedSnackbar(BaseInputContainerAdapter containerAdapter, View removedItem) {
        ListItemDeletedUndoOnClickListener undoOnClickListener =
                new ListItemDeletedUndoOnClickListener(containerAdapter, removedItem);
        SnackbarHelper.buildListItemDeletedSnackbar(containerAdapter.getRootScrollView(), undoOnClickListener).show();
    }

    public static void emptyRecyclerBin(Activity activity) {
        final MaterialDialog.ButtonCallback emptyRecyclerBinCallback = new EmptyRecyclerBinCallback();
        DialogHelper.buildEmptyBinDialog(activity, emptyRecyclerBinCallback);
    }

    public static void restoreDeletedNote(Activity activity, ContentBase note) {
        MaterialDialog.ButtonCallback restoreNoteFromDisplayCallback = new RestoreNoteFromDisplayCallback(activity, note);
        DialogHelper.buildRestoreNoteFromDisplayDialog(activity, restoreNoteFromDisplayCallback);
    }

    public static void deleteNoteFromDisplayBin(Activity activity, ContentBase note) {
        MaterialDialog.ButtonCallback deleteNoteFromDisplayCallback = new DeleteNoteFromDisplayCallback(activity, note);
        DialogHelper.buildDeleteNoteFromBinDialog(activity, deleteNoteFromDisplayCallback, true);
    }

    public static void deleteNoteFromBin(Activity activity, MainAdapter adapter, ContentBase note, int position) {
        MaterialDialog.ButtonCallback deleteNoteFromBinCallback = new DeleteNoteFromBinCallback(adapter, position, note);
        DialogHelper.buildDeleteNoteFromBinDialog(activity, deleteNoteFromBinCallback, false);
    }
}
