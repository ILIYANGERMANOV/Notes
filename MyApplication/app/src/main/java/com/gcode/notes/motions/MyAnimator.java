package com.gcode.notes.motions;

import android.view.View;
import android.view.animation.AnimationUtils;

public class MyAnimator {
    public static void startAnimationOnView(View view, int animationResourceId) {
        view.startAnimation(AnimationUtils.loadAnimation(view.getContext(), animationResourceId));
    }
}
