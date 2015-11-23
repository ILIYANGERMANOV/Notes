package com.gcode.notes.extras.utils;


import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.listeners.utils.MediaPlayerCompletionListener;
import com.gcode.notes.listeners.utils.MediaPlayerPreparedListener;
import com.gcode.notes.tasks.other.UpdateAudioProgressTask;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AudioUtils {
    //class members initialized with values from outside
    private Activity mActivity;
    private LinearLayout mAudioLayout;
    private ProgressBar mProgressBar;
    private ImageButton mPlayPauseButton;
    private TextView mAudioDurationTextView;
    private String mAudioPath;

    //members initialized in AudioUtils
    private MediaPlayer mMediaPlayer;
    private Timer mTimer;
    private boolean mIsReady = false;
    private int mTriesToResolve = 0;

    public AudioUtils(Activity activity, String audioPath,
                      TextView audioDurationTextView, ProgressBar progressBar,
                      ImageButton playPauseButton, LinearLayout audioLayout) {

        mAudioPath = audioPath;
        mAudioLayout = audioLayout;
        mAudioDurationTextView = audioDurationTextView;
        mProgressBar = progressBar;
        mPlayPauseButton = playPauseButton;
        buildPlayer(); //building player, uses above class members, so cannot be called before them
        mActivity = activity;
        mTimer = new Timer(); //timer used by audioUtils to schedule update audio progress and rebuild tasks
    }

    public boolean isPlaying() {
        return mIsReady && mMediaPlayer != null && mMediaPlayer.isPlaying();
    }

    public void playAudio() {
        if (mIsReady && mMediaPlayer != null) {
            try {
                if (!mMediaPlayer.isPlaying()) {
                    setPlayPauseButtonImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
                    mMediaPlayer.start();
                    //update audio progress

                    mTimer.scheduleAtFixedRate(new UpdateAudioProgressTask(this, getDuration()), 0, Constants.MINIMUM_DELAY);
                }
            } catch (IllegalStateException e) {
                //this exception is almost impossible to be thrown, just to secure from app crash
                MyDebugger.log("pauseAudio() IllegalStateException caught", e.getMessage());
            }
        } else {
            //player not ready or null, try to rebuild it
            scheduleRebuild();
        }
    }

    public void pauseAudio() {
        if (mIsReady && mMediaPlayer != null) {
            try {
                if (mMediaPlayer.isPlaying()) {
                    setPlayPauseButtonImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                    mMediaPlayer.pause();
                    mTimer.purge();
                }
            } catch (IllegalStateException e) {
                //this exception is almost impossible to be thrown, just to secure from app crash
                MyDebugger.log("pauseAudio() IllegalStateException caught", e.getMessage());
            }
        } else {
            //player not ready or null, try to rebuild it
            scheduleRebuild();
        }
    }

    public void stopAudio() {
        if (mIsReady && mMediaPlayer != null) {
            try {
                if (mMediaPlayer.isPlaying()) {
                    //stop player if it is playing
                    setPlayPauseButtonImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                    mMediaPlayer.stop();
                    mTimer.purge();
                }
            } catch (IllegalStateException e) {
                //this exception is almost impossible to be thrown, just to secure from app crash
                MyDebugger.log("pauseAudio() IllegalStateException caught", e.getMessage());
            }
        } else {
            //player not ready or null, it won't be used more so no need to rebuild
            MyDebugger.log("stopAudio() player is not ready or null");
        }
    }

    public void clearResources() {
        stopAudio();
        mIsReady = false; //set after stopAudio() in order it to work
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void showAudioLayout() {
        mAudioLayout.setVisibility(View.VISIBLE);
    }

    public void hideAudioLayout() {
        mAudioLayout.setVisibility(View.GONE);
    }

    public TextView getAudioDurationTextView() {
        return mAudioDurationTextView;
    }

    public void setTriesToResolve(int triesToResolve) {
        mTriesToResolve = triesToResolve;
    }

    public void setAudioProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    public void setReady(boolean mIsReady) {
        this.mIsReady = mIsReady;
    }

    public boolean isReady() {
        return mMediaPlayer != null && mIsReady;
    }

    public void setPlayPauseButtonImageResource(int resourceId) {
        mPlayPauseButton.setImageResource(resourceId);
    }

    public float getDuration() {
        return mMediaPlayer.getDuration();
    }

    public float getCurrentPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    private void scheduleRebuild() {
        MyDebugger.log("Player is not ready or null, try to rebuild it");
        mTriesToResolve++;
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                buildPlayer();
            }
        }, Constants.MEDIUM_DELAY);
    }

    private void buildPlayer() {
        if (FileUtils.fileExists(mAudioPath) && mTriesToResolve < Constants.MAX_TRIES_TO_RESOLVE) {
            if (mMediaPlayer == null) {
                //player isn't instantiated, create it
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setOnPreparedListener(new MediaPlayerPreparedListener(this));
                mMediaPlayer.setOnCompletionListener(new MediaPlayerCompletionListener(this));
            } else {
                //player is already create, reset its state and try to rebuild
                try {
                    mMediaPlayer.reset();
                } catch (IllegalStateException e) {
                    MyDebugger.log("buildPlayer() player.reset() IllegalStateException", e.getMessage());
                    scheduleRebuild();
                    return;
                }
            }
            preparePlayer();
        } else {
            //audio file doesn't exists or max tries to resolve reached, hide audio layout
            MyDebugger.log("Audio file doesn't exists or max tries to resolve reached", mTriesToResolve);
            hideAudioLayout();
            //TODO: Set error layout instead of hiding audio layout
        }
    }

    private void preparePlayer() {
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            try {
                mMediaPlayer.setDataSource(mAudioPath);
            } catch (IOException e) {
                //audioUtils can't be ready here, so don't set mIsReady to false
                //try to rebuild player
                MyDebugger.log("IOException setDataSource()", e.getMessage());
                hideAudioLayout();
                scheduleRebuild();
                return;
            }
            mMediaPlayer.prepareAsync();
        } catch (IllegalStateException e) {
            MyDebugger.log("Illegal state exception while preparing player", e.getMessage());
            mIsReady = false;
            //try to buildPlayer again
            scheduleRebuild();
        }
    }
}
