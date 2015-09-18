package com.gcode.notes.listeners;


import android.view.KeyEvent;
import android.view.View;

import com.gcode.notes.adapters.MyCustomContainerAdapter;

public class ListInputOnKeyListener implements View.OnKeyListener {
    MyCustomContainerAdapter mContainerAdapter;

    public ListInputOnKeyListener(MyCustomContainerAdapter myCustomContainerAdapter) {
        mContainerAdapter = myCustomContainerAdapter;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_DPAD_CENTER:
                case KeyEvent.KEYCODE_ENTER:
                    mContainerAdapter.addInputItemAfterView((View) v.getParent());
                    return true;
                default:
                    break;
            }
        }
        return false;
    }
}
