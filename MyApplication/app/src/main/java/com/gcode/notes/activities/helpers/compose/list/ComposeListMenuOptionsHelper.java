package com.gcode.notes.activities.helpers.compose.list;

import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.list.ComposeListActivity;

public class ComposeListMenuOptionsHelper {
    public static boolean optionsItemSelected(ComposeListActivity composeListActivity, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                new ComposeListSaveHelper(composeListActivity).saveList();
        }
        return false;
    }
}
