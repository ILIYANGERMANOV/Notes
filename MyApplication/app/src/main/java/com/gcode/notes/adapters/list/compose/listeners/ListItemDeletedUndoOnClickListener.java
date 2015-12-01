package com.gcode.notes.adapters.list.compose.listeners;

import android.view.View;

import com.gcode.notes.adapters.list.compose.BaseComposeContainerAdapter;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;

public class ListItemDeletedUndoOnClickListener implements View.OnClickListener {
    BaseComposeContainerAdapter mContainerAdapter;
    View mRemovedItem;

    public ListItemDeletedUndoOnClickListener(BaseComposeContainerAdapter containerAdapter, View removedItem) {
        mContainerAdapter = containerAdapter;
        mRemovedItem = removedItem;
    }

    @Override
    public void onClick(View v) {
        int removedItemId = mContainerAdapter.getViewId(mRemovedItem);
        if (removedItemId != Constants.ERROR) {
            int previousItemPosition = removedItemId - 1;
            if (previousItemPosition >= 0) {
                //removedItem isn't first add it after previous item
                View previousItem = mContainerAdapter.getViewAtPosition(previousItemPosition);
                if (previousItem != null) {
                    //previousItem found ready to proceed
                    mContainerAdapter.addInputItemAfterView(previousItem, mRemovedItem, false);
                } else {
                    //previousItem is null, add removed item as last and focus it
                    MyDebugger.log("Previous item is null.");
                    mContainerAdapter.addInputItem(mRemovedItem, true);
                }
            } else {
                //removedItem was first in the container, add it as first
                mContainerAdapter.addInputItemAsFirst(mRemovedItem, false);
            }
        } else {
            //cannot find removed item id, add it as last and focus it
            MyDebugger.log("ERROR: Cannot retrieve removedItemId");
            mContainerAdapter.addInputItem(mRemovedItem, true);
        }
    }
}
