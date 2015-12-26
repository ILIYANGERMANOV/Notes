package com.gcode.notes.ui.helpers;

import android.graphics.Paint;
import android.widget.CheckedTextView;

public class CheckedTextViewHelper {
    public static void setChecked(CheckedTextView checkedTextView) {
        checkedTextView.setChecked(true);
        PaintFlagsHelper.setStrikeThrough(checkedTextView);
    }

    public static void setUnchecked(CheckedTextView checkedTextView) {
        checkedTextView.setChecked(false);
        PaintFlagsHelper.unsetStrikeThrough(checkedTextView);
    }
}
