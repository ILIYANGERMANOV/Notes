package com.gcode.notes.listeners.utils;

import android.media.MediaPlayer;

import com.gcode.notes.extras.utils.AudioUtils;

import java.util.concurrent.TimeUnit;

public class MediaPlayerPreparedListener implements MediaPlayer.OnPreparedListener {
    AudioUtils mAudioUtils;

    public MediaPlayerPreparedListener(AudioUtils audioUtils) {
        mAudioUtils = audioUtils;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mAudioUtils.setReady(true);
        mAudioUtils.setTriesToResolve(0);
        mAudioUtils.setAudioProgress(0);
        mAudioUtils.getAudioDurationTextView().setText(convertMillisToMinutesAndSeconds(mp.getDuration()));
        mAudioUtils.showAudioLayout();
    }

    private String convertMillisToMinutesAndSeconds(int millis) {
        return String.format("%02d:%02d",
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), //minutes
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)) //seconds
        );

    }
}
