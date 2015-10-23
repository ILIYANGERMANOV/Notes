package com.gcode.notes.ui;

import android.graphics.Rect;
import android.view.View;
import android.widget.ScrollView;

public class VisibilityHelper {
    public static boolean isViewVisibleInScrollView(View view, ScrollView scrollView) {
        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);
        return view.getLocalVisibleRect(scrollBounds);
    }
}
