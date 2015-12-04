package com.gcode.notes.activities.helpers.display.list.base;


import android.view.View;

import com.gcode.notes.R;
import com.gcode.notes.activities.display.list.DisplayListBaseActivity;

public class DisplayListBaseTasksHelper {
    public static void hideDoneTasks(DisplayListBaseActivity displayListBaseActivity) {
        displayListBaseActivity.getTickedLinearListView().setVisibility(View.GONE);
        displayListBaseActivity.getDoneButton().setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_expand_less_black_24dp, 0, 0, 0);
        displayListBaseActivity.mIsDoneTasksHidden = true;
        displayListBaseActivity.mDisplayTickedAdapter.setDoneHidden(true);
    }

    public static void showDoneTasks(DisplayListBaseActivity displayListBaseActivity) {
        displayListBaseActivity.getTickedLinearListView().setVisibility(View.VISIBLE);
        displayListBaseActivity.getDoneButton().setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_expand_more_black_24dp, 0, 0, 0);
        displayListBaseActivity.mIsDoneTasksHidden = false;
        displayListBaseActivity.mDisplayTickedAdapter.setDoneHidden(false);
    }
}
