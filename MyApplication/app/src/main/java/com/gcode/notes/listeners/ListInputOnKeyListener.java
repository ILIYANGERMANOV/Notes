package com.gcode.notes.listeners;


import android.view.KeyEvent;
import android.view.View;

import com.gcode.notes.adapters.custom.BaseContainerAdapter;

public class ListInputOnKeyListener implements View.OnKeyListener {
    BaseContainerAdapter mContainerAdapter;

    public ListInputOnKeyListener(BaseContainerAdapter baseContainerAdapter) {
        mContainerAdapter = baseContainerAdapter;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    mContainerAdapter.addInputItemAfterView((View) v.getParent(), null);
                    return true;
                default:
                    break;
            }
        }
        return false;
    }
}
