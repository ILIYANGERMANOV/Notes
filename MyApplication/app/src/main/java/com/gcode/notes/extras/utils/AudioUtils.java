package com.gcode.notes.extras.utils;


import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gcode.notes.R;
import com.gcode.notes.extras.MyDebugger;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AudioUtils {
    //TODO: REFACTOR AND OPTIMIZE (very important)
    Activity mActivity;
    MediaPlayer mMediaPlayer;
    ProgressBar mProgressBar;
    ImageButton mPlayPauseButton;
    TextView mAudioDurationTextView;
    Timer mTimer;

    private boolean mIsReady;
    private String mAudioPath;
    private boolean mIsPlaying;

    public AudioUtils(Activity activity, String audioPath,
                      TextView audioDurationTextView, ProgressBar progressBar,
                      ImageButton playPauseButton) {
        mMediaPlayer = new MediaPlayer();
        mAudioPath = audioPath;
        mAudioDurationTextView = audioDurationTextView;
        try {
            //TODO: handle not existing file
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(audioPath);
            mMediaPlayer.prepareAsync();
            mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mIsReady = true;
                    mProgressBar.setProgress(0);
                    int duration = mp.getDuration();
                    mAudioDurationTextView.setText(
                            String.format("%02d:%02d",
                                    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                                    TimeUnit.MILLISECONDS.toSeconds(duration) -
                                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))
                    );
                }
            });
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mProgressBar.setProgress(0);
                    mPlayPauseButton.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                    mIsPlaying = false;
                }
            });
        } catch (IOException e) {
            MyDebugger.log("IOException while setting audio source", e.getMessage());
            mIsReady = false;
        }
        mPlayPauseButton = playPauseButton;
        mActivity = activity;
        mProgressBar = progressBar;
        mTimer = new Timer();
    }

    public boolean isPlaying() {
        return mIsPlaying;
    }

    public void playAudio() {
        if (mIsReady && !mMediaPlayer.isPlaying()) {
            mPlayPauseButton.setImageResource(R.drawable.ic_pause_circle_filled_black_24dp);
            mIsPlaying = true;
            mMediaPlayer.start();
            final float duration = (float) mMediaPlayer.getDuration();
            mTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (mIsPlaying && mMediaPlayer.getCurrentPosition() < duration) {
                                mProgressBar.setProgress(Math.round((mMediaPlayer.getCurrentPosition() / duration) * 100f));
                            } else {
                                cancel();
                            }
                        }
                    });
                }
            }, 0, 50);
        } else {
            rebuildPlayer();
        }
    }

    public void pauseAudio() {
        if (mIsReady && mMediaPlayer.isPlaying()) {
            mPlayPauseButton.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
            mIsPlaying = false;
            mMediaPlayer.pause();
            mTimer.purge();
        } else {
            rebuildPlayer();
        }
    }

    public void stopAudio() {
        if (mIsPlaying && mIsReady) {
            mIsPlaying = false;
            mMediaPlayer.stop();
        }
    }

    public void clearResources() {
        if (mMediaPlayer != null) {
            stopAudio();
            mMediaPlayer.release();
        }
        mIsReady = false;
    }

    private void rebuildPlayer() {
        if (!mIsReady) {
            MyDebugger.log("Player is not ready, rebuilding.");
            if (mMediaPlayer == null) {
                mMediaPlayer = new MediaPlayer();
                mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mIsReady = true;
                        mProgressBar.setProgress(0);
                        int duration = mp.getDuration();
                        mAudioDurationTextView.setText(
                                String.format("%02d:%02d",
                                        TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration)),
                                        TimeUnit.MILLISECONDS.toSeconds(duration) -
                                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)))
                        );
                    }
                });
                mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mProgressBar.setProgress(0);
                        mPlayPauseButton.setImageResource(R.drawable.ic_play_circle_filled_black_24dp);
                        mIsPlaying = false;
                    }
                });
            }
            try {
                mMediaPlayer.reset();
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(mAudioPath);
            } catch (IOException e) {
                MyDebugger.log("rebuildPlayer() IOException", e.getMessage());
                //TODO: handle exception
                mIsReady = false;
            }
            mMediaPlayer.prepareAsync();

        }
    }
}
