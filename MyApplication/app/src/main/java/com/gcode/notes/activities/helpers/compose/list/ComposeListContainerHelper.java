package com.gcode.notes.activities.helpers.compose.list;

import com.gcode.notes.activities.compose.ComposeListActivity;
import com.gcode.notes.adapters.list.compose.ListComposeContainerAdapter;
import com.gcode.notes.adapters.list.compose.TickedListComposeContainerAdapter;
import com.gcode.notes.data.extras.ListDataItem;

import java.util.ArrayList;

public class ComposeListContainerHelper {
    public static void addListDataItems(ComposeListActivity composeListActivity, ArrayList<ListDataItem> listDataItems) {
        for (ListDataItem item : listDataItems) {
            if (!item.isChecked()) {
                composeListActivity.mContainerAdapter.addInputItem(item.getContent(), false);
            } else {
                composeListActivity.mTickedContainerAdapter.addInputItem(item.getContent(), false);
            }
        }
    }

    public static void setupContainers(ComposeListActivity composeListActivity) {
        if (composeListActivity.mContainerAdapter == null || composeListActivity.mTickedContainerAdapter == null) {
            composeListActivity.mContainerAdapter = new ListComposeContainerAdapter(composeListActivity.getContainer(),
                    composeListActivity.getScrollView(), composeListActivity.getLastDividerView());

            composeListActivity.mTickedContainerAdapter = new TickedListComposeContainerAdapter(composeListActivity.getTickedContainer(),
                    composeListActivity.getScrollView());

            composeListActivity.mContainerAdapter.setOtherContainerAdapter(composeListActivity.mTickedContainerAdapter);
            composeListActivity.mTickedContainerAdapter.setOtherContainerAdapter(composeListActivity.mContainerAdapter);
        }
    }
}
