package com.gcode.notes.activities.helpers.compose.list;

import android.app.Activity;
import android.content.Intent;

import com.gcode.notes.activities.compose.list.ComposeListActivity;
import com.gcode.notes.activities.helpers.compose.ComposeBaseSaveHelper;
import com.gcode.notes.data.note.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.utils.DateUtils;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;

public class ComposeListSaveHelper {

    public static void saveList(ComposeListActivity composeListActivity) {
        ListData mListData = composeListActivity.mListData;

        if(!composeListActivity.mIsOpenedInEditMode) {
            //!NOTE contentBase type must be set before ComposeBaseSaveHelper#saveBase() cuz reminder won't be set correctly
            //when it is new note
            mListData.setType(Constants.TYPE_LIST); //sets contentBase type to list
        }

        //setList here, cuz list is used in isValidList();
        mListData.setList(composeListActivity.mContainerAdapter.getListDataItems(true)); //add not ticked listDataItems (filtering empty ones)
        mListData.addToList(composeListActivity.mTickedContainerAdapter.getListDataItems(true)); //add ticked listDataItems (filtering empty ones)

        boolean hadValidTitleBeforeSaveBase = ComposeBaseSaveHelper.saveBase(composeListActivity, mListData);
        //!NOTE ComposeBaseSaveHelper#saveBase() returns boolean which indicates whether the title was valid
        //used cuz in ComposeBaseSaveHelper#saveBase()
        //if title isn't valid will be generated and this will result in saving not valid notes
        //cuz listData#isValidNote will always return true

        if (mListData.isValidList(hadValidTitleBeforeSaveBase)) {
            Intent resultIntent = composeListActivity.mResultIntent;

            if (!composeListActivity.mIsOpenedInEditMode) {
                //save new list
                if (MyApplication.getWritableDatabase().insertNote(mListData) != Constants.DATABASE_ERROR) {
                    resultIntent.putExtra(Constants.NOTE_ADDED_SUCCESSFULLY, true);
                    resultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, mListData.getMode());
                } else {
                    MyDebugger.log("Failed to save list.");
                    return;
                }
            } else {
                //update existing list
                if (MyApplication.getWritableDatabase().updateNote(mListData)) {
                    resultIntent.putExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, true);
                    resultIntent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(mListData));
                    resultIntent.putExtra(Constants.EXTRA_NOTE_MODE_CHANGED, composeListActivity.mNoteModeChanged);
                } else {
                    MyDebugger.log("Failed to update list.");
                    return;
                }
            }
            composeListActivity.setResult(Activity.RESULT_OK, resultIntent);
        } else {
            MyDebugger.toast(composeListActivity, "Cannot save empty list.");
        }
    }
}
