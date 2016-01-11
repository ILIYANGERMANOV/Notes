package com.gcode.notes.activities.helpers.display.list.normal;

import android.app.Activity;
import android.content.Intent;

import com.gcode.notes.activities.display.list.DisplayListNormalActivity;
import com.gcode.notes.activities.helpers.display.list.DisplayListBaseTasksHelper;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class DisplayListNormalResultHandler {
    public static void handleResult(DisplayListNormalActivity displayListNormalActivity, int requestCode,
                                    int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && data != null && requestCode == Constants.COMPOSE_NOTE_REQUEST_CODE) {
            //result from compose note
            if (data.getBooleanExtra(Constants.NOTE_UPDATED_SUCCESSFULLY, false)) {
                //list updated successfully and passed as EXTRA_LIST_DATA, try to parse it from string
                ListData listData = Serializer.parseListData(data.getStringExtra(Constants.EXTRA_LIST_DATA));
                if (listData != null) {
                    //list parsed successfully, set it to activities list, display it and save if its mode was changed
                    displayListNormalActivity.mNoteModeChanged = data.getBooleanExtra(Constants.EXTRA_NOTE_MODE_CHANGED, false);
                    displayListNormalActivity.mListData = listData;
                    displayListNormalActivity.displayListData();
                    if (displayListNormalActivity.mIsDoneTasksHidden) {
                        //adapter hide done tasks, only when item adapter#add() is called (in this case adapter's clear(), addAll())
                        DisplayListBaseTasksHelper.hideDoneTasks(displayListNormalActivity);
                    }
                }
            }
        }
    }
}
