package com.gcode.notes.ui.helpers;

import android.widget.TextView;

import com.gcode.notes.extras.MyLogger;
import com.gcode.notes.extras.utils.MyUtils;

public class RTLHelper {
    public static void setDrawableStart(TextView view, int resourceId) {
        if (!MyUtils.isRTL()) {
            //normal directionality, set drawable left
            view.setCompoundDrawablesWithIntrinsicBounds(resourceId, 0, 0, 0);
        } else {
            //RTL directionality, set drawable right
            MyLogger.log("shnur");
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, resourceId, 0);
        }
    }
}
