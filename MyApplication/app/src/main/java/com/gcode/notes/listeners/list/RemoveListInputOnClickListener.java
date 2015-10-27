package com.gcode.notes.listeners.list;

import android.view.View;

import com.gcode.notes.adapters.custom.BaseInputContainerAdapter;
import com.gcode.notes.ui.ActionExecutor;

public class RemoveListInputOnClickListener implements View.OnClickListener {
    BaseInputContainerAdapter mContainerAdapter;

    public RemoveListInputOnClickListener(BaseInputContainerAdapter baseInputContainerAdapter) {
        mContainerAdapter = baseInputContainerAdapter;
    }

    @Override
    public void onClick(View v) {
        View inputItem = (View) v.getParent();
        mContainerAdapter.removeInputItem(inputItem);
        ActionExecutor.popListItemDeletedSnackbar(mContainerAdapter, inputItem);
    }
}
