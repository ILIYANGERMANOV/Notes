package com.gcode.notes.activities.display.list.editable;


import android.content.Intent;
import android.view.MenuItem;

import com.gcode.notes.activities.display.list.DisplayListBaseActivity;
import com.gcode.notes.activities.helpers.display.list.DisplayListBaseResultHandler;
import com.gcode.notes.activities.helpers.display.list.editable.DisplayListEditableMenuOptionsHelper;
import com.gcode.notes.activities.helpers.display.list.editable.DisplayListEditableResultHandler;
import com.gcode.notes.tasks.async.display.UpdateListAttributesTask;

public class DisplayListEditableActivity extends DisplayListBaseActivity {
    boolean mFinishCalled;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DisplayListEditableResultHandler.handleResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        if (mListData.getHasAttributesFlag()) {
            //list has attributes, save tasks changes (ticked/unticked) to database
            if (!mFinishCalled) {
                mListData.setList(mListDataItems);
                mListData.addToList(mTickedListDataItems);
            }
            new UpdateListAttributesTask().execute(mListData);
        }
        super.onStop();
    }

    @Override
    public void finish() {
        mFinishCalled = true;
        //set mListData list according activity's current state
        mListData.setList(mListDataItems);
        mListData.addToList(mTickedListDataItems);
        DisplayListBaseResultHandler.setResult(this);
        super.finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) ||
                DisplayListEditableMenuOptionsHelper.optionItemSelected(this, item);
    }
}
