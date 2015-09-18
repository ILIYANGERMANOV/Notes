package com.gcode.notes.listeners;

import android.view.View;

import com.gcode.notes.adapters.MyCustomContainerAdapter;

public class RemoveListInputOnClickListener implements View.OnClickListener {
    MyCustomContainerAdapter mContainerAdapter;

    public RemoveListInputOnClickListener(MyCustomContainerAdapter myCustomContainerAdapter) {
        mContainerAdapter = myCustomContainerAdapter;
    }

    @Override
    public void onClick(View v) {
        mContainerAdapter.removeInputItem((View) v.getParent());
    }
}
