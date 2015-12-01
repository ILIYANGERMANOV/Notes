package com.gcode.notes.adapters.list.compose.listeners;

import android.view.View;

import com.gcode.notes.adapters.list.compose.BaseComposeContainerAdapter;
import com.gcode.notes.ui.ActionExecutor;

public class RemoveListInputOnClickListener implements View.OnClickListener {
    BaseComposeContainerAdapter mContainerAdapter;

    public RemoveListInputOnClickListener(BaseComposeContainerAdapter baseComposeContainerAdapter) {
        mContainerAdapter = baseComposeContainerAdapter;
    }

    @Override
    public void onClick(View v) {
        View inputItem = (View) v.getParent();
        //if item to remove is focused, request focus on previous item
        mContainerAdapter.removeInputItem(inputItem, mContainerAdapter.getEditTextFromView(inputItem).isFocused());
        ActionExecutor.popListItemDeletedSnackbar(mContainerAdapter, inputItem);
    }
}
