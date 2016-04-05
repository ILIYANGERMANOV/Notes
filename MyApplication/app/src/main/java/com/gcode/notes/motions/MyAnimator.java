package com.gcode.notes.motions;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.github.clans.fab.FloatingActionMenu;

public class MyAnimator {
    public static void startAnimationOnView(Context context, View view, int animationResourceId) {
        view.startAnimation(AnimationUtils.loadAnimation(context, animationResourceId));
    }

    //TODO: animate fab translation and consider better value for up translation
    public static void translateFabUp(FloatingActionMenu fabMenu, int snackbarHeight) {
        TranslateAnimation anim = new TranslateAnimation(0, 0, 0, -snackbarHeight);
        anim.setDuration(200);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        fabMenu.startAnimation(anim);
        // fabMenu.setTranslationY(-snackbarHeight);
    }

    public static void translateFabDown(FloatingActionMenu fabMenu, int snackbarHeight) {
        TranslateAnimation anim = new TranslateAnimation(0, 0, -snackbarHeight, 0);
        anim.setDuration(200);
        anim.setFillEnabled(true);
        anim.setFillAfter(true);
        fabMenu.startAnimation(anim);
        //fabMenu.setTranslationY(0);
    }
}
