package com.gcode.notes.ui.helpers;

import android.app.Activity;
import android.text.format.DateFormat;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.gcode.notes.R;
import com.gcode.notes.extras.values.Tags;
import com.gcode.notes.fragments.ComposeReminderFragment;
import com.gcode.notes.ui.callbacks.AddPictureListCallback;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

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

    public static void buildRemovePhotoFromNoteDialog(Activity activity, MaterialDialog.ButtonCallback buttonCallback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.remove_photo_dialog_title)
                .positiveText(R.string.remove_photo_dialog_positive_text)
                .negativeText(R.string.remove_photo_dialog_negative_text)
                .callback(buttonCallback)
                .show();
    }

    public static void buildDeleteAudioFromNoteDialog(Activity activity, MaterialDialog.ButtonCallback buttonCallback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.delete_audio_from_note_dialog_title)
                .content(R.string.delete_audio_from_note_dialog_content)
                .positiveText(R.string.delete_audio_from_note_dialog_positive_text)
                .negativeText(R.string.delete_audio_from_note_negative_text)
                .callback(buttonCallback)
                .show();
    }

    public static MaterialDialog buildOpenImageProgressDialog(Activity activity) {
        return new MaterialDialog.Builder(activity)
                .title(R.string.open_image_progress_dialog_title)
                .content(R.string.open_image_progress_dialog_content)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    public static MaterialDialog buildEncryptNoteProgressDialog(Activity activity) {
        return new MaterialDialog.Builder(activity)
                .title(R.string.encrypt_progress_dialog_title)
                .content(R.string.encrypt_progress_dialog_content)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    public static MaterialDialog buildDecryptNoteProgressDialog(Activity activity) {
        return new MaterialDialog.Builder(activity)
                .title(R.string.decrypt_progress_dialog_title)
                .content(R.string.decrypt_progress_dialog_content)
                .progress(true, 0)
                .cancelable(false)
                .show();
    }

    public static MaterialDialog buildDecryptAllNotesProgressDialog(Activity activity) {
        return new MaterialDialog.Builder(activity)
                .title(R.string.decrypt_all_notes_progress_dialog_title)
                .content(R.string.decrypt_progress_dialog_content)
                .progress(false, 100)
                .cancelable(false)
                .show();
    }

    public static void buildDatePickerDialog(ComposeReminderFragment composeReminderFragment) {
        DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(
                composeReminderFragment.mDatePickerOnDateSetListener,
                composeReminderFragment.mYear,
                composeReminderFragment.mMonthOfYear,
                composeReminderFragment.mDayOfMonth
        );
        datePickerDialog.setMinDate(Calendar.getInstance());
        datePickerDialog.show(composeReminderFragment.getActivity().getFragmentManager(), Tags.DATE_PICKER_DIALOG_TAG);
    }

    public static void buildTimePickerDialog(ComposeReminderFragment composeReminderFragment) {
        TimePickerDialog.newInstance(
                composeReminderFragment.mTimePickerOnTimeSetListener,
                composeReminderFragment.mHour,
                composeReminderFragment.mMinute,
                DateFormat.is24HourFormat(composeReminderFragment.getContext()) //is24HourMode
        ).show(composeReminderFragment.getActivity().getFragmentManager(), Tags.TIME_PICKER_DIALOG_TAG);
    }
}
