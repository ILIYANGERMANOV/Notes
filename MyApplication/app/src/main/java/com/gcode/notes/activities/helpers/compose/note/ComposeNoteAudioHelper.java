package com.gcode.notes.activities.helpers.compose.note;

import com.gcode.notes.activities.compose.ComposeNoteActivity;
import com.gcode.notes.extras.utils.AudioUtils;

public class ComposeNoteAudioHelper {
    public static void setupFromAudio(ComposeNoteActivity composeNoteActivity, String audioPath, String recognizedSpeechText) {
        composeNoteActivity.getDescriptionEditText().setText(recognizedSpeechText);
        setupAudio(composeNoteActivity, audioPath);
    }

    public static void setupAudio(ComposeNoteActivity composeNoteActivity, String audioPath) {
        composeNoteActivity.mAudioUtils = new AudioUtils(composeNoteActivity, audioPath,
                composeNoteActivity.getAudioDurationTextView(), composeNoteActivity.getAudioProgressBar(),
                composeNoteActivity.getAudioPlayPauseButton(), composeNoteActivity.getAudioLayout());

        composeNoteActivity.mAudioPath = audioPath;
    }
}
