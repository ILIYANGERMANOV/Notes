package com.gcode.notes.activities.display.list;

import android.view.Menu;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.ui.ActionExecutor;

import butterknife.OnClick;

public class DisplayListBinActivity extends DisplayListBaseActivity {
    @Override
    protected void displayListData() {
        super.displayListData();
        mActionImageButton.setImageResource(R.drawable.ic_restore_deleted_24dp);
    }

    @Override
    protected void setupRecyclerViews(boolean isDeactivated) {
        super.setupRecyclerViews(true);
    }

    @OnClick(R.id.display_action_image_button)
    public void restoreNote() {
        ActionExecutor.restoreDeletedNote(this, mListData);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_list_bin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_forever:
                ActionExecutor.deleteNoteFromDisplayBin(this, mListData);
                return true;
            case R.id.action_restore_deleted:
                ActionExecutor.restoreDeletedNote(this, mListData);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
