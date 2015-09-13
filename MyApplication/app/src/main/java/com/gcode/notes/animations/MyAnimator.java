package com.gcode.notes.animations;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MyAnimator {
    public static void startAnimation(Context context, View view, int animationResourceId) {
        Animation animation = AnimationUtils.loadAnimation(context, animationResourceId);
        view.startAnimation(animation);
    }
}
