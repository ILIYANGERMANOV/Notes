package com.gcode.notes.activities.display.list.editable;

import android.view.Menu;

import com.gcode.notes.R;
import com.gcode.notes.notes.MyApplication;

import butterknife.OnClick;

public class DisplayListNormalActivity extends DisplayListEditableActivity {

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_display_list_normal, menu);
        return true;
    }
}
