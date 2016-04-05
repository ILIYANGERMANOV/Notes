package com.gcode.notes.ui.helpers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.text.format.DateFormat;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.MaterialDialog.SingleButtonCallback;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListAdapter;
import com.afollestad.materialdialogs.simplelist.MaterialSimpleListItem;
import com.gcode.notes.R;
import com.gcode.notes.extras.utils.AuthenticationUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.extras.values.Tags;
import com.gcode.notes.fragments.ComposeReminderFragment;
import com.gcode.notes.ui.callbacks.compose.AddPictureCallback;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;

public class DialogBuilder {
    //TODO: REFACTOR AND OPTIMIZE

    public static void buildEmptyBinDialog(Activity activity, SingleButtonCallback buttonCallback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.empty_bin_dialog_title)
                .content(R.string.empty_bin_dialog_content)
                .positiveText(R.string.empty_bin_dialog_agree)
                .negativeText(R.string.dialog_cancel)
                .onPositive(buttonCallback)
                .show();
    }

    public static void buildDeleteNotePermanentlyDialog(Activity activity, SingleButtonCallback buttonCallback,
                                                        boolean cancelable) {

        new MaterialDialog.Builder(activity)
                .title(R.string.delete_note_permanently_dialog_title)
                .positiveText(R.string.delete_note_permanently_dialog_agree)
                .negativeText(R.string.dialog_cancel)
                .onPositive(buttonCallback)
                .onNegative(buttonCallback)
                .cancelable(cancelable)
                .show();
    }

    public static void buildRestoreNoteFromDisplayDialog(Activity activity, SingleButtonCallback buttonCallback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.restore_note_dialog_title)
                .content(R.string.restore_note_dialog_content)
                .positiveText(R.string.restore_note_dialog_agree)
                .negativeText(R.string.dialog_cancel)
                .onPositive(buttonCallback)
                .show();
    }

    public static void buildAddPictureDialog(final Activity activity) {
        final MaterialSimpleListAdapter adapter = new MaterialSimpleListAdapter(activity);
        adapter.add(new MaterialSimpleListItem.Builder(activity)
                .content(R.string.add_picture_dialog_take_photo)
                .iconPaddingRes(R.dimen.add_pic_dialog_padding)
                .backgroundColorRes(R.color.color_accent_pink)
                .icon(R.drawable.ic_photo_camera_white_18dp)
                .build());
        adapter.add(new MaterialSimpleListItem.Builder(activity)
                .content(R.string.add_picture_dialog_choose_image)
                .iconPaddingRes(R.dimen.add_pic_dialog_padding)
                .backgroundColorRes(R.color.color_accent_pink)
                .icon(R.drawable.ic_insert_photo_white_18dp)
                .build());

        new MaterialDialog.Builder(activity)
                .title(R.string.add_picture_dialog_title)
                .items(R.array.add_picture_dialog_options)
                .adapter(adapter, new AddPictureCallback(activity, adapter))
                .show();
    }

    public static void buildRemovePhotoFromNoteDialog(Activity activity, SingleButtonCallback buttonCallback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.remove_photo_dialog_title)
                .positiveText(R.string.remove_photo_dialog_positive_text)
                .negativeText(R.string.dialog_cancel)
                .onPositive(buttonCallback)
                .show();
    }

    public static void buildDeleteAudioFromNoteDialog(Activity activity, SingleButtonCallback buttonCallback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.delete_audio_from_note_dialog_title)
                .content(R.string.delete_audio_from_note_dialog_content)
                .positiveText(R.string.delete_audio_from_note_dialog_positive_text)
                .negativeText(R.string.dialog_cancel)
                .onPositive(buttonCallback)
                .show();
    }

    public static void buildUnlockNoteDialog(Activity activity, SingleButtonCallback buttonCallback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.unlock_note_dialog_title)
                .content(R.string.unlock_note_dialog_content)
                .positiveText(R.string.unlock_note_dialog_positive_text)
                .negativeText(R.string.dialog_cancel)
                .onPositive(buttonCallback)
                .show();
    }

    public static void buildLockNoteDialog(Activity activity, SingleButtonCallback buttonCallback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.lock_note_dialog_title)
                .content(R.string.lock_note_dialog_content)
                .positiveText(R.string.lock_dialog_positive_text)
                .negativeText(R.string.dialog_cancel)
                .onPositive(buttonCallback)
                .show();
    }

    public static void buildDeleteNormalNoteDialog(Activity activity, SingleButtonCallback buttonCallback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.delete_normal_note_dialog_title)
                .content(R.string.delete_normal_note_dialog_content)
                .positiveText(R.string.delete_normal_note_dialog_positive_text)
                .negativeText(R.string.dialog_cancel)
                .onPositive(buttonCallback)
                .show();
    }

    public static void buildDeletePrivateNoteFromDisplayDialog(Activity activity, SingleButtonCallback buttonCallback) {
        new MaterialDialog.Builder(activity)
                .title(R.string.delete_private_note_dialog_title)
                .content(R.string.delete_private_note_dialog_content)
                .positiveText(R.string.delete_private_note_dialog_positive_text)
                .negativeText(R.string.dialog_cancel)
                .onPositive(buttonCallback)
                .show();
    }


    public static MaterialDialog buildOpenImageProgressDialog(Activity activity) {
        return new MaterialDialog.Builder(activity)
                .title(R.string.open_image_progress_dialog_title)
                .content(R.string.open_image_progress_dialog_content)
                .progress(true, 0)
                .cancelable(false)
                .build();
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

    public static void buildCreatePasswordDialog(final AuthenticationUtils authenticationUtils) {
        new MaterialDialog.Builder(authenticationUtils.getActivity())
                .title("Setup password 1/2")
                .content("Choose password for private notes.")
                .inputRangeRes(Constants.PASS_MIN_LENGTH, Constants.PASS_MAX_LENGTH, R.color.not_valid_red)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input("Password", null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        authenticationUtils.onCreatePassword(input.toString());
                    }
                })
                .cancelable(false)
                .positiveText("Submit")
                .negativeText("Exit private")
                .onNegative(new SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        authenticationUtils.getCallbacks().onExitPrivate();
                    }
                })
                .show();
    }

    public static void buildConfirmPasswordDialog(final AuthenticationUtils authenticationUtils) {
        new MaterialDialog.Builder(authenticationUtils.getActivity())
                .title("Setup password 2/2")
                .content("Confirm password.")
                .inputRangeRes(Constants.PASS_MIN_LENGTH, Constants.PASS_MAX_LENGTH, R.color.not_valid_red)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input("Confirm password", null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        authenticationUtils.onConfirmPassword(input.toString());
                    }
                })
                .cancelable(false)
                .positiveText("Confirm")
                .negativeText("Previous")
                .onNegative(new SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.cancel();
                        authenticationUtils.authenticate();
                    }
                })
                .show();
    }

    public static void buildEnterPasswordDialog(final AuthenticationUtils authenticationUtils, String content) {
        new MaterialDialog.Builder(authenticationUtils.getActivity())
                .title("Enter password")
                .content(content)
                .inputRangeRes(Constants.PASS_MIN_LENGTH, Constants.PASS_MAX_LENGTH, R.color.not_valid_red)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input("Password", null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        authenticationUtils.onEnterPassword(input.toString());
                    }
                })
                .cancelable(false)
                .positiveText("Enter")
                .negativeText("Exit private")
                .onNegative(new SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        authenticationUtils.getCallbacks().onExitPrivate();
                    }
                })
                .show();
    }
}
