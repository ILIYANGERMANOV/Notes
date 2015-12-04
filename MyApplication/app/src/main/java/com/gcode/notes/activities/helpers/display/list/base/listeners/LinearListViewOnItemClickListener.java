package com.gcode.notes.activities.helpers.display.list.base.listeners;


import android.view.View;

import com.gcode.notes.activities.display.list.DisplayListBaseActivity;
import com.gcode.notes.data.extras.ListDataItem;
import com.linearlistview.LinearListView;

public class LinearListViewOnItemClickListener implements LinearListView.OnItemClickListener {
    DisplayListBaseActivity mDisplayListBaseActivity;

    public LinearListViewOnItemClickListener(DisplayListBaseActivity displayListBaseActivity) {
        mDisplayListBaseActivity = displayListBaseActivity;
    }

    @Override
    public void onItemClick(LinearListView parent, View view, int position, long id) {
        ListDataItem item = mDisplayListBaseActivity.mDisplayAdapter.getItem(position);
        mDisplayListBaseActivity.mDisplayAdapter.remove(item);
        mDisplayListBaseActivity.mDisplayTickedAdapter.add(item);
    }
}
