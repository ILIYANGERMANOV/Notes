package com.gcode.notes.activities.helpers.display.list.listeners;


import android.view.View;

import com.gcode.notes.activities.display.list.DisplayListBaseActivity;
import com.gcode.notes.data.list.ListDataItem;
import com.linearlistview.LinearListView;

public class TickedLinearListViewOnItemClickListener implements LinearListView.OnItemClickListener {
    DisplayListBaseActivity mDisplayListBaseActivity;

    public TickedLinearListViewOnItemClickListener(DisplayListBaseActivity displayListBaseActivity) {
        mDisplayListBaseActivity = displayListBaseActivity;
    }

    @Override
    public void onItemClick(LinearListView parent, View view, int position, long id) {
        ListDataItem item = mDisplayListBaseActivity.mDisplayTickedAdapter.getItem(position);
        mDisplayListBaseActivity.mDisplayTickedAdapter.remove(item);
        mDisplayListBaseActivity.mDisplayAdapter.add(item);
    }
}
