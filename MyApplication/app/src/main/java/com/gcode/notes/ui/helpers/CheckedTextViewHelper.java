package com.gcode.notes.ui.helpers;

import android.graphics.Paint;
import android.widget.CheckedTextView;

public class CheckedTextViewHelper {
    public static void setChecked(CheckedTextView checkedTextView) {
        checkedTextView.setChecked(true);
        checkedTextView.setPaintFlags(checkedTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
    }

    public static void setUnchecked(CheckedTextView checkedTextView) {
        checkedTextView.setChecked(false);
        checkedTextView.setPaintFlags(checkedTextView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
    }
}
