package com.gcode.notes.ui;

import android.content.Context;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.R;

public class DialogHelper {
    public static void buildEmptyBinDialog(Context context, MaterialDialog.ButtonCallback buttonCallback) {
        new MaterialDialog.Builder(context)
                .title(R.string.empty_bin_dialog_title)
                .content(R.string.empty_bin_dialog_content)
                .positiveText(R.string.empty_bin_dialog_agree)
                .negativeText(R.string.dialog_cancel)
                .callback(buttonCallback)
                .show();
    }

    public static void buildDeleteNoteFromBinDialog(Context context, MaterialDialog.ButtonCallback buttonCallback,
                                                    boolean cancelable) {

        new MaterialDialog.Builder(context)
                .title(R.string.delete_note_bin_dialog_title)
                .positiveText(R.string.delete_note_bin_dialog_agree)
                .negativeText(R.string.dialog_cancel)
                .callback(buttonCallback)
                .cancelable(cancelable)
                .show();
    }

    public static void buildRestoreNoteFromDisplayDialog(Context context, MaterialDialog.ButtonCallback buttonCallback) {
        new MaterialDialog.Builder(context)
                .title(R.string.restore_note_dialog_title)
                .content(R.string.restore_note_dialog_content)
                .positiveText(R.string.restore_note_dialog_agree)
                .negativeText(R.string.dialog_cancel)
                .callback(buttonCallback)
                .show();
    }
}
