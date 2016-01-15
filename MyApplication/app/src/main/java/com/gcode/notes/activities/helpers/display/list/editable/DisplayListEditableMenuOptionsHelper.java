package com.gcode.notes.activities.helpers.display.list.editable;


import android.content.Intent;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.list.ComposeListActivity;
import com.gcode.notes.activities.display.list.editable.DisplayListEditableActivity;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class DisplayListEditableMenuOptionsHelper {
    public static boolean optionItemSelected(DisplayListEditableActivity displayListEditableActivity, MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(displayListEditableActivity, ComposeListActivity.class);
            intent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(displayListEditableActivity.mListData));
            displayListEditableActivity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
            return true;
        }
        return false;
    }
}
