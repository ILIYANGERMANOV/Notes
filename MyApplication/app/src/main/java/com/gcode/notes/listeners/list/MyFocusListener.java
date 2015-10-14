package com.gcode.notes.listeners.list;

import android.view.View;

import com.gcode.notes.adapters.custom.BaseInputContainerAdapter;

public class MyFocusListener implements View.OnFocusChangeListener {
    BaseInputContainerAdapter mContainerAdapter;

    public MyFocusListener(BaseInputContainerAdapter baseInputContainerAdapter) {
        mContainerAdapter = baseInputContainerAdapter;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mContainerAdapter.setLastFocused((View) v.getParent());
        }
    }
}
