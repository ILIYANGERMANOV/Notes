package com.gcode.notes.extras.builders;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.speech.RecognizerIntent;

import com.gcode.notes.activities.compose.note.ComposeNoteActivity;
import com.gcode.notes.extras.values.Constants;

public class IntentBuilder {
    public static Intent buildStartComposeFromPhotoIntent(Activity activity, Uri photoUri) {
        return buildStartComposeFromPhotoIntent(activity, photoUri.toString());
    }

    public static Intent buildStartComposeFromPhotoIntent(Activity activity, String photoPath) {
        Intent intent = new Intent(activity, ComposeNoteActivity.class);
        intent.putExtra(Constants.EXTRA_PHOTO_URI, photoPath);
        intent.putExtra(Constants.EXTRA_SETUP_FROM, Constants.SETUP_FROM_PHOTO);
        return intent;
    }

    public static Intent buildStartComposeFromAudioIntent(Activity activity, Intent data, String audioFilePath) {
        Intent intent = new Intent(activity, ComposeNoteActivity.class);
        intent.putExtra(Constants.EXTRA_SETUP_FROM, Constants.SETUP_FROM_AUDIO);
        intent.putExtra(Constants.EXTRA_AUDIO_PATH, audioFilePath);
        intent.putExtra(Constants.EXTRA_RECOGNIZED_SPEECH_TEXT,
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
        return intent;
    }
}
