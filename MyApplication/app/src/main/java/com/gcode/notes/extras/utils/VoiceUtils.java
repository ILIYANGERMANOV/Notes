package com.gcode.notes.extras.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;

import com.gcode.notes.R;
import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.extras.values.Constants;

import java.util.Locale;

public class VoiceUtils {
    public static void promptSpeechInput(Activity activity) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().getLanguage());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, activity.getString(R.string.speech_prompt));

        //get audio
        intent.putExtra("android.speech.extra.GET_AUDIO_FORMAT", "audio/AMR");
        intent.putExtra("android.speech.extra.GET_AUDIO", true);
        try {
            activity.startActivityForResult(intent, Constants.SPEECH_INPUT_REQ_CODE);
        } catch (ActivityNotFoundException a) {
            MyLogger.toast(activity, activity.getString(R.string.speech_not_supported));
        }
    }
}
