package com.gcode.notes.motions;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.github.clans.fab.FloatingActionMenu;

public class MyAnimator {
    public static void startAnimationOnView(Context context, View view, int animationResourceId) {
        Animation animation = AnimationUtils.loadAnimation(context, animationResourceId);
        view.startAnimation(animation);
    }

    //TODO: animate fab translation and consider better value for up translation
    public static void translateFabButtonUp(FloatingActionMenu fabMenu, int snackbarHeight) {
        fabMenu.setTranslationY(-snackbarHeight);
    }

    public static void translateFabButtonDown(FloatingActionMenu fabMenu) {
        fabMenu.setTranslationY(0);
    }
}
