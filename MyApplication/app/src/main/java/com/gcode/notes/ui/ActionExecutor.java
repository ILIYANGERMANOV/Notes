package com.gcode.notes.ui;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.activities.display.DisplayBaseActivity;
import com.gcode.notes.adapters.list.compose.BaseComposeContainerAdapter;
import com.gcode.notes.adapters.list.compose.listeners.ListItemDeletedUndoOnClickListener;
import com.gcode.notes.adapters.note.compose.ComposeNoteImagesAdapter;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.ui.callbacks.bin.DeleteNoteFromDisplayBinCallback;
import com.gcode.notes.ui.callbacks.bin.DeleteNotePermanentlyCallback;
import com.gcode.notes.ui.callbacks.bin.EmptyRecyclerBinCallback;
import com.gcode.notes.ui.callbacks.bin.RestoreNoteFromDisplayCallback;
import com.gcode.notes.ui.callbacks.compose.DeleteAudioCallback;
import com.gcode.notes.ui.callbacks.compose.RemovePhotoCallback;
import com.gcode.notes.ui.callbacks.display.DeleteNormalNoteCallback;
import com.gcode.notes.ui.callbacks.display.DeletePrivateNoteFromDisplayCallback;
import com.gcode.notes.ui.callbacks.display.LockNoteCallback;
import com.gcode.notes.ui.callbacks.display.UnlockNoteCallback;
import com.gcode.notes.ui.callbacks.main.NoteDeletedSnackbarCallback;
import com.gcode.notes.ui.callbacks.main.UndoOnClickListener;
import com.gcode.notes.ui.helpers.DialogBuilder;
import com.gcode.notes.ui.helpers.SnackbarHelper;

public class ActionExecutor {
    //TODO: REFACTOR AND OPTIMIZE

    public static void popNoteDeletedSnackbar(MainActivity mainActivity, int position, ContentBase note) {

        UndoOnClickListener undoOnClickListener =
                new UndoOnClickListener(position, note);
        Snackbar.Callback snackbarCallback =
                new NoteDeletedSnackbarCallback(mainActivity, note, position, undoOnClickListener);

        Snackbar snackbar = SnackbarHelper.buildNoteDeletedSnackbar(mainActivity.getCoordinatorLayout(),
                undoOnClickListener, snackbarCallback);
        snackbar.show();
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

    public static void restoreDeletedNote(DisplayBaseActivity displayBaseActivity, ContentBase note) {
        SingleButtonCallback restoreNoteFromDisplayCallback =
                new RestoreNoteFromDisplayCallback(displayBaseActivity, note);
        DialogBuilder.buildRestoreNoteFromDisplayDialog(displayBaseActivity, restoreNoteFromDisplayCallback);
    }

    public static void deleteNoteFromDisplayBin(DisplayBaseActivity activity, ContentBase note) {
        SingleButtonCallback deleteNoteFromDisplayBinCallback = new DeleteNoteFromDisplayBinCallback(activity, note);
        DialogBuilder.buildDeleteNotePermanentlyDialog(activity, deleteNoteFromDisplayBinCallback, true);
    }

    public static void deleteNotePermanently(MainActivity mainActivity,
                                             ContentBase note, int position) {
        SingleButtonCallback deleteNotePermanentlyCallback =
                new DeleteNotePermanentlyCallback(mainActivity, position, note);
        DialogBuilder.buildDeleteNotePermanentlyDialog(mainActivity, deleteNotePermanentlyCallback, false);
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

    public static void lockNote(DisplayBaseActivity displayBaseActivity, ContentBase contentBase) {
        LockNoteCallback lockNoteCallback = new LockNoteCallback(displayBaseActivity, contentBase);
        DialogBuilder.buildLockNoteDialog(displayBaseActivity, lockNoteCallback);
    }

    public static void deleteNormalNote(DisplayBaseActivity displayBaseActivity, ContentBase contentBase) {
        DeleteNormalNoteCallback deleteNormalNoteCallback =
                new DeleteNormalNoteCallback(displayBaseActivity, contentBase);
        DialogBuilder.buildDeleteNormalNoteDialog(displayBaseActivity, deleteNormalNoteCallback);
    }

    public static void deletePrivateNoteFromDisplay(DisplayBaseActivity displayBaseActivity, ContentBase contentBase) {
        DeletePrivateNoteFromDisplayCallback deletePrivateNoteFromDisplayCallback =
                new DeletePrivateNoteFromDisplayCallback(displayBaseActivity, contentBase);
        DialogBuilder.buildDeletePrivateNoteFromDisplayDialog(displayBaseActivity, deletePrivateNoteFromDisplayCallback);
    }
}
