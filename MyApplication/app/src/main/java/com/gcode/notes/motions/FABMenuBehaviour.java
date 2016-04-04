package com.gcode.notes.motions;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.github.clans.fab.FloatingActionMenu;

import java.util.List;

/**
 * Created by Matteo on 08/08/2015.
 * <p/>
 * Floating Action Menu Behavior for Clans.FloatingActionButton
 * https://github.com/Clans/FloatingActionButton/
 * <p/>
 * Use this behavior as your app:layout_behavior attribute in your Floating Action Menu to use the
 * FabMenu in a Coordinator Layout.
 * <p/>
 * Remember to use the correct namespace for the fab:
 * xmlns:fab="http://schemas.android.com/apk/res-auto"
 */
public class FABMenuBehaviour extends CoordinatorLayout.Behavior {
    //TODO: REFACTOR AND OPTIMIZED (not mine and not very skilled implementation)
    private float mTranslationY;

    public FABMenuBehaviour(Context context, AttributeSet attrs) {
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        if (child instanceof FloatingActionMenu && dependency instanceof Snackbar.SnackbarLayout) {
            updateTranslation(parent, child, dependency);
        }
        return false;
    }

    private void updateTranslation(CoordinatorLayout parent, View child, View dependency) {
        float translationY = getTranslationY(parent, child);
        if (translationY != mTranslationY) {
            ViewCompat.animate(child)
                    .cancel();
            if (Math.abs(translationY - mTranslationY) == (float) dependency.getHeight()) {
                ViewCompat.animate(child)
                        .translationY(translationY);
            } else {
                ViewCompat.setTranslationY(child, translationY);
            }
            mTranslationY = translationY;
        }

    }

    private float getTranslationY(CoordinatorLayout parent, View child) {
        float minOffset = 0.0f;
        List dependencies = parent.getDependencies(child);

        for (int dependenciesCount = dependencies.size(), i = 0; i < dependenciesCount; ++i) {
            View view = (View) dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(child, view)) {
                minOffset = Math.min(minOffset, ViewCompat.getTranslationY(view) - (float) view.getHeight());
            }
        }

        return minOffset;
    }
}