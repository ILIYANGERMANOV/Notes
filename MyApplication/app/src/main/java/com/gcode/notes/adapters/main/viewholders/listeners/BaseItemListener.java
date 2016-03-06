package com.gcode.notes.adapters.main.viewholders.listeners;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gcode.notes.R;
import com.gcode.notes.motions.MyAnimator;

public class BaseItemListener implements View.OnClickListener {
    public static boolean mIsAnimating = false;
    Activity mActivity;
    boolean mDisabled;

    public BaseItemListener(Activity activity) {
        mActivity = activity;
        mDisabled = false;
    }

    public void setDisabled(boolean disabled) {
        mDisabled = disabled;
    }

    public void performAnimationAndStartActivity(final View itemView, final Intent intent, final int requestCode) {
        Animation animation = AnimationUtils.loadAnimation(mActivity, R.anim.hyper_jump);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mIsAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                MyAnimator.startAnimationOnView(mActivity, itemView, R.anim.fade_out);
                mActivity.startActivityForResult(intent, requestCode);
                mIsAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        itemView.startAnimation(animation);
    }

    @Override
    public void onClick(View v) {

    }
}
