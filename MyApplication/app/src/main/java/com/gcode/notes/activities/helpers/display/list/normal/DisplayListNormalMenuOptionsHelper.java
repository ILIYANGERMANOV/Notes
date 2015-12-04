package com.gcode.notes.activities.helpers.display.list.normal;


import android.content.Intent;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.list.ComposeListActivity;
import com.gcode.notes.activities.display.list.DisplayListNormalActivity;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class DisplayListNormalMenuOptionsHelper {
    public static boolean optionItemSelected(DisplayListNormalActivity displayListNormalActivity, MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(displayListNormalActivity, ComposeListActivity.class);
            intent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(displayListNormalActivity.mListData));
            displayListNormalActivity.startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
            return true;
        }
        return false;
    }
}
