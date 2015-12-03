package com.gcode.notes.activities.helpers.compose.note;

import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.data.main.NoteData;
import com.gcode.notes.extras.utils.AudioUtils;

public class ComposeNoteAudioHelper {
    public static void setupFromAudio(ComposeNoteActivity composeNoteActivity, String audioPath, String recognizedSpeechText) {
        composeNoteActivity.mNoteData = new NoteData();
        composeNoteActivity.mNoteData.setAttachedAudioPath(audioPath);
        //description is not set cuz, it is most likely to change so set in saveNote()
        composeNoteActivity.getDescriptionEditText().setText(recognizedSpeechText);
        setupAudio(composeNoteActivity, audioPath);
    }

    public static void setupAudio(ComposeNoteActivity composeNoteActivity, String audioPath) {
        composeNoteActivity.mAudioUtils = new AudioUtils(composeNoteActivity, audioPath,
                composeNoteActivity.getAudioDurationTextView(), composeNoteActivity.getAudioProgressBar(),
                composeNoteActivity.getAudioPlayPauseButton(), composeNoteActivity.getAudioLayout());

        composeNoteActivity.mNoteData.setAttachedAudioPath(audioPath);
    }
}
