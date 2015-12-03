package com.gcode.notes.ui.callbacks;

import android.app.Activity;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.data.main.NoteData;
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
        AudioUtils audioUtils = mComposeNoteActivity.mAudioUtils;
        audioUtils.stopAudio();
        audioUtils.hideAudioLayout();
        NoteData mNoteData = mComposeNoteActivity.mNoteData;
        new DeleteFileTask().execute(mNoteData.getAttachedAudioPath()); //launch it before, setting attached audio path to NO_AUDIO
        mNoteData.setAttachedAudioPath(Constants.NO_AUDIO); //remove audio from note, so saveNote() will work correctly
        int editNoteTargetId = mNoteData.getTargetId();
        if (editNoteTargetId != Constants.NO_VALUE) {
            //note opened in edit mode, so remove audio from db and set result for display activity
            //to secure if saveNote() isn't called
            new RemoveAttachedAudioTask().execute(editNoteTargetId);
            mComposeNoteActivity.mResultIntent.putExtra(Constants.EXTRA_DELETED_AUDIO, true);
            mComposeNoteActivity.setResult(Activity.RESULT_OK, mComposeNoteActivity.mResultIntent); //set result if here not isn't saved so EXTRA_AUDIO_DELETED will be used
        }
        dialog.cancel();
    }

    @Override
    public void onNegative(MaterialDialog dialog) {
        dialog.cancel();
    }
}
