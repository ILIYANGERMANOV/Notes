package com.gcode.notes.activities.helpers.display.list;

import android.view.View;

import com.gcode.notes.activities.display.list.DisplayListBaseActivity;
import com.gcode.notes.activities.helpers.display.list.listeners.LinearListViewOnItemClickListener;
import com.gcode.notes.activities.helpers.display.list.listeners.TickedLinearListViewOnItemClickListener;
import com.gcode.notes.adapters.list.display.ListDisplayAdapter;
import com.gcode.notes.adapters.list.display.ListDisplayTickedAdapter;
import com.gcode.notes.data.list.ListDataItem;

import java.util.ArrayList;

public class DisplayListBaseContainersHelper {
    public static void setupLinearListViews(DisplayListBaseActivity displayListBaseActivity, boolean isDeactivated) {
        displayListBaseActivity.mListDataItems = new ArrayList<>(); //creates list used by the adapter
        displayListBaseActivity.mTickedListDataItems = new ArrayList<>(); //creates list used by the adapter

        fillListDataItemLists(displayListBaseActivity); //adds list data items to relevant list and hides/show done button

        displayListBaseActivity.mDisplayAdapter = new ListDisplayAdapter(displayListBaseActivity,
                displayListBaseActivity.mListDataItems, isDeactivated);

        displayListBaseActivity.mDisplayTickedAdapter = new ListDisplayTickedAdapter(displayListBaseActivity,
                displayListBaseActivity.mTickedListDataItems, isDeactivated,
                displayListBaseActivity.getDoneButton(), displayListBaseActivity.getRootScrollView(),
                displayListBaseActivity.getDatesTextView(), displayListBaseActivity.getTickedLinearListView());

        displayListBaseActivity.getLinearListView().setAdapter(displayListBaseActivity.mDisplayAdapter); //chains adapters
        displayListBaseActivity.getTickedLinearListView().setAdapter(displayListBaseActivity.mDisplayTickedAdapter); //chains adapters

        displayListBaseActivity.getLinearListView().setOnItemClickListener(
                new LinearListViewOnItemClickListener(displayListBaseActivity));

        displayListBaseActivity.getTickedLinearListView().setOnItemClickListener(
                new TickedLinearListViewOnItemClickListener(displayListBaseActivity));
    }

    public static void fillListDataItemLists(DisplayListBaseActivity displayListBaseActivity) {
        //mListData#getList() method secures that list won't be null
        for (ListDataItem listDataItem : displayListBaseActivity.mListData.getList()) {
            if (!listDataItem.isChecked()) {
                //item is not checked, add it to list (not checked list)
                displayListBaseActivity.mListDataItems.add(listDataItem);
            } else {
                //item is checked, add it to ticked list (checked list)
                displayListBaseActivity.mTickedListDataItems.add(listDataItem);
            }
        }

        if (displayListBaseActivity.mTickedListDataItems.isEmpty()) {
            //there are no ticked (done) items, hide done button
            displayListBaseActivity.getDoneButton().setVisibility(View.GONE);
        } else {
            //there are ticked (done) items, show done button
            displayListBaseActivity.getDoneButton().setVisibility(View.VISIBLE);
        }
    }
}
