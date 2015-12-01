package com.gcode.notes.listeners.list;

import android.view.View;

import com.gcode.notes.activities.compose.ComposeListActivity;
import com.gcode.notes.extras.values.Constants;

public class TitleEditTextOnFocusChangeListener implements View.OnFocusChangeListener {
    ComposeListActivity mComposeListActivity;

    public TitleEditTextOnFocusChangeListener(ComposeListActivity composeListActivity) {
        mComposeListActivity = composeListActivity;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        //if title is focused drop focus from containers
        mComposeListActivity.mContainerAdapter.setLastFocused(Constants.NO_FOCUS);
        mComposeListActivity.mTickedContainerAdapter.setLastFocused(Constants.NO_FOCUS);
    }
}
