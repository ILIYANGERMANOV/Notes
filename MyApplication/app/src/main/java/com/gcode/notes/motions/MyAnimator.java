package com.gcode.notes.motions;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class MyAnimator {
    public static void startAnimationOnView(Context context, View view, int animationResourceId) {
        Animation animation = AnimationUtils.loadAnimation(context, animationResourceId);
        view.startAnimation(animation);
    }
}
