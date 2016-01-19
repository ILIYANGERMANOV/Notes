package com.gcode.notes.ui;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.activities.display.DisplayBaseActivity;
import com.gcode.notes.adapters.MainAdapter;
import com.gcode.notes.adapters.list.compose.BaseComposeContainerAdapter;
import com.gcode.notes.adapters.list.compose.listeners.ListItemDeletedUndoOnClickListener;
import com.gcode.notes.adapters.note.compose.ComposeNoteImagesAdapter;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.ui.callbacks.NoteDeletedSnackbarCallback;
import com.gcode.notes.ui.callbacks.bin.DeleteNoteFromDisplayCallback;
import com.gcode.notes.ui.callbacks.bin.DeleteNotePermanentlyCallback;
import com.gcode.notes.ui.callbacks.bin.EmptyRecyclerBinCallback;
import com.gcode.notes.ui.callbacks.bin.RestoreNoteFromDisplayCallback;
import com.gcode.notes.ui.callbacks.compose.DeleteAudioCallback;
import com.gcode.notes.ui.callbacks.compose.RemovePhotoCallback;
import com.gcode.notes.ui.callbacks.display.UnlockNoteCallback;
import com.gcode.notes.ui.helpers.DialogBuilder;
import com.gcode.notes.ui.helpers.SnackbarHelper;
import com.gcode.notes.ui.listeners.NoteDeletedUndoOnClickListener;

public class ActionExecutor {
    //TODO: REFACTOR AND OPTIMIZE

    public static void popNoteDeletedSnackbar(View rootView, MainAdapter adapter,
                                              int position, ContentBase note) {

        NoteDeletedUndoOnClickListener undoOnClickListener = new NoteDeletedUndoOnClickListener(position, note);
        Snackbar.Callback snackbarCallback = new NoteDeletedSnackbarCallback(adapter, note, position, undoOnClickListener);
        SnackbarHelper.buildNoteDeletedSnackbar(rootView, undoOnClickListener, snackbarCallback).show();
    }

    public static void popListItemDeletedSnackbar(BaseComposeContainerAdapter containerAdapter, View removedItem) {
        ListItemDeletedUndoOnClickListener undoOnClickListener =
                new ListItemDeletedUndoOnClickListener(containerAdapter, removedItem);
        SnackbarHelper.buildListItemDeletedSnackbar(containerAdapter.getRootScrollView(), undoOnClickListener).show();
    }

    public static void emptyRecyclerBin(Activity activity) {
        SingleButtonCallback emptyRecyclerBinCallback = new EmptyRecyclerBinCallback();
        DialogBuilder.buildEmptyBinDialog(activity, emptyRecyclerBinCallback);
    }

    public static void restoreDeletedNote(Activity activity, ContentBase note) {
        SingleButtonCallback restoreNoteFromDisplayCallback = new RestoreNoteFromDisplayCallback(activity, note);
        DialogBuilder.buildRestoreNoteFromDisplayDialog(activity, restoreNoteFromDisplayCallback);
    }

    public static void deleteNoteFromDisplayBin(Activity activity, ContentBase note) {
        SingleButtonCallback deleteNoteFromDisplayCallback = new DeleteNoteFromDisplayCallback(activity, note);
        DialogBuilder.buildDeleteNotePermanentlyDialog(activity, deleteNoteFromDisplayCallback, true);
    }

    public static void deleteNotePermanently(Activity activity, MainAdapter adapter,
                                             ContentBase note, int position) {
        SingleButtonCallback deleteNotePermanentlyCallback =
                new DeleteNotePermanentlyCallback(adapter, position, note);
        DialogBuilder.buildDeleteNotePermanentlyDialog(activity, deleteNotePermanentlyCallback, false);
    }

    public static void addPhotoToNote(Activity activity) {
        DialogBuilder.buildAddPictureDialog(activity);
    }

    public static void removePhotoFromNote(Activity activity, ComposeNoteImagesAdapter adapter, String item) {
        RemovePhotoCallback removePhotoCallback = new RemovePhotoCallback(adapter, item);
        DialogBuilder.buildRemovePhotoFromNoteDialog(activity, removePhotoCallback);
    }

    public static void deleteAudioFromNote(ComposeNoteActivity composeNoteActivity) {
        DeleteAudioCallback deleteAudioCallback = new DeleteAudioCallback(composeNoteActivity);
        DialogBuilder.buildDeleteAudioFromNoteDialog(composeNoteActivity, deleteAudioCallback);
    }

    public static void unlockNote(DisplayBaseActivity displayBaseActivity, ContentBase contentBase) {
        UnlockNoteCallback unlockNoteCallback = new UnlockNoteCallback(displayBaseActivity, contentBase);
        DialogBuilder.buildUnlockNoteDialog(displayBaseActivity, unlockNoteCallback);
    }
}
