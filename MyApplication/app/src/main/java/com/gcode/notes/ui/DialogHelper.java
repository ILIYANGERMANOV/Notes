package com.gcode.notes.ui;

import android.app.Activity;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.gcode.notes.R;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.ui.callbacks.AddPictureListCallback;

public class DialogHelper {
    public static void buildEmptyBinDialog(Activity activity, MaterialDialog.ButtonCallback buttonCallback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.empty_bin_dialog_title)
                .content(R.string.empty_bin_dialog_content)
                .positiveText(R.string.empty_bin_dialog_agree)
                .negativeText(R.string.dialog_cancel)
                .callback(buttonCallback)
                .show();
    }

    public static void buildDeleteNoteFromBinDialog(Activity activity, MaterialDialog.ButtonCallback buttonCallback,
                                                    boolean cancelable) {

        new MaterialDialog.Builder(activity)
                .title(R.string.delete_note_bin_dialog_title)
                .positiveText(R.string.delete_note_bin_dialog_agree)
                .negativeText(R.string.dialog_cancel)
                .callback(buttonCallback)
                .cancelable(cancelable)
                .show();
    }

    public static void buildRestoreNoteFromDisplayDialog(Activity activity, MaterialDialog.ButtonCallback buttonCallback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.restore_note_dialog_title)
                .content(R.string.restore_note_dialog_content)
                .positiveText(R.string.restore_note_dialog_agree)
                .negativeText(R.string.dialog_cancel)
                .callback(buttonCallback)
                .show();
    }

    public static void buildAddPictureDialog(final Activity activity) {
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(activity);
        adapter.add(new MaterialSimpleListItem.Builder(activity)
                .content(R.string.add_picture_dialog_take_photo)
                .icon(R.drawable.ic_photo_camera_black_18dp)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(activity)
                .content(R.string.add_picture_dialog_choose_image)
                .icon(R.drawable.ic_photo_black_18dp)
                .build());

        new MaterialDialog.Builder(activity)
                .title(R.string.add_picture_dialog_title)
                .items(R.array.add_picture_dialog_options)
                .adapter(adapter, new AddPictureListCallback(activity, adapter))
                .show();
    }
}
