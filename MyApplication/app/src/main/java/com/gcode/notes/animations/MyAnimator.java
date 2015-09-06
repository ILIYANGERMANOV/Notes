package com.gcode.notes.animations;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.gcode.notes.R;

public class MyAnimator {
    public static void startAnimation(Context context, View view, int animationResourceId) {
        Animation animation = AnimationUtils.loadAnimation(context, animationResourceId);
        view.startAnimation(animation);
    }
}
