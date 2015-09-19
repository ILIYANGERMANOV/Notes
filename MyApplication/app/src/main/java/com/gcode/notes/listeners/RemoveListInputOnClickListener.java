package com.gcode.notes.listeners;

import android.view.View;

import com.gcode.notes.adapters.custom.BaseContainerAdapter;

public class RemoveListInputOnClickListener implements View.OnClickListener {
    BaseContainerAdapter mContainerAdapter;

    public RemoveListInputOnClickListener(BaseContainerAdapter baseContainerAdapter) {
        mContainerAdapter = baseContainerAdapter;
    }

    @Override
    public void onClick(View v) {
        mContainerAdapter.removeInputItem((View) v.getParent());
    }
}
