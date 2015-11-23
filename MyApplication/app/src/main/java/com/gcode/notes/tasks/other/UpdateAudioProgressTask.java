package com.gcode.notes.tasks.other;

import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.AudioUtils;

import java.util.TimerTask;

public class UpdateAudioProgressTask extends TimerTask {
    private AudioUtils mAudioUtils;
    private int mProgress;
    private float mDuration;

    public UpdateAudioProgressTask(AudioUtils audioUtils, float duration) {
        mAudioUtils = audioUtils;
        mDuration = duration;
    }

    @Override
    public void run() {
        mProgress = -1;
        if (mAudioUtils.isReady()) {
            try {
                float currentPosition = mAudioUtils.getCurrentPosition();
                if (mAudioUtils.isPlaying() && currentPosition < mDuration) {
                    mProgress = Math.round((currentPosition / mDuration) * 100f);
                } else {
                    //audio is completed or not playing, cancel task
                    cancel();
                    return;
                }
            } catch (IllegalStateException e) {
                //illegal state exception caught, cancel task
                MyDebugger.log("UpdateAudioProgressTask IllegalStateException", e.getMessage());
                cancel();
                return;
            }

            mAudioUtils.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mProgress > 0) {
                        //progress is legit, set new progress
                        mAudioUtils.setAudioProgress(mProgress);
                    }
                }
            });
        } else {
            //audioUtils aren't ready, cancel task
            cancel();
        }
    }
}
