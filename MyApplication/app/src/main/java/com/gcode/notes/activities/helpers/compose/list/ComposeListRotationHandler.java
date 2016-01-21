package com.gcode.notes.activities.helpers.compose.list;

import android.os.Bundle;
import android.os.Handler;

import com.gcode.notes.activities.compose.list.ComposeListActivity;
import com.gcode.notes.activities.helpers.compose.ComposeBaseRotationHandler;
import com.gcode.notes.data.list.ListData;
import com.gcode.notes.extras.MyDebugger;
import com.gcode.notes.extras.values.Constants;
import com.gcode.notes.serialization.Serializer;

public class ComposeListRotationHandler {
    public static void saveInstanceState(ComposeListActivity composeListActivity, Bundle outState) {
        ComposeBaseRotationHandler.saveInstanceState(composeListActivity, outState); //save base
        ListData listData = composeListActivity.mListData; //get listData reference for easier access

        listData.setList(composeListActivity.mContainerAdapter.getListDataItems(false)); //set list to not ticked items
        listData.addToList(composeListActivity.mTickedContainerAdapter.getListDataItems(false)); //add to list ticked items

        outState.putString(Constants.EXTRA_LIST_DATA, Serializer.serializeListData(listData));

        int lastFocused = composeListActivity.mContainerAdapter.getLastFocused();
        if (lastFocused != Constants.NO_FOCUS) {
            //adding 1 in order to escape problems with id 0
            outState.putInt(Constants.EXTRA_LAST_FOCUSED, lastFocused + 1);
        } else {
            lastFocused = composeListActivity.mTickedContainerAdapter.getLastFocused();
            if (lastFocused != Constants.NO_FOCUS) {
                //if last focused is from ticked items its passed negated
                //adding 1 in order to escape problems with id 0
                outState.putInt(Constants.EXTRA_LAST_FOCUSED, (lastFocused + 1) * -1);
            }
        }
    }

    public static void handlerScreenRotation(final ComposeListActivity composeListActivity, final Bundle savedInstanceState) {
        ComposeBaseRotationHandler.handlerScreenRotation(composeListActivity, savedInstanceState); //handle base
        //TODO: fix skipping frames by parsing note on another thread
        final ListData listData = Serializer.parseListData(
                savedInstanceState.getString(Constants.EXTRA_LIST_DATA));
        if (listData != null) {
            composeListActivity.mListData = listData;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() { //postDelayed because layout isn't loaded and adding items to it and requesting focus leads to crash
                    ComposeListContainerHelper.addListDataItems(composeListActivity, listData.getList());

                    //WARNING: lastFocused MUST BE lastFocused -= 1
                    int lastFocused = savedInstanceState.getInt(Constants.EXTRA_LAST_FOCUSED, Constants.NO_FOCUS);
                    if (Math.abs(lastFocused) != Constants.NO_FOCUS) {
                        if (lastFocused > 0) {
                            //focused from mContainerAdapter
                            composeListActivity.mContainerAdapter.setFocusOnChild(lastFocused - 1); //-1 cuz it was passed by +1 in save instance
                        } else {
                            //focused from mTickedContainerAdapter (lastFocused is passed negated)
                            composeListActivity.mTickedContainerAdapter.setFocusOnChild(lastFocused * -1 - 1);  //-1 cuz it was passed by +1 in save instance
                        }
                    } else {
                        //there was no_focus, request focus on title
                        composeListActivity.getTitleEditText().requestFocus();
                    }
                }
            }, Constants.MINIMUM_DELAY);
        } else {
            MyDebugger.log("handleScreenRotation", "listData is null");
        }
    }
}
