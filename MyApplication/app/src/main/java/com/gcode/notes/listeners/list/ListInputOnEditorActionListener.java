package com.gcode.notes.listeners.list;


import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.gcode.notes.adapters.custom.BaseInputContainerAdapter;

public class ListInputOnEditorActionListener implements TextView.OnEditorActionListener {
    BaseInputContainerAdapter mContainerAdapter;

    public ListInputOnEditorActionListener(BaseInputContainerAdapter baseInputContainerAdapter) {
        mContainerAdapter = baseInputContainerAdapter;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        mContainerAdapter.addInputItemAfterView((View) v.getParent(), null, true);
        return true;
    }
}
