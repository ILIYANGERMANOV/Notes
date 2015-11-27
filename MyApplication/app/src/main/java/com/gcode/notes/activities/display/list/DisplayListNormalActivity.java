package com.gcode.notes.activities.display.list;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.ComposeListActivity;
import com.gcode.notes.data.main.ListData;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;
import com.gcode.notes.tasks.async.UpdateListAttributesTask;

import butterknife.OnClick;

public class DisplayListNormalActivity extends DisplayListBaseActivity {
    boolean mIsStarred;

    @Override
    protected void displayListData() {
        super.displayListData();
        if (mListData.isImportant()) {
            setStarredState();
        } else {
            setNotStarredState();
        }
    }

    @OnClick(R.id.display_action_image_button)
    public void starImageButtonClicked() {
        if (mIsStarred) {
            setNotStarredState();
        } else {
            setStarredState();
        }
        mListData.setImportant(mIsStarred);
        mNoteModeChanged = !mNoteModeChanged;
        MyApplication.getWritableDatabase().updateNoteMode(mListData);
    }

    private void setStarredState() {
        mIsStarred = true;
        mActionImageButton.setImageResource(R.drawable.ic_star_orange_36dp);
    }

    private void setNotStarredState() {
        mIsStarred = false;
        mActionImageButton.setImageResource(R.drawable.ic_star_border_black_36dp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: REFACTOR
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null && requestCode == Constants.COMPOSE_NOTE_REQUEST_CODE) {
            if (data.getBooleanExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, false)) {
                String serializedListData = data.getStringExtra(Constants.EXTRA_LIST_DATA);
                if (serializedListData != null) {
                    ListData listData = Serializer.parseListData(serializedListData);
                    if (listData != null) {
                        mNoteModeChanged = data.getBooleanExtra(Constants.EXTRA_NOTE_MODE_CHANGED, false);
                        mListData = listData;
                        displayListData();
                    }
                }
            }
        }
    }

    @Override
    protected void onStop() {
        if (mListData.getHasAttributesFlag()) {
            //save done/undone changes to database
            new UpdateListAttributesTask().execute(mListData);
        }
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_list_normal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            Intent intent = new Intent(this, ComposeListActivity.class);
            intent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(mListData));
            startActivityForResult(intent, Constants.COMPOSE_NOTE_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
