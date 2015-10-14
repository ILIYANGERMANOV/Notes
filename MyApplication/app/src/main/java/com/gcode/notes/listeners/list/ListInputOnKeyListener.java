package com.gcode.notes.listeners.list;


import android.view.KeyEvent;
import android.view.View;

import com.gcode.notes.adapters.custom.BaseInputContainerAdapter;

public class ListInputOnKeyListener implements View.OnKeyListener {
    BaseInputContainerAdapter mContainerAdapter;

    public ListInputOnKeyListener(BaseInputContainerAdapter baseInputContainerAdapter) {
        mContainerAdapter = baseInputContainerAdapter;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    mContainerAdapter.addInputItemAfterView((View) v.getParent(), null, true);
                    return true;
                default:
                    break;
            }
        }
        return false;
    }
}
