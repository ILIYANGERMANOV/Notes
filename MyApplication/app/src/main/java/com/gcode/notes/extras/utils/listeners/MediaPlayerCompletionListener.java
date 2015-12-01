package com.gcode.notes.extras.utils.listeners;

import android.media.MediaPlayer;

import com.gcode.notes.R;
import com.gcode.notes.extras.utils.AudioUtils;

public class MediaPlayerCompletionListener implements MediaPlayer.OnCompletionListener {
    AudioUtils mAudioUtils;

    public MediaPlayerCompletionListener(AudioUtils audioUtils) {
        mAudioUtils = audioUtils;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mAudioUtils.setAudioProgress(0);
        mAudioUtils.setPlayPauseButtonImageResource(R.drawable.ic_play_circle_filled_black_24dp);
    }
}
