package com.gcode.notes.ui.callbacks;

import android.app.Activity;
import android.content.Intent;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.extras.utils.AudioUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.tasks.async.DeleteFileTask;
import com.gcode.notes.tasks.async.RemoveAttachedAudioTask;

public class DeleteAudioCallback extends MaterialDialog.ButtonCallback {
    ComposeNoteActivity mComposeNoteActivity;

    public DeleteAudioCallback(ComposeNoteActivity composeNoteActivity) {
        mComposeNoteActivity = composeNoteActivity;
    }

    @Override
    public void onPositive(MaterialDialog dialog) {
        AudioUtils audioUtils = mComposeNoteActivity.getAudioUtils();
        audioUtils.stopAudio();
        audioUtils.hideAudioLayout();
        new DeleteFileTask().execute(mComposeNoteActivity.getAudioPath()); //launch it before, setAudioPath()
        mComposeNoteActivity.setAudioPath(Constants.NO_AUDIO); //remove audio from compose activity, so saveNote() will work correctly
        int editNoteTargetId = mComposeNoteActivity.getEditNoteTargetId();
        if (editNoteTargetId != Constants.NO_VALUE) {
            //note opened in edit mode, so remove audio from db and set result for display activity
            //to secure if saveNote() isn't called
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
