package com.gcode.notes.activities.display.list;

import android.view.Menu;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.display.list.base.DisplayListBaseResultHandler;
import com.gcode.notes.activities.helpers.display.list.bin.DisplayListBinMenuOptionsHelper;
import com.gcode.notes.ui.ActionExecutor;

import butterknife.OnClick;

public class DisplayListBinActivity extends DisplayListBaseActivity {
    @Override
    public void displayListData() {
        super.displayListData();
        mActionImageButton.setImageResource(R.drawable.ic_restore_deleted_24dp);
    }

    @Override
    public void setupLinearListViews(boolean isDeactivated) {
        super.setupLinearListViews(true);
        //TODO: onItemClick show restore note snackbar
    }

    @OnClick(R.id.display_action_image_button)
    public void restoreNote() {
        ActionExecutor.restoreDeletedNote(this, mListData);
    }

    @Override
    public void finish() {
        DisplayListBaseResultHandler.setResult(this);
        super.finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_list_bin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) || DisplayListBinMenuOptionsHelper.optionItemSelected(this, item);
    }
}
