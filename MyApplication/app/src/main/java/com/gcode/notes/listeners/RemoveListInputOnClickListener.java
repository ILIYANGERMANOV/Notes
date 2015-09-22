package com.gcode.notes.listeners;

import android.view.View;

import com.gcode.notes.adapters.custom.BaseInputContainerAdapter;

public class RemoveListInputOnClickListener implements View.OnClickListener {
    BaseInputContainerAdapter mContainerAdapter;

    public RemoveListInputOnClickListener(BaseInputContainerAdapter baseInputContainerAdapter) {
        mContainerAdapter = baseInputContainerAdapter;
    }

    @Override
    public void onClick(View v) {
        mContainerAdapter.removeInputItem((View) v.getParent());
    }
}
