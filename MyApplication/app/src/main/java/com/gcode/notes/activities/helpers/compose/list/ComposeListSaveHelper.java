package com.gcode.notes.activities.helpers.compose.list;

import android.app.Activity;
import android.content.Intent;

import com.gcode.notes.activities.compose.list.ComposeListActivity;
import com.gcode.notes.activities.helpers.compose.ComposeBaseSaveHelper;
import com.gcode.notes.data.base.ContentBase;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.notes.MyApplication;
import com.gcode.notes.serialization.Serializer;
import com.gcode.notes.tasks.async.encryption.EncryptNoteTask;
import com.gcode.notes.tasks.async.encryption.callbacks.CryptTaskCallbacks;

public class ComposeListSaveHelper implements CryptTaskCallbacks {
    ComposeListActivity mComposeListActivity;

    public ComposeListSaveHelper(ComposeListActivity composeListActivity) {
        mComposeListActivity = composeListActivity;
    }

    public void saveList() {
        ListData listData = mComposeListActivity.mListData;

        if (!mComposeListActivity.mIsOpenedInEditMode) {
            //!NOTE contentBase type must be set before ComposeBaseSaveHelper#saveBase() cuz reminder won't be set correctly
            //when it is new note
            listData.setType(Constants.TYPE_LIST); //sets contentBase type to list
        }

        //setList here, cuz list is used in isValidList();
        listData.setList(mComposeListActivity.mContainerAdapter.getListDataItems(true)); //add not ticked listDataItems (filtering empty ones)
        listData.addToList(mComposeListActivity.mTickedContainerAdapter.getListDataItems(true)); //add ticked listDataItems (filtering empty ones)

        boolean hadValidTitleBeforeSaveBase = ComposeBaseSaveHelper.saveBase(mComposeListActivity, listData);
        //!NOTE ComposeBaseSaveHelper#saveBase() returns boolean which indicates whether the title was valid
        //used cuz in ComposeBaseSaveHelper#saveBase()
        //if title isn't valid will be generated and this will result in saving not valid notes
        //cuz listData#isValidNote will always return true

        if (listData.isValidList(hadValidTitleBeforeSaveBase)) {
            if (mComposeListActivity.mInPrivateMode) {
                //note is private mode, encrypt it before saving
                //!NOTE: onTaskCompletedSuccessfully or error callback will be called when ready
                new EncryptNoteTask(mComposeListActivity, this).execute(listData);
                return;
            }

            saveToDbAndSetResult(listData);
        } else {
            MyDebugger.toast(mComposeListActivity, "Cannot save empty list.");
        }
    }

    private void saveToDbAndSetResult(ListData listData) {
        Intent resultIntent = mComposeListActivity.mResultIntent;

        if (!mComposeListActivity.mIsOpenedInEditMode) {
            //save new list
            if (MyApplication.getWritableDatabase().insertNote(listData) != Constants.DATABASE_ERROR) {
                resultIntent.putExtra(Constants.NOTE_ADDED_SUCCESSFULLY, true);
                resultIntent.putExtra(Constants.COMPOSE_NOTE_MODE, listData.getMode());
            } else {
                MyDebugger.log("Failed to save list.");
                return;
            }
        } else {
            //update existing list
            if (MyApplication.getWritableDatabase().updateNote(listData)) {
                resultIntent.putExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, true);
                resultIntent.putExtra(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(listData));
                resultIntent.putExtra(Constants.EXTRA_NOTE_MODE_CHANGED, mComposeListActivity.mNoteModeChanged);
            } else {
                MyDebugger.log("Failed to update list.");
                return;
            }
        }

        //list saved successfully, set alarm for reminder
        ComposeBaseSaveHelper.setAlarmIfHasReminder(mComposeListActivity.getComposeReminderFragment(), listData);

        mComposeListActivity.setResult(Activity.RESULT_OK, resultIntent);
        mComposeListActivity.finish();
    }

    @Override
    public void onTaskCompletedSuccessfully(ContentBase contentBase) {
        //encrypted successfully, save list to db
        saveToDbAndSetResult(((ListData) contentBase));
    }
}
