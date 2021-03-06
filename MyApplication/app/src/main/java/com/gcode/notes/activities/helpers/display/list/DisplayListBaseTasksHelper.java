package com.gcode.notes.activities.helpers.display.list;

import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.activities.display.list.DisplayListBaseActivity;
import com.gcode.notes.ui.helpers.RTLHelper;

public class DisplayListBaseTasksHelper {
    public static void hideDoneTasks(DisplayListBaseActivity displayListBaseActivity) {
        displayListBaseActivity.getTickedLinearListView().setVisibility(View.GONE);
        RTLHelper.setDrawableStart(displayListBaseActivity.getDoneButton(), R.drawable.ic_display_list_less);
        displayListBaseActivity.mIsDoneTasksHidden = true;
        displayListBaseActivity.mDisplayTickedAdapter.setDoneHidden(true);
    }

    public static void showDoneTasks(DisplayListBaseActivity displayListBaseActivity) {
        displayListBaseActivity.getTickedLinearListView().setVisibility(View.VISIBLE);
        RTLHelper.setDrawableStart(displayListBaseActivity.getDoneButton(), R.drawable.ic_display_list_more);
        displayListBaseActivity.mIsDoneTasksHidden = false;
        displayListBaseActivity.mDisplayTickedAdapter.setDoneHidden(false);
    }
}
