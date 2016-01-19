package com.gcode.notes.ui.callbacks.compose;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.data.NoteData;
import com.gcode.notes.extras.utils.AudioUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.tasks.async.DeleteFileTask;
import com.gcode.notes.tasks.async.compose.RemoveAttachedAudioTask;

public class DeleteAudioCallback implements MaterialDialog.SingleButtonCallback {
    ComposeNoteActivity mComposeNoteActivity;

    public DeleteAudioCallback(ComposeNoteActivity composeNoteActivity) {
        mComposeNoteActivity = composeNoteActivity;
    }
    @Override
    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
        AudioUtils audioUtils = mComposeNoteActivity.mAudioUtils;
        audioUtils.stopAudio();
        audioUtils.hideAudioLayout();
        NoteData mNoteData = mComposeNoteActivity.mNoteData;
        new DeleteFileTask().execute(mNoteData.getAttachedAudioPath()); //launch it before, setting attached audio path to NO_AUDIO
        mNoteData.setAttachedAudioPath(null); //remove audio from note, so saveNote() will work correctly
        int editNoteTargetId = mNoteData.getTargetId();
        if (editNoteTargetId != Constants.NO_VALUE) {
            //note opened in edit mode, so remove audio from db and set result for display activity
            //to secure if saveNote() isn't called
            new RemoveAttachedAudioTask().execute(editNoteTargetId);
            mComposeNoteActivity.mResultIntent.putExtra(Constants.EXTRA_DELETED_AUDIO, true);
            mComposeNoteActivity.setResult(Activity.RESULT_OK, mComposeNoteActivity.mResultIntent); //set result if here not isn't saved so EXTRA_AUDIO_DELETED will be used
        }
    }
}
