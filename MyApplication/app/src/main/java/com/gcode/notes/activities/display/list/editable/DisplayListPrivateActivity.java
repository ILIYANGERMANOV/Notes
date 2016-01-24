package com.gcode.notes.activities.display.list.editable;

import android.view.Menu;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.display.DisplayBasePrivateOptionsHelper;
import com.gcode.notes.ui.ActionExecutor;

import butterknife.OnClick;

public class DisplayListPrivateActivity extends DisplayListEditableActivity {
    @Override
    public void displayListData() {
        super.displayListData();
        getActionImageButton().setImageResource(R.drawable.ic_lock_open_black_24dp);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.display_action_image_button)
    public void starImageButtonClicked() {
        ActionExecutor.unlockNote(this, mListData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_list_private, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) ||
                DisplayBasePrivateOptionsHelper.optionItemSelected(this, item, mListData);
    }
}
