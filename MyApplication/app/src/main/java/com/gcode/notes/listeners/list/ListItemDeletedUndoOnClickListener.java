package com.gcode.notes.listeners.list;

import android.view.View;

import com.gcode.notes.adapters.custom.BaseInputContainerAdapter;

public class ListItemDeletedUndoOnClickListener implements View.OnClickListener {
    BaseInputContainerAdapter mContainerAdapter;
    View mRemovedItem;

    public ListItemDeletedUndoOnClickListener(BaseInputContainerAdapter containerAdapter, View removedItem) {
        mContainerAdapter = containerAdapter;
        mRemovedItem = removedItem;
    }

    @Override
    public void onClick(View v) {
        String inputItemContent = mContainerAdapter.getEditTextFromView(mRemovedItem).getText().toString();
        mContainerAdapter.addInputItem(inputItemContent, true);
    }
}
