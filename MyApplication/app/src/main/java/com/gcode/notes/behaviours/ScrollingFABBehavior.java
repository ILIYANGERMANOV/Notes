package com.gcode.notes.behaviours;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.View;

import com.gcode.notes.activities.MainActivity;
import com.gcode.notes.extras.Constants;
import com.gcode.notes.extras.Utils;

public class ScrollingFABBehavior extends FloatingActionButton.Behavior {
    private int toolbarHeight;

    public ScrollingFABBehavior(Context context, AttributeSet attrs) {
        super();
        this.toolbarHeight = Utils.getToolbarHeight(context);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        return super.layoutDependsOn(parent, fab, dependency) || (dependency instanceof AppBarLayout);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton fab, View dependency) {
        boolean returnValue = super.onDependentViewChanged(parent, fab, dependency);
        if (dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = fab.getHeight() + fabBottomMargin;
            float ratio = dependency.getY() / (float) toolbarHeight;
            fab.setTranslationY(-distanceToScroll * ratio);

            if (fab.getTranslationY() > Constants.FAB_THRESHOLD_TRANSLATION_Y &&
                    MainActivity.mActionMenu != null && MainActivity.mActionMenu.isOpen()) {

                if (fab.getTranslationY() < Constants.FAB_MAX_TRANSLATION_Y) {
                    MainActivity.mActionMenu.close(true);
                } else {
                    MainActivity.mActionMenu.close(false);
                }
            }

            fab.clearAnimation();
        }
        return returnValue;
    }
}