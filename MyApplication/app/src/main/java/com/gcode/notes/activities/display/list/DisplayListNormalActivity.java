package com.gcode.notes.activities.display.list;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.helpers.display.list.DisplayListBaseResultHandler;
import com.gcode.notes.activities.helpers.display.list.normal.DisplayListNormalMenuOptionsHelper;
import com.gcode.notes.activities.helpers.display.list.normal.DisplayListNormalResultHandler;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.tasks.async.UpdateListAttributesTask;

import butterknife.OnClick;

public class DisplayListNormalActivity extends DisplayListBaseActivity {
    boolean mFinishCalled;

    @Override
    public void displayListData() {
        super.displayListData();
        if (mListData.isImportant()) {
            setStarredState();
        } else {
            setNotStarredState();
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.display_action_image_button)
    public void starImageButtonClicked() {
        if (mIsStarred) {
            setNotStarredState();
        } else {
            setStarredState();
        }
        mListData.setModeImportant(mIsStarred);
        mNoteModeChanged = !mNoteModeChanged;
        MyApplication.getWritableDatabase().updateNoteMode(mListData);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DisplayListNormalResultHandler.handleResult(this, requestCode, resultCode, data);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_list_normal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item) || DisplayListNormalMenuOptionsHelper.optionItemSelected(this, item);
    }
}
