package com.gcode.notes.activities.helpers.compose.list;

import android.app.Activity;
import android.content.Intent;

import com.gcode.notes.R;
import com.gcode.notes.activities.compose.ComposeListActivity;
import com.gcode.notes.data.main.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;

public class ComposeListSaveHelper {
    //TODO: optimize

    public static void saveList(ComposeListActivity composeListActivity) {
        ListData mListData = composeListActivity.mListData;

        mListData.setTitle(composeListActivity.getTitleEditText().getText().toString()); //TODO: remove redundancy with ComposeNoteSaveHelper
        mListData.setList(composeListActivity.mContainerAdapter.getListDataItems(true));
        mListData.addToList(composeListActivity.mTickedContainerAdapter.getListDataItems(true));

        if (mListData.isValidList()) {
            mListData.setMode(composeListActivity.mIsStarred ? Constants.MODE_IMPORTANT : Constants.MODE_NORMAL);

            //TODO: use real reminder
            String reminderString = composeListActivity.getReminderTextView().getText().toString();
            if (!reminderString.equals(composeListActivity.getResources().getString(R.string.compose_note_set_reminder_text))) {
                mListData.setReminder(reminderString);
            }

            mListData.setHasAttributesFlag(mListData.hasAttributes());

            Intent resultIntent = composeListActivity.mResultIntent;

            if (!composeListActivity.mIsOpenedInEditMode) {
                //save new list
                if (MyApplication.getWritableDatabase().insertNote(mListData) != Constants.DATABASE_ERROR) {
                    resultIntent.putExtra(Constants.NOTE_ADDED_SUCCESSFULLY, true);
                    resultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, mListData.getMode());
                } else {
                    MyDebugger.log("Failed to save list.");
                }
            } else {
                //update existing list
                mListData.setLastModifiedDate(DateUtils.getCurrentTimeSQLiteFormatted());
                if (MyApplication.getWritableDatabase().updateNote(mListData)) {
                    resultIntent.putExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, true);
                    resultIntent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(mListData));
                    resultIntent.putExtra(Constants.EXTRA_NOTE_MODE_CHANGED, composeListActivity.mNoteModeChanged);
                } else {
                    MyDebugger.log("Failed to update list.");
                }
            }
            composeListActivity.setResult(Activity.RESULT_OK, resultIntent);
        } else {
            MyDebugger.toast(composeListActivity, "Cannot save empty list.");
        }
    }
}
