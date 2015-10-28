package com.gcode.notes.listeners.list;

import android.view.View;

import com.gcode.notes.adapters.list.compose.BaseComposeContainerAdapter;

public class MyFocusListener implements View.OnFocusChangeListener {
    BaseComposeContainerAdapter mContainerAdapter;

    public MyFocusListener(BaseComposeContainerAdapter baseComposeContainerAdapter) {
        mContainerAdapter = baseComposeContainerAdapter;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            mContainerAdapter.setLastFocused((View) v.getParent());
        }
    }
}
