package com.gcode.notes.activities.helpers.display.list.base;

import com.gcode.notes.activities.display.list.DisplayListBaseActivity;

public class DisplayListBaseDisplayHelper {
    public static void displayListData(DisplayListBaseActivity displayListBaseActivity) {
        displayListBaseActivity.mListData.displayBase(displayListBaseActivity.getTitleTextView());
        displayListBaseActivity.getDatesTextView().setText(displayListBaseActivity.mListData.getDateDetails());
        if (displayListBaseActivity.mListDataItems == null || displayListBaseActivity.mTickedListDataItems == null) {
            //displayListData for first time
            displayListBaseActivity.setupLinearListViews(false); //creates adapters and fills them with items
        } else {
            //adapters are already created, change their lists content (comeback with save from compose activity)
            displayListBaseActivity.mListDataItems.clear();
            displayListBaseActivity.mTickedListDataItems.clear();

            DisplayListBaseContainersHelper.fillListDataItemLists(displayListBaseActivity);

            displayListBaseActivity.mDisplayAdapter.notifyDataSetChanged();
            displayListBaseActivity.mDisplayTickedAdapter.notifyDataSetChanged();
        }
    }
}
