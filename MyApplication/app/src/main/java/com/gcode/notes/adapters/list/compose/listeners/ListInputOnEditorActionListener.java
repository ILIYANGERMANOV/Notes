package com.gcode.notes.adapters.list.compose.listeners;


import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.gcode.notes.adapters.list.compose.BaseComposeContainerAdapter;

public class ListInputOnEditorActionListener implements TextView.OnEditorActionListener {
    BaseComposeContainerAdapter mContainerAdapter;

    public ListInputOnEditorActionListener(BaseComposeContainerAdapter baseComposeContainerAdapter) {
        mContainerAdapter = baseComposeContainerAdapter;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        //cast it to String in order to not be ambiguous
        mContainerAdapter.addInputItemAfterView((View) v.getParent(), (String) null, true);
        return true;
    }
}
