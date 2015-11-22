package com.gcode.notes.ui.callbacks;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.tasks.DeleteFileTask;
import com.gcode.notes.tasks.RemoveAttachedAudioTask;

public class DeleteAudioCallback extends MaterialDialog.ButtonCallback {
    //TODO: test and fix possible bugs, refactor (care setting to NO_AUDIO, then using audioPath)
    ComposeNoteActivity mComposeNoteActivity;
    String mAudioPath;

    public DeleteAudioCallback(ComposeNoteActivity composeNoteActivity, String audioPath) {
        mComposeNoteActivity = composeNoteActivity;
        mAudioPath = audioPath;
    }

    @Override
    public void onPositive(MaterialDialog dialog) {
        new DeleteFileTask().execute(mAudioPath);
        mComposeNoteActivity.getAudioLayout().setVisibility(View.GONE);
        mComposeNoteActivity.setAudioPath(Constants.NO_AUDIO);
        mComposeNoteActivity.getAudioUtils().stopAudio();
        int editNoteTargetId = mComposeNoteActivity.getEditNoteTargetId();
        if (editNoteTargetId != Constants.ERROR) {
            new RemoveAttachedAudioTask().execute(editNoteTargetId);
            Intent resultIntent = new Intent();
            resultIntent.putExtra(Constants.EXTRA_DELETED_AUDIO, true);
            mComposeNoteActivity.setResult(Activity.RESULT_OK, resultIntent);
        }
        dialog.cancel();
    }

    @Override
    public void onNegative(MaterialDialog dialog) {
        dialog.cancel();
    }
}
